package com.jingtian.mtdemo.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.view.BaseFragment
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.ui.interfaces.LoginInterface
import com.jingtian.mtdemo.ui.presenter.LoginPresenter
import com.jingtian.mtdemo.utils.SetFont




class PasswordFragment:BaseFragment<LoginPresenter>(), LoginInterface.View {
    override fun getPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    private var isPassword = true
    private var agree = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { it ->
            //checkbox图标切换
            val tvCheckboxLogin = it.findViewById<TextView>(R.id.tv_checkbox_login)
            val llAgreementLogin = it.findViewById<LinearLayout>(R.id.ll_agreement_login)
            SetFont.setFont(tvCheckboxLogin, it)
            llAgreementLogin.setOnClickListener {
                if (agree) {
                    tvCheckboxLogin.setText(R.string.unchecked)
                } else {
                    tvCheckboxLogin.setText(R.string.checked)
                }
                agree = !agree
            }

            //切换动画，登录方式切换
            val tvPwdChange = it.findViewById<TextView>(R.id.tv_fgpwd_change)
            val tvPwdLoginHint = it.findViewById<TextView>(R.id.tv_fgpwd_loginhint)
            val llPwd = it.findViewById<LinearLayout>(R.id.ll_pwd)
            val pwdDivider2 = it.findViewById<View>(R.id.fgpwd_divider2)
            val btLogin = it.findViewById<Button>(R.id.bt_login)
            isPassword = true
            tvPwdChange.setOnClickListener {
                if (isPassword) {
                    val itemIn = AnimationUtils.loadAnimation(this.context, R.anim.item_in)
                    val itemOut = AnimationUtils.loadAnimation(this.context, R.anim.item_out)
                    itemOut.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {}
                        override fun onAnimationEnd(p0: Animation?) {
                            llPwd.visibility = View.GONE
                            pwdDivider2.visibility = View.GONE
                        }
                        override fun onAnimationRepeat(p0: Animation?) {}
                    })
                    itemIn.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {
                            tvPwdLoginHint.visibility = View.VISIBLE
                        }
                        override fun onAnimationEnd(p0: Animation?) {}
                        override fun onAnimationRepeat(p0: Animation?) {}
                    })
                    llPwd.startAnimation(itemOut)
                    pwdDivider2.startAnimation(itemOut)
                    tvPwdLoginHint.startAnimation(itemIn)
                    tvPwdChange.text = "使用密码"
                    btLogin.text = "获取验证码"
                } else {
                    val itemIn = AnimationUtils.loadAnimation(this.context, R.anim.item_back)
                    val itemOut = AnimationUtils.loadAnimation(this.context, R.anim.item_exit)
                    itemOut.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {}
                        override fun onAnimationEnd(p0: Animation?) {
                            tvPwdLoginHint.visibility = View.GONE
                        }
                        override fun onAnimationRepeat(p0: Animation?) {}
                    })
                    itemIn.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {
                            llPwd.visibility = View.VISIBLE
                            pwdDivider2.visibility = View.VISIBLE
                        }
                        override fun onAnimationEnd(p0: Animation?) {}
                        override fun onAnimationRepeat(p0: Animation?) {}
                    })
                    llPwd.startAnimation(itemIn)
                    pwdDivider2.startAnimation(itemIn)
                    tvPwdLoginHint.startAnimation(itemOut)
                    tvPwdChange.text = "使用验证码"
                    btLogin.text = "登录"
                }
                isPassword = !isPassword
            }
            //获取输入框
            val etPhone = it.findViewById<EditText>(R.id.et_phone)
            val etPwd = it.findViewById<EditText>(R.id.et_pwd)
            etPhone.addTextChangedListener(object :TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            //显示密码按钮
            val tvShowPdLogin = it.findViewById<TextView>(R.id.tv_show_pd_login)
            SetFont.setFont(tvShowPdLogin, it)
            tvShowPdLogin.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    etPwd.inputType = (InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                }else if (motionEvent.action == MotionEvent.ACTION_UP) {
                    etPwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                etPwd.setSelection(etPwd.text.toString().length)
                true
            }
            //登录按钮功能
            val llPhoneLogin = it.findViewById<FrameLayout>(R.id.ll_phone_login)
            val flPwd = it.findViewById<FrameLayout>(R.id.fl_pwd)
            btLogin.setOnClickListener {
                val shake = BaseApplication.anims.ShakeAnimation()
                val phone = etPhone.text.toString().trim()
                if (phone.length != 11) {
                    llPhoneLogin.startAnimation(shake)
                    Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                // TODO: 还没登录成功，怎么就开始记录输入的信息了？
                BaseApplication.sp.phone = etPhone.text.toString()
                if (isPassword && etPwd.text.toString().isBlank()) {
                    flPwd.startAnimation(shake)
                    Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (!agree) {
                    llAgreementLogin.startAnimation(shake)
                    Toast.makeText(context, "请同意我们的政策", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (isPassword) {
                    val pwd = etPwd.text.toString()
                    mPresenter?.loginByPd(phone, pwd)
                } else {
                    verifyCodeFragment = VerifyCodeFragment.getInstance(this)
                    verifyCodeFragment?.let {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.login_frame, it)?.commit()
                    }
//                activity?.let {
//                    it.supportFragmentManager.beginTransaction()
//                    .replace(R.id.login_frame, verifyCodeFragment!!)
//                    .commit()
//                } 

                }
            }
        }


    }
    private var verifyCodeFragment:VerifyCodeFragment? = null
    override fun getLayout(): Int {
        return R.layout.fragment_password
    }

    override fun loginSuccess() {
        activity?.finish()
        BaseApplication.sp.login = true
    }

    override fun loginByVcFailed(mes: String) {
        Toast.makeText(context, mes, Toast.LENGTH_SHORT).show()
    }

    override fun loginByPdFailed(mes: String) {
        Toast.makeText(context, mes, Toast.LENGTH_SHORT).show()
    }
}