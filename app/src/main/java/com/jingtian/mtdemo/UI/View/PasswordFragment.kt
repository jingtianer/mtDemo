package com.jingtian.mtdemo.UI.View

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.jingtian.mtdemo.Base.BaseApplication
import com.jingtian.mtdemo.Base.View.BaseFragment
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.UI.Interface.LoginInterface
import com.jingtian.mtdemo.UI.Presenter.LoginPresenter
import com.jingtian.mtdemo.Utils.SetFont




class PasswordFragment:BaseFragment<LoginPresenter>(), LoginInterface.view {
    override fun getPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    var is_password = true
    var agree = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //checkbox图标切换
        val tv_checkbox_login = activity!!.findViewById<TextView>(R.id.tv_checkbox_login)
        val ll_agreement_login = activity!!.findViewById<LinearLayout>(R.id.ll_agreement_login)
        SetFont.setFont(tv_checkbox_login, activity!!)
        ll_agreement_login.setOnClickListener {
            if (agree) {
                tv_checkbox_login.setText(R.string.unchecked)
            } else {
                tv_checkbox_login.setText(R.string.checked)
            }
            agree = !agree
        }

        //切换动画，登录方式切换
        val tv_fgpwd_change = activity!!.findViewById<TextView>(R.id.tv_fgpwd_change)
        val tv_fgpwd_loginhint = activity!!.findViewById<TextView>(R.id.tv_fgpwd_loginhint)
        val ll_pwd = activity!!.findViewById<LinearLayout>(R.id.ll_pwd)
        val fgpwd_divider2 = activity!!.findViewById<View>(R.id.fgpwd_divider2)
        val bt_login = activity!!.findViewById<Button>(R.id.bt_login)
        is_password = true
        tv_fgpwd_change.setOnClickListener {
            if (is_password) {
                val item_in = AnimationUtils.loadAnimation(this.context, R.anim.item_in)
                val item_out = AnimationUtils.loadAnimation(this.context, R.anim.item_out)
                item_out.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {}
                    override fun onAnimationEnd(p0: Animation?) {
                        ll_pwd.visibility = View.GONE
                        fgpwd_divider2.visibility = View.GONE
                    }
                    override fun onAnimationRepeat(p0: Animation?) {}
                })
                item_in.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {
                        tv_fgpwd_loginhint.visibility = View.VISIBLE
                    }
                    override fun onAnimationEnd(p0: Animation?) {}
                    override fun onAnimationRepeat(p0: Animation?) {}
                })
                ll_pwd.startAnimation(item_out)
                fgpwd_divider2.startAnimation(item_out)
                tv_fgpwd_loginhint.startAnimation(item_in)
                tv_fgpwd_change.setText("使用密码")
                bt_login.setText("获取验证码")
            } else {
                val item_in = AnimationUtils.loadAnimation(this.context, R.anim.item_back)
                val item_out = AnimationUtils.loadAnimation(this.context, R.anim.item_exit)
                item_out.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {}
                    override fun onAnimationEnd(p0: Animation?) {
                        tv_fgpwd_loginhint.visibility = View.GONE
                    }
                    override fun onAnimationRepeat(p0: Animation?) {}
                })
                item_in.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {
                        ll_pwd.visibility = View.VISIBLE
                        fgpwd_divider2.visibility = View.VISIBLE
                    }
                    override fun onAnimationEnd(p0: Animation?) {}
                    override fun onAnimationRepeat(p0: Animation?) {}
                })
                ll_pwd.startAnimation(item_in)
                fgpwd_divider2.startAnimation(item_in)
                tv_fgpwd_loginhint.startAnimation(item_out)
                tv_fgpwd_change.setText("使用验证码")
                bt_login.setText("登录")
            }
            is_password = !is_password
        }
        //获取输入框
        val et_phone = activity!!.findViewById<EditText>(R.id.et_phone)
        val et_pwd = activity!!.findViewById<EditText>(R.id.et_pwd)
        et_phone.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        //显示密码按钮
        val tv_show_pd_login = activity!!.findViewById<TextView>(R.id.tv_show_pd_login)
        SetFont.setFont(tv_show_pd_login, activity!!)
        tv_show_pd_login.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                et_pwd.inputType = (InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
            }else if (motionEvent.action == MotionEvent.ACTION_UP) {
                et_pwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            et_pwd.setSelection(et_pwd.text.toString().length)
            true
        }
        //登录按钮功能
        val ll_phone_login = activity!!.findViewById<FrameLayout>(R.id.ll_phone_login)
        val fl_pwd = activity!!.findViewById<FrameLayout>(R.id.fl_pwd)
        bt_login.setOnClickListener {
            val shake = BaseApplication.anims.ShakeAnimation()
            val phone = et_phone.text.toString().trim()
            if (phone.length != 11) {
                ll_phone_login.startAnimation(shake)
                Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            BaseApplication.sp.phone = et_phone.text.toString()
            if (is_password && et_pwd.text.toString().isNullOrBlank()) {
                fl_pwd.startAnimation(shake)
                Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!agree) {
                ll_agreement_login.startAnimation(shake)
                Toast.makeText(context, "请同意我们的政策", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (is_password) {
                val pwd = et_pwd.text.toString()
                mPresenter?.loginByPd(phone, pwd)
            } else {
                verifycode_fragment = VerifycodeFragment.getInstance(this)
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.login_frame, verifycode_fragment!!)
                    .commit()

            }
        }

    }
    var verifycode_fragment:VerifycodeFragment? = null
    override fun getLayout(): Int {
        return R.layout.fragment_password
    }

    override fun login_success() {
        activity!!.finish()
        BaseApplication.sp.login = true
    }

    override fun loginByVc_fail(mes: String) {
        Toast.makeText(context, mes, Toast.LENGTH_SHORT).show()
    }

    override fun loginByPd_fail(mes: String) {
        Toast.makeText(context, mes, Toast.LENGTH_SHORT).show()
    }
}