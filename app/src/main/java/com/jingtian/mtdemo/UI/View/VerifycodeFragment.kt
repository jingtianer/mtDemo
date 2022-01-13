package com.jingtian.mtdemo.UI.View

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
import com.jingtian.mtdemo.Base.BaseApplication
import com.jingtian.mtdemo.Base.View.BaseFragment
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.UI.Interface.LoginInterface
import com.jingtian.mtdemo.UI.Presenter.LoginPresenter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class VerifycodeFragment(val passwordFragment: PasswordFragment):BaseFragment<LoginInterface.presenter>(), LoginInterface.view {
    companion object {
        var verifycodeFragment:VerifycodeFragment? = null
        fun getInstance(passwordFragment: PasswordFragment):VerifycodeFragment {
            if (verifycodeFragment==null) {
                verifycodeFragment = VerifycodeFragment(passwordFragment)
            }
            return verifycodeFragment!!
        }
    }

    val codes = arrayListOf<TextView>()
    var count = 10
    var timer:Timer? = null
    inner class countTask(val activity: Activity, val timer: Timer, val textView: TextView): TimerTask() {
        override fun run() {
            activity.runOnUiThread {
                if (count > 0) {
                    count--
                    textView.text = "${count}s"
                }else {
                    timer.cancel()
                    textView.text = "再次发送"
                }
            }
        }
    }
    fun start_count(textView: TextView) {
        timer?.cancel()
        count = 10
        timer = Timer()
        timer!!.scheduleAtFixedRate(countTask(activity!!, timer!!,textView), 0 ,1000)
    }
    var cur=""
    inner class text_wacher(val editText: EditText,val list: ArrayList<TextView>):TextWatcher {

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
        fun update() {

            var i = 0
            while (i < cur.length) {
                list[i].setText("${cur[i]}")
                i++
            }
        }

    }
    inner class keyListener(val list: ArrayList<TextView>):View.OnKeyListener {
        override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
            if ((p1 == KeyEvent.KEYCODE_DEL) && (p2!!.action == KeyEvent.ACTION_DOWN)) {
                val i = cur.length - 1
                list[i].text = ""
                cur = cur.substring(0, cur.length-1)
            }
            return true
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val tv_curphone = activity!!.findViewById<TextView>(R.id.tv_curphone)
        val et_code1 = activity!!.findViewById<TextView>(R.id.et_code1)
        val et_code2 = activity!!.findViewById<TextView>(R.id.et_code2)
        val et_code3 = activity!!.findViewById<TextView>(R.id.et_code3)
        val et_code4 = activity!!.findViewById<TextView>(R.id.et_code4)
        val tv_change_phone = activity!!.findViewById<TextView>(R.id.tv_change_phone)
        val tv_send_code = activity!!.findViewById<TextView>(R.id.tv_send_code)
        val et_real_text = activity!!.findViewById<EditText>(R.id.et_real_text)
        start_count(tv_send_code)
        codes.add(et_code1)
        codes.add(et_code2)
        codes.add(et_code3)
        codes.add(et_code4)
        et_real_text.addTextChangedListener(text_wacher(et_real_text, codes))
        et_real_text.setOnKeyListener(keyListener(codes))

        tv_curphone.text = BaseApplication.sp.phone
        tv_change_phone.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.login_frame, passwordFragment)
                .commit()
        }
        tv_send_code.setOnClickListener {
            if (count == 0) {
                Toast.makeText(context, "验证码已发送", Toast.LENGTH_SHORT).show()
                start_count(tv_send_code)
            } else {
                Toast.makeText(context, "请稍后再试", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        verifycodeFragment = null
    }
    override fun getPresenter(): LoginInterface.presenter {
        return LoginPresenter()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_verifycode
    }

    override fun login_success() {
        Toast.makeText(activity!!, "登录成功", Toast.LENGTH_SHORT).show()
    }

    override fun loginByVc_fail(mes: String) {
        Toast.makeText(activity!!, mes, Toast.LENGTH_SHORT).show()
    }

    override fun loginByPd_fail(mes: String) {
        Toast.makeText(activity!!, mes, Toast.LENGTH_SHORT).show()
    }
}