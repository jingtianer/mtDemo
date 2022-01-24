package com.jingtian.mtdemo.ui.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.jingtian.mtdemo.BuildConfig
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.base.view.BaseActivity
import com.jingtian.mtdemo.databinding.ActivitySearchBinding
import com.jingtian.mtdemo.ui.adapters.AppsAdapter
import kotlin.concurrent.thread

class SearchActivity : BaseActivity<BaseInterface.Presenter>(), BaseInterface.View {
    interface UninstallButton {
        fun clickForResult(packageName: String)
    }


    companion object {
        const val TAG = "search_activity"
    }

    override fun getPresenter(): BaseInterface.Presenter {
        return BasePresenter<SearchActivity>()
    }

    private val mStartActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val adapter =
                binding.rvSearchResult.adapter as AppsAdapter
            if (it != null && it.resultCode == Activity.RESULT_OK) {
                adapter.deletionResult(true)
            } else {
                adapter.deletionResult(false)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.llSearchSearchBarRoot.apply {
            setPadding(paddingLeft, paddingTop + getStatusBarHeight(), paddingRight, paddingBottom)
        }

        BaseApplication.utilsHolder.utils.setFont(binding.tvSearchSearchBar)
        imeShowUp()
        binding.rvSearchResult.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResult.adapter = AppsAdapter(object : UninstallButton {
            override fun clickForResult(packageName: String) {
                val intent = Intent(Intent.ACTION_DELETE)
                intent.data = Uri.parse("package:$packageName")
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
                mStartActivity.launch(intent)
            }

        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.nestedScrollView3.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                val isBottom =
                    scrollY == binding.rvSearchResult.measuredHeight - binding.nestedScrollView3.measuredHeight
                //            加入前用戶已經滑動到底部
                if (isBottom && !loadCompleted) {
                    Toast.makeText(v.context, "已經在用力加載了啦", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
//            binding.nestedScrollView3.setOnDragListener { view, dragEvent ->
//
//            }
        }

        readApps()
    }

    data class AppBean(
        val label: String,
        val applicationInfo: ApplicationInfo,
        val icon: Drawable,
        val type: Type
    ) {
        enum class Type {
            SYSTEM_APP,
            USER_APP,
            OTHER_APP
        }
    }

    private fun PackageManager.getIcon(applicationInfo: ApplicationInfo): Drawable {
        return applicationInfo.loadIcon(this)
    }

    //    private fun getPackageInfo(packageName:String):PackageInfo? {
//        return try {
//            packageManager.getPackageInfo(packageName, PackageManager.GET_GIDS)
//        } catch (e:PackageManager.NameNotFoundException) {
//            null
//        }
//    }
//    private fun readAppWithAdb():ArrayList<AppBean> {
//        val data0 = arrayListOf<String>()
//        try {
//            val process = Runtime.getRuntime().exec("pm list package")
//            val bis = BufferedReader(InputStreamReader(process.inputStream))
//            var line:String? = bis.readLine()
//            while (line != null) {
//                data0.add(line)
//                line = bis.readLine()
//            }
//        } catch (e:IOException) {
//            e.printStackTrace()
//        }
//        for (item in data0) {
//            val packageInfo = getPackageInfo(item)
//            if (packageInfo != null) {
//                if ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
//                    if (BuildConfig.DEBUG) {
//                        Log.d(TAG, "system:${packageInfo.packageName}")
//                    }
//                    systemApps.add(AppBean(packageInfo.applicationInfo, packageManager.getIcon(packageInfo.applicationInfo), AppBean.Type.SYSTEM_APP))
//                } else {
//                    if (BuildConfig.DEBUG) {
//                        Log.d(TAG, "user:${packageInfo.packageName}")
//                    }
//                    userApps.add(AppBean(packageInfo.applicationInfo, packageManager.getIcon(packageInfo.applicationInfo), AppBean.Type.USER_APP))
//                }
//            }
//        }
//        val data = arrayListOf<AppBean>()
//        data.addAll(userApps)
//        data.addAll(systemApps)
//        return data
//    }
    private val handler = Handler(Looper.myLooper()!!) { msg ->
        (binding.rvSearchResult.adapter as AppsAdapter).let {
//            val isBottom = binding.nestedScrollView3.scrollY == binding.rvSearchResult.measuredHeight - binding.nestedScrollView3.measuredHeight
            //加入前用戶已經滑動到底部
            it.insertItem(msg.obj as AppBean)
//            if (isBottom) {
//                binding.nestedScrollView3.scrollY = binding.rvSearchResult.measuredHeight - binding.nestedScrollView3.measuredHeight
//            }
        }
        true
    }
    var loadCompleted = false

    @SuppressLint("QueryPermissionsNeeded")
    private fun readApps() {
        thread {
            val packages = packageManager.getInstalledApplications(0)
            val userApps = mutableListOf<AppBean>()
            val systemApps = mutableListOf<AppBean>()
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "${packages.size}")
            }
            var log = "other"
            var type = AppBean.Type.OTHER_APP
            for (applicationInfo in packages) {
                Thread.sleep(300)
                if ((applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
                    log = "system"
                    type = AppBean.Type.SYSTEM_APP
                    systemApps.add(
                        AppBean(
                            applicationInfo.loadLabel(packageManager).toString(),
                            applicationInfo,
                            packageManager.getIcon(applicationInfo),
                            type
                        )
                    )

                } else if ((applicationInfo.flags and ApplicationInfo.FLAG_INSTALLED) != 0) {
                    type = AppBean.Type.USER_APP
                    log = "user"
                }

                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "$log:${applicationInfo.packageName}")
                }
                handler.sendMessage(Message().apply {
                    obj = AppBean(
                        applicationInfo.loadLabel(packageManager).toString(),
                        applicationInfo,
                        packageManager.getIcon(applicationInfo),
                        type
                    )
                })
            }
            loadCompleted = true
        }

    }

    private fun imeShowUp() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        binding.etSearchSearchBar.postDelayed({
            runOnUiThread {
                binding.etSearchSearchBar.apply {
                    isFocusable = true
                    isFocusableInTouchMode = true
                    requestFocus()
                    findFocus()
                }
                val inputManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(
                    binding.etSearchSearchBar,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }
        }, 300)
    }


    override fun getLayout(): Int {
        return R.layout.activity_search
    }

    private lateinit var binding: ActivitySearchBinding
    override fun viewBinding(): View {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        return binding.root
    }
}