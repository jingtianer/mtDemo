package com.jingtian.mtdemo.UI.Refresh

import android.R
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.Transformation
import android.widget.AbsListView
import android.widget.ImageView
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewCompat

class PullRefreshLayout @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    ViewGroup(context, attrs) {
    private var mTarget: View? = null
    private val mRefreshView: ImageView = ImageView(context)
    private val mLoadView: ImageView = ImageView(context)
    private val mDecelerateInterpolator: Interpolator
    private val mTouchSlop: Int
    val finalOffset: Int = dp2px(DRAG_MAX_DISTANCE)
    private val mTotalDragDistance: Int
    private var mRefreshDrawable: RefreshDrawable? = null
    private var mLoadDrawable: RefreshDrawable? = null
    private var mCurrentOffsetTop = 0
    private var mRefreshing = false
    private var mLoading = false
    private var mActivePointerId = 0
    private var mIsBeingDragged = false
    private var mInitialMotionY = 0f
    private var mFrom = 0
    private var mNotify = false
    private var mRefreshListener: OnRefreshListener? = null
    private var mLoadLisener: OnLoadListener? = null
    var mDurationToStartPosition: Int
    var mDurationToCorrectPosition: Int
    private var mInitialOffsetTop = 0
    private var mDispatchTargetTouchDown = false
    private var mDragPercent = 0f
    private val mMode = RefreshMode.getDefault()

    //之前手势的方向，为了解决同一个触点前后移动方向不同导致后一个方向会刷新的问题，
    //这里Mode.DISABLED无意义，只是一个初始值，和上拉/下拉方向进行区分
    private var mLastDirection: RefreshMode = RefreshMode.DISABLE

    //当子控件移动到尽头时才开始计算初始点的位置
    private var mStartPoint = 0f
    private var up = false
    private var down = false
    fun setRefreshDrawable(drawable: RefreshDrawable?) {
        setRefreshing(false)
        mRefreshDrawable = drawable
        mRefreshView.setImageDrawable(mRefreshDrawable)
    }

    fun setLoadDrawable(drawable: RefreshDrawable?) {
        setLoading(false)
        mLoadDrawable = drawable
        mLoadView.setImageDrawable(mLoadDrawable)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        ensureTarget()
        if (mTarget == null) return
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
            measuredWidth - paddingRight - paddingLeft,
            MeasureSpec.EXACTLY
        )
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
            measuredHeight - paddingTop - paddingBottom,
            MeasureSpec.EXACTLY
        )
        mTarget!!.measure(widthMeasureSpec, heightMeasureSpec)
        mRefreshView.measure(widthMeasureSpec, heightMeasureSpec)
        mLoadView.measure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun ensureTarget() {
        if (mTarget != null) return
        if (childCount > 0) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child !== mRefreshView && child !== mLoadView) mTarget = child
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (!isEnabled || canChildScrollUp() && canChildScrollDown() && !mRefreshing) {
            return false
        }
        val action: Int = MotionEventCompat.getActionMasked(ev)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mRefreshing || !mLoading) {
                    setTargetOffsetTop(0, true)
                }
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0)
                mIsBeingDragged = false
                val initialMotionY = getMotionEventY(ev, mActivePointerId)
                if (initialMotionY == -1f) {
                    return false
                }
                mInitialMotionY = initialMotionY
                mInitialOffsetTop = mCurrentOffsetTop
                mDispatchTargetTouchDown = false
                mDragPercent = 0f
                mStartPoint = mInitialMotionY

                //这里用up/down记录子控件能否下拉，如果当前子控件不能上下滑动，但当手指按下并移动子控件时，控件就会变得可滑动
                //后面的一些处理不能直接使用canChildScrollUp/canChildScrollDown
                //但仍存在问题：当数据不满一屏且设置可以上拉模式后，多次快速上拉会激发上拉加载
                up = canChildScrollUp()
                down = canChildScrollDown()
            }
            MotionEvent.ACTION_MOVE -> {
                if (mActivePointerId == INVALID_POINTER) {
                    return false
                }
                val y = getMotionEventY(ev, mActivePointerId)
                if (y == -1f) {
                    return false
                }
                val yDiff = y - mStartPoint
                if (mLastDirection === RefreshMode.PULL_FROM_START && yDiff < 0 ||
                    mLastDirection === RefreshMode.PULL_FROM_END && yDiff > 0
                ) {
                    return false
                }
                //下拉或上拉时，子控件本身能够滑动时，记录当前手指位置，当其滑动到尽头时，
                //mStartPoint作为下拉刷新或上拉加载的手势起点
                if (canChildScrollUp() && yDiff > 0 || canChildScrollDown() && yDiff < 0) {
                    mStartPoint = y
                }
                //下拉
                if (yDiff > mTouchSlop) {
                    //若当前子控件能向下滑动，或者上个手势为上拉，则返回
                    if (canChildScrollUp() || mMode === RefreshMode.PULL_FROM_END) {
                        mIsBeingDragged = false
                        return false
                    }
                    if (mMode === RefreshMode.PULL_FROM_START || mMode === RefreshMode.BOTH) {
                        mIsBeingDragged = true
                        mLastDirection = RefreshMode.PULL_FROM_START
                    }
                } else if (-yDiff > mTouchSlop) {
                    //若当前子控件能向上滑动，或者上个手势为下拉，则返回
                    if (canChildScrollDown() || mMode === RefreshMode.PULL_FROM_START) {
                        mIsBeingDragged = false
                        return false
                    }
                    //若子控件不能上下滑动，说明数据不足一屏，若不满屏不加载，返回
                    if (!up && !down) {
                        mIsBeingDragged = false
                        return false
                    }
                    if (mMode === RefreshMode.PULL_FROM_END || mMode === RefreshMode.BOTH) {
                        mIsBeingDragged = true
                        mLastDirection = RefreshMode.PULL_FROM_END
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mIsBeingDragged = false
                mActivePointerId = INVALID_POINTER
                mLastDirection = RefreshMode.DISABLE
            }
            MotionEventCompat.ACTION_POINTER_UP -> onSecondaryPointerUp(ev)
        }
        return mIsBeingDragged
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (!mIsBeingDragged) {
            return super.onTouchEvent(ev)
        }
        val action: Int = MotionEventCompat.getActionMasked(ev)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mInitialMotionY = ev.y
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0)
                mIsBeingDragged = false
                mDragPercent = 0f
                mStartPoint = mInitialMotionY
                up = canChildScrollUp()
                down = canChildScrollDown()
            }
            MotionEvent.ACTION_MOVE -> {
                val pointerIndex: Int = MotionEventCompat.findPointerIndex(ev, mActivePointerId)
                if (pointerIndex < 0) {
                    return false
                }
                val y: Float = MotionEventCompat.getY(ev, pointerIndex)
                val yDiff = y - mStartPoint
                if (mLastDirection === RefreshMode.PULL_FROM_START && yDiff < 0
                    || mLastDirection === RefreshMode.PULL_FROM_END && yDiff > 0
                ) {
                    return true
                }
                if (!mIsBeingDragged && yDiff > 0 && mLastDirection === RefreshMode.PULL_FROM_START
                    || yDiff < 0 && mLastDirection === RefreshMode.PULL_FROM_END
                ) {
                    mIsBeingDragged = true
                }
                var targetY: Int
                if (mRefreshing || mLoading) {
                    targetY = (mInitialOffsetTop + yDiff).toInt()
                    if (mRefreshing && canChildScrollUp() || mLoading && canChildScrollDown()) {
                        targetY = -1
                        mInitialMotionY = y
                        mInitialOffsetTop = 0
                        if (mDispatchTargetTouchDown) {
                            mTarget!!.dispatchTouchEvent(ev)
                        } else {
                            val obtain = MotionEvent.obtain(ev)
                            obtain.action = MotionEvent.ACTION_DOWN
                            mDispatchTargetTouchDown = true
                            mTarget!!.dispatchTouchEvent(obtain)
                        }
                    } else {
                        if (targetY < 0) {
                            if (mDispatchTargetTouchDown) {
                                mTarget!!.dispatchTouchEvent(ev)
                            } else {
                                val obtain = MotionEvent.obtain(ev)
                                obtain.action = MotionEvent.ACTION_DOWN
                                mDispatchTargetTouchDown = true
                                mTarget!!.dispatchTouchEvent(obtain)
                            }
                            targetY = 0
                        } else if (targetY > mTotalDragDistance) {
                            targetY = mTotalDragDistance
                        } else {
                            if (mDispatchTargetTouchDown) {
                                val obtain = MotionEvent.obtain(ev)
                                obtain.action = MotionEvent.ACTION_CANCEL
                                mDispatchTargetTouchDown = false
                                mTarget!!.dispatchTouchEvent(obtain)
                            }
                        }
                    }
                    setTargetOffsetTop(targetY - mCurrentOffsetTop, true)
                } else {
                    val scrollTop = yDiff * DRAG_RATE
                    val originalDragPercent = scrollTop / mTotalDragDistance
                    mDragPercent = Math.min(1f, Math.abs(originalDragPercent)) //拖动的百分比
                    val extraOS = Math.abs(scrollTop) - mTotalDragDistance //弹簧效果的位移
                    val slingshotDist = finalOffset.toFloat()
                    //当弹簧效果位移小余0时，tensionSlingshotPercent为0，否则取弹簧位移于总高度的比值，最大为2
                    val tensionSlingshotPercent =
                        Math.max(0f, Math.min(extraOS, slingshotDist * 2) / slingshotDist)
                    //对称轴为tensionSlingshotPercent = 2的二次函数，0到2递增
                    val tensionPercent = (tensionSlingshotPercent / 4 - Math.pow(
                        (tensionSlingshotPercent / 4).toDouble(),
                        2.0
                    )).toFloat() * 2f
                    val extraMove = slingshotDist * tensionPercent * 2
                    targetY = (slingshotDist * mDragPercent + extraMove).toInt()
                    if (originalDragPercent < 0) {
                        //上拉加载
                        if (mLoadView.visibility != VISIBLE) {
                            mLoadView.visibility = VISIBLE
                        }
                        if (Math.abs(scrollTop) < mTotalDragDistance) {
                            mLoadDrawable!!.setPercent(mDragPercent)
                        }
                        setTargetOffsetTop(-targetY - mCurrentOffsetTop, true)
                    } else {
                        //下拉刷新
                        targetY = (slingshotDist * mDragPercent + extraMove).toInt()
                        if (mRefreshView.visibility != VISIBLE) {
                            mRefreshView.visibility = VISIBLE
                        }
                        if (scrollTop < mTotalDragDistance) {
                            mRefreshDrawable!!.setPercent(mDragPercent)
                        }
                        setTargetOffsetTop(targetY - mCurrentOffsetTop, true)
                    }
                }
            }
            MotionEventCompat.ACTION_POINTER_DOWN -> {
                val index: Int = MotionEventCompat.getActionIndex(ev)
                mActivePointerId = MotionEventCompat.getPointerId(ev, index)
            }
            MotionEventCompat.ACTION_POINTER_UP -> onSecondaryPointerUp(ev)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (mActivePointerId == INVALID_POINTER) {
                    return false
                }
                if (mRefreshing || mLoading) {
                    if (mDispatchTargetTouchDown) {
                        mTarget!!.dispatchTouchEvent(ev)
                        mDispatchTargetTouchDown = false
                    }
                    return false
                }
                val pointerIndex: Int = MotionEventCompat.findPointerIndex(ev, mActivePointerId)
                val y: Float = MotionEventCompat.getY(ev, pointerIndex)
                val overscrollTop = (y - mInitialMotionY) * DRAG_RATE
                mIsBeingDragged = false
                if (overscrollTop > mTotalDragDistance && mCurrentOffsetTop > mTotalDragDistance) {
                    setRefreshing(true, true)
                    mLastDirection = RefreshMode.PULL_FROM_START
                } else if (Math.abs(overscrollTop) > mTotalDragDistance && mCurrentOffsetTop < -mTotalDragDistance) {
                    setLoading(true)
                    mLastDirection = RefreshMode.PULL_FROM_END
                } else {
                    mRefreshing = false
                    animateOffsetToStartPosition()
                }
                mActivePointerId = INVALID_POINTER
                mLastDirection = RefreshMode.DISABLE
                return false
            }
        }
        return true
    }

    fun setDurations(durationToStartPosition: Int, durationToCorrectPosition: Int) {
        mDurationToStartPosition = durationToStartPosition
        mDurationToCorrectPosition = durationToCorrectPosition
    }

    private fun animateOffsetToStartPosition() {
        mFrom = mCurrentOffsetTop
        mAnimateToStartPosition.reset()
        mAnimateToStartPosition.duration = mDurationToStartPosition.toLong()
        mAnimateToStartPosition.interpolator = mDecelerateInterpolator
        mAnimateToStartPosition.setAnimationListener(mToStartListener)
        mRefreshView.clearAnimation()
        mRefreshView.startAnimation(mAnimateToStartPosition)
    }

    private fun animateOffsetToCorrectPosition() {
        mFrom = mCurrentOffsetTop
        mAnimateToCorrectPosition.reset()
        mAnimateToCorrectPosition.duration = mDurationToCorrectPosition.toLong()
        mAnimateToCorrectPosition.interpolator = mDecelerateInterpolator
        mAnimateToCorrectPosition.setAnimationListener(mRefreshAnimationListener)
        mRefreshView.clearAnimation()
        mRefreshView.startAnimation(mAnimateToCorrectPosition)
    }

    private fun animateLoadOffsetToCorrectPosition() {
        mFrom = mCurrentOffsetTop
        mAnimateLoadToCorrectPosition.reset()
        mAnimateLoadToCorrectPosition.duration = mDurationToCorrectPosition.toLong()
        mAnimateLoadToCorrectPosition.interpolator = mDecelerateInterpolator
        mAnimateLoadToCorrectPosition.setAnimationListener(mRefreshLoadAnimationListener)
        mLoadView.clearAnimation()
        mLoadView.startAnimation(mAnimateLoadToCorrectPosition)
    }

    private val mAnimateToCorrectPosition: Animation = object : Animation() {
        public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            val endTarget: Int = finalOffset
            val targetTop = mFrom + ((endTarget - mFrom) * interpolatedTime).toInt()
            val offset = targetTop - mTarget!!.top
            setTargetOffsetTop(offset, false /* requires update */)
        }
    }
    private val mAnimateLoadToCorrectPosition: Animation = object : Animation() {
        public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            val endTarget: Int = -finalOffset
            val targetTop = mFrom + ((endTarget - mFrom) * interpolatedTime).toInt()
            val offset = targetTop - mTarget!!.top
            setTargetOffsetTop(offset, false /* requires update */)
        }
    }
    private val mAnimateToStartPosition: Animation = object : Animation() {
        public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            moveToStart(interpolatedTime)
        }
    }

    private fun moveToStart(interpolatedTime: Float) {
        val targetTop = mFrom - (mFrom * interpolatedTime).toInt()
        val offset = targetTop - mTarget!!.top
        setTargetOffsetTop(offset, false)
        if (offset > 0) {
            mRefreshDrawable!!.setPercent(mDragPercent * (1 - interpolatedTime))
        } else {
            mLoadDrawable!!.setPercent(mDragPercent * (1 - interpolatedTime))
        }
    }

    fun setRefreshing(refreshing: Boolean) {
        if (mRefreshing != refreshing) {
            setRefreshing(refreshing, false /* notify */)
        }
    }

    private fun setRefreshing(refreshing: Boolean, notify: Boolean) {
        if (mRefreshing != refreshing) {
            mNotify = notify
            ensureTarget()
            mRefreshing = refreshing
            if (mRefreshing) {
                mRefreshDrawable!!.setPercent(1f)
                animateOffsetToCorrectPosition()
            } else {
                mLastDirection = RefreshMode.DISABLE
                animateOffsetToStartPosition()
            }
        }
    }

    fun setLoading(loading: Boolean) {
        if (mLoading != loading) {
            ensureTarget()
            mLoading = loading
            if (mLoading) {
                mLoadDrawable!!.setPercent(1f)
                animateLoadOffsetToCorrectPosition()
            } else {
                mLastDirection = RefreshMode.DISABLE
                animateOffsetToStartPosition()
            }
        }
    }

    private val mRefreshAnimationListener: Animation.AnimationListener =
        object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                mRefreshView.visibility = VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                if (mRefreshing) {
                    mRefreshDrawable!!.start()
                    if (mNotify) {
                        if (mRefreshListener != null) {
                            mRefreshListener!!.onRefresh()
                        }
                    }
                } else {
                    mRefreshDrawable!!.stop()
                    mRefreshView.visibility = GONE
                    animateOffsetToStartPosition()
                }
                mCurrentOffsetTop = mTarget!!.top
                mLastDirection = RefreshMode.DISABLE
            }
        }
    private val mRefreshLoadAnimationListener: Animation.AnimationListener =
        object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                mLoadView.visibility = VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                if (mLoading) {
                    mLoadDrawable!!.start()
                    if (mLoadLisener != null) {
                        mLoadLisener!!.onLoad()
                    }
                } else {
                    mLoadDrawable!!.stop()
                    mLoadView.visibility = GONE
                    animateOffsetToStartPosition()
                }
                mCurrentOffsetTop = mTarget!!.top
                mLastDirection = RefreshMode.DISABLE
            }
        }
    private val mToStartListener: Animation.AnimationListener =
        object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                mRefreshDrawable!!.stop()
                mLoadDrawable!!.stop()
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                mRefreshView.visibility = GONE
                mLoadView.visibility = GONE
                mCurrentOffsetTop = mTarget!!.top
            }
        }

    private fun onSecondaryPointerUp(ev: MotionEvent) {
        val pointerIndex: Int = MotionEventCompat.getActionIndex(ev)
        val pointerId: Int = MotionEventCompat.getPointerId(ev, pointerIndex)
        if (pointerId == mActivePointerId) {
            val newPointerIndex = if (pointerIndex == 0) 1 else 0
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex)
        }
    }

    private fun getMotionEventY(ev: MotionEvent, activePointerId: Int): Float {
        val index: Int = MotionEventCompat.findPointerIndex(ev, activePointerId)
        return if (index < 0) {
            (-1).toFloat()
        } else MotionEventCompat.getY(ev, index)
    }

    private fun setTargetOffsetTop(offset: Int, requiresUpdate: Boolean) {
//        mRefreshView.bringToFront();
        mTarget!!.offsetTopAndBottom(offset)
        mCurrentOffsetTop = mTarget!!.top
        mRefreshDrawable!!.offsetTopAndBottom(offset)
        mLoadDrawable!!.offsetTopAndBottom(offset)
    }

    private fun canChildScrollUp(): Boolean {
        return if (Build.VERSION.SDK_INT < 14) {
            if (mTarget is AbsListView) {
                val absListView = mTarget as AbsListView
                (absListView.childCount > 0
                        && (absListView.firstVisiblePosition > 0 || absListView.getChildAt(0)
                    .top < absListView.paddingTop))
            } else {
                mTarget!!.scrollY > 0
            }
        } else {
            ViewCompat.canScrollVertically(mTarget, -1)
        }
    }

    fun canChildScrollDown(): Boolean {
        return if (Build.VERSION.SDK_INT < 14) {
            if (mTarget is AbsListView) {
                val absListView = mTarget as AbsListView
                val lastChild = absListView.getChildAt(absListView.childCount - 1)
                if (lastChild != null) {
                    (absListView.lastVisiblePosition == absListView.count - 1
                            && lastChild.bottom > absListView.paddingBottom)
                } else {
                    false
                }
            } else {
                mTarget!!.height - mTarget!!.scrollY > 0
            }
        } else {
            ViewCompat.canScrollVertically(mTarget, 1)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        ensureTarget()
        if (mTarget == null) return
        val height = measuredHeight
        val width = measuredWidth
        val left = paddingLeft
        val top = paddingTop
        val right = paddingRight
        val bottom = paddingBottom
        mTarget!!.layout(
            left,
            top + mTarget!!.top,
            left + width - right,
            top + height - bottom + mTarget!!.top
        )
        mRefreshView.layout(left, top, left + width - right, top + height - bottom)
        mLoadView.layout(left, top, left + width - right, top + height - bottom)
    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
            .toInt()
    }

    fun setOnRefreshListener(listener: OnRefreshListener?) {
        mRefreshListener = listener
    }

    fun setOnLoadListener(listener: OnLoadListener?) {
        mLoadLisener = listener
    }

    interface OnLoadListener {
        fun onLoad()
    }

    interface OnRefreshListener {
        fun onRefresh()
    }

    companion object {
        private const val DECELERATE_INTERPOLATION_FACTOR = 2f
        private const val DRAG_MAX_DISTANCE = 64
        private const val INVALID_POINTER = -1
        private const val DRAG_RATE = .5f
    }

    init {
        mDecelerateInterpolator = DecelerateInterpolator(
            DECELERATE_INTERPOLATION_FACTOR
        )
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        val defaultDuration = resources.getInteger(R.integer.config_mediumAnimTime)
        mDurationToStartPosition = defaultDuration
        mDurationToCorrectPosition = defaultDuration
        mTotalDragDistance = dp2px(DRAG_MAX_DISTANCE)
        setRefreshDrawable(SwipDrawable(getContext(), this))
        mRefreshView.visibility = GONE
        addView(mRefreshView, 0)
        setLoadDrawable(SwipLoadDrawable(getContext(), this))
        mLoadView.visibility = GONE
        addView(mLoadView, 0)
        setWillNotDraw(false)
        ViewCompat.setChildrenDrawingOrderEnabled(this, true)
    }
}