package com.jingtian.mtdemo.UI.View

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jingtian.mtdemo.Base.View.BaseActivity
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.UI.Interface.MainInterface
import com.jingtian.mtdemo.UI.Presenter.MainPresenter

class MainActivity: BaseActivity<MainInterface.presenter>(), MainInterface.view {
    var pager:ViewPager2? = null
    val navi_item_listener = object :BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            pager?.setCurrentItem(item.order, false)
            Log.d("navi_pressed", "${item.order}")
            if (item.order == 3) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            return true
        }

    }
    override fun getPresenter(): MainInterface.presenter {
        return MainPresenter()
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }
    fun setTransparentBars(){
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentBars()
        val navi = findViewById<BottomNavigationView>(R.id.main_btmnavi)
        navi.setOnNavigationItemSelectedListener(navi_item_listener)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}