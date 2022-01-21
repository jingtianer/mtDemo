package com.jingtian.mtdemo.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.view.BaseFragment
import com.jingtian.mtdemo.databinding.FragmentMineBinding
import com.jingtian.mtdemo.databinding.FragmentPasswordBinding
import com.jingtian.mtdemo.ui.interfaces.LoginInterface
import com.jingtian.mtdemo.ui.presenter.LoginPresenter


class PasswordFragment : BaseFragment<LoginPresenter>(), LoginInterface.View {
    override fun getPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    private var isPassword = true
    private var agree = false
    private var phoneTemp = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.let { it ->
            //checkbox图标切换
            BaseApplication.utilsHolder.utils.setFont(binding.tvCheckboxLogin)
            binding.llAgreementLogin.setOnClickListener {
                if (agree) {
                    binding.tvCheckboxLogin.setText(R.string.unchecked)
                } else {
                    binding.tvCheckboxLogin.setText(R.string.checked)
                }
                agree = !agree
            }

            //切换动画，登录方式切换
            isPassword = true
            binding.tvFgpwdChange.setOnClickListener {
                if (isPassword) {
                    val itemIn = AnimationUtils.loadAnimation(this.context, R.anim.item_in)
                    val itemOut = AnimationUtils.loadAnimation(this.context, R.anim.item_out)
                    itemOut.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {}
                        override fun onAnimationEnd(p0: Animation?) {
                            binding.llPwd.visibility = View.GONE
                            binding.fgpwdDivider2.visibility = View.GONE
                        }

                        override fun onAnimationRepeat(p0: Animation?) {}
                    })
                    itemIn.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {
                            binding.tvFgpwdLoginhint.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(p0: Animation?) {}
                        override fun onAnimationRepeat(p0: Animation?) {}
                    })
                    binding.llPwd.startAnimation(itemOut)
                    binding.fgpwdDivider2.startAnimation(itemOut)
                    binding.tvFgpwdLoginhint.startAnimation(itemIn)
                    binding.tvFgpwdChange.text = "使用密码"
                    binding.btLogin.text = "获取验证码"
                } else {
                    val itemIn = AnimationUtils.loadAnimation(this.context, R.anim.item_back)
                    val itemOut = AnimationUtils.loadAnimation(this.context, R.anim.item_exit)
                    itemOut.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {}
                        override fun onAnimationEnd(p0: Animation?) {
                            binding.tvFgpwdLoginhint.visibility = View.GONE
                        }

                        override fun onAnimationRepeat(p0: Animation?) {}
                    })
                    itemIn.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {
                            binding.llPwd.visibility = View.VISIBLE
                            binding.fgpwdDivider2.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(p0: Animation?) {}
                        override fun onAnimationRepeat(p0: Animation?) {}
                    })
                    binding.llPwd.startAnimation(itemIn)
                    binding.fgpwdDivider2.startAnimation(itemIn)
                    binding.tvFgpwdLoginhint.startAnimation(itemOut)
                    binding.tvFgpwdChange.text = "使用验证码"
                    binding.btLogin.text = "登录"
                }
                isPassword = !isPassword
            }
            //获取输入框
            binding.etPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            //显示密码按钮
            BaseApplication.utilsHolder.utils.setFont(binding.tvShowPdLogin)
            binding.tvShowPdLogin.setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    binding.etPwd.inputType = (InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                    binding.etPwd.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                binding.etPwd.setSelection(binding.etPwd.text.toString().length)
                true
            }
            //登录按钮功能
            binding.btLogin.setOnClickListener {
                val shake = BaseApplication.utilsHolder.anims.shakeAnimation()
                val phone = binding.etPhone.text.toString().trim()
                if (phone.length != 11) {
                    binding.llPhoneLogin.startAnimation(shake)
                    Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                phoneTemp =binding. etPhone.text.toString()
                if (isPassword && binding.etPwd.text.toString().isBlank()) {
                    binding.flPwd.startAnimation(shake)
                    Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (!agree) {
                    binding.llAgreementLogin.startAnimation(shake)
                    Toast.makeText(context, "请同意我们的政策", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (isPassword) {
                    val pwd = binding.etPwd.text.toString()
                    mPresenter?.loginByPd(phone, pwd)
                } else {
                    verifyCodeFragment = VerifyCodeFragment.getInstance(this, phoneTemp)
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

    private var verifyCodeFragment: VerifyCodeFragment? = null
    override fun getLayout(): Int {
        return R.layout.fragment_password
    }

    override fun loginSuccess() {
        BaseApplication.utilsHolder.sp.login = true
        BaseApplication.utilsHolder.sp.phone = phoneTemp
        activity?.finish()
    }

    override fun loginByVcFailed(mes: String) {
        Toast.makeText(context, mes, Toast.LENGTH_SHORT).show()
    }

    override fun loginByPdFailed(mes: String) {
        Toast.makeText(context, mes, Toast.LENGTH_SHORT).show()
    }

    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!
    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?,): View? {
        _binding = FragmentPasswordBinding.inflate(inflater, container,false)
        return _binding?.root
    }
    override fun unViewBinding() {
        _binding = null
    }
}