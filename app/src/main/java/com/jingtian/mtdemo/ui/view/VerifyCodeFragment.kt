package com.jingtian.mtdemo.ui.view

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.view.BaseFragment
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.ui.interfaces.LoginInterface
import com.jingtian.mtdemo.ui.presenter.LoginPresenter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class VerifyCodeFragment(private val passwordFragment: PasswordFragment):BaseFragment<LoginInterface.Presenter>(), LoginInterface.View {
    companion object {
        var verifyCodeFragment:VerifyCodeFragment? = null
        fun getInstance(passwordFragment: PasswordFragment):VerifyCodeFragment {
            if (verifyCodeFragment==null) {
                verifyCodeFragment = VerifyCodeFragment(passwordFragment)
            }
            VerifyCodeFragment.tempPhone = tempPhone
            return verifyCodeFragment!!
        }
    }

    private val codes = arrayListOf<TextView>()
    private var count = 10
    private var timer:Timer? = null
    inner class CountTask(private val activity: Activity?, private val timer: Timer, val textView: TextView): TimerTask() {
        override fun run() {
            activity?.runOnUiThread {
                if (count > 0) {
                    count--
                    textView.text = activity.resources.getString(R.string.tvDelay,count)
                }else {
                    timer.cancel()
                    textView.text = "再次发送"
                }
            }
        }
    }
    private fun startCount(textView: TextView) {
        timer?.cancel()
        count = 10
        timer = Timer()
        timer!!.scheduleAtFixedRate(CountTask(activity, timer!!,textView), 0 ,1000)
    }
    var cur=""
    inner class VerifyCodeWatcher(private val editText: EditText, private val list: ArrayList<TextView>):TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if(p0 == null || p0.isEmpty()) return
            val text = p0.toString() + ""
            if (text.isNotEmpty()) {
                cur += text
                cur = cur.substring(0, min(4, cur.length))
                Log.d("watcher", cur)
                update()
                editText.setText("")
            }
            if (cur.length == 4) {
                mPresenter?.loginByVc(BaseApplication.sp.phone, cur)
            }
        }
        override fun afterTextChanged(p0: Editable?) {}
        private fun update() {
            var i = 0
            while (i < cur.length) {
                list[i].text = "${cur[i]}"
                //用settext会导致原生os不能正常显示
                i++
            }
        }

    }
    inner class KeyListener(private val list: ArrayList<TextView>):View.OnKeyListener {
        override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
            if ((p1 == KeyEvent.KEYCODE_DEL) && (p2!!.action == KeyEvent.ACTION_DOWN)) {
                val i = cur.length - 1
                if (i >= 0) {
                    list[i].text = ""
                    cur = cur.substring(0, i)
                }
            }
            return false
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        view.let {
            val tvCurPhone = it.findViewById<TextView>(R.id.tv_curphone)
            val etCode1 = it.findViewById<TextView>(R.id.et_code1)
            val etCode2 = it.findViewById<TextView>(R.id.et_code2)
            val etCode3 = it.findViewById<TextView>(R.id.et_code3)
            val etCode4 = it.findViewById<TextView>(R.id.et_code4)
            val tvChangePhone = it.findViewById<TextView>(R.id.tv_change_phone)
            val tvSendCode = it.findViewById<TextView>(R.id.tv_send_code)
            val etRealText = it.findViewById<EditText>(R.id.et_real_text)
            startCount(tvSendCode)
            codes.add(etCode1)
            codes.add(etCode2)
            codes.add(etCode3)
            codes.add(etCode4)
            etRealText.addTextChangedListener(VerifyCodeWatcher(etRealText, codes))
            etRealText.setOnKeyListener(KeyListener(codes))

            tvCurPhone.text = VerifyCodeFragment.tempPhone
            tvChangePhone.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.login_frame, passwordFragment)?.commit()
            }
            tvSendCode.setOnClickListener {
                if (count == 0) {
                    Toast.makeText(context, "验证码已发送", Toast.LENGTH_SHORT).show()
                    startCount(tvSendCode)
                } else {
                    Toast.makeText(context, "请稍后再试", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        verifyCodeFragment = null
    }
    override fun getPresenter(): LoginInterface.Presenter {
        return LoginPresenter()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_verifycode
    }

    override fun loginSuccess() {
        BaseApplication.sp.login = true
        BaseApplication.sp.phone = tempPhone
        activity?.finish()
    }

    override fun loginByVcFailed(mes: String) {
        Toast.makeText(activity, mes, Toast.LENGTH_SHORT).show()
    }

    override fun loginByPdFailed(mes: String) {
        Toast.makeText(activity, mes, Toast.LENGTH_SHORT).show()
    }
}