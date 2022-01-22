package com.jingtian.mtdemo.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.jingtian.mtdemo.BuildConfig
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.base.view.BaseActivity
import com.jingtian.mtdemo.databinding.ActivitySearchBinding
import com.jingtian.mtdemo.databinding.ItemAppsBinding
import com.jingtian.mtdemo.ui.adapters.CommonRvAdapter
import kotlin.concurrent.thread

class SearchActivity : BaseActivity<BaseInterface.Presenter>(), BaseInterface.View {
    class AppsAdapter() : CommonRvAdapter<AppBean, ItemAppsBinding>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CommonRvHolder<ItemAppsBinding> {
            return CommonRvHolder(
                ItemAppsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: CommonRvHolder<ItemAppsBinding>, position: Int) {
            holder.binding.apply {
                data[position].let { item ->
                    tvAppName.text = item.applicationInfo.name
                    ivAppsIcon.background = item.icon
                    tvAppPackageName.text = item.applicationInfo.packageName
                    when (item.type) {
                        AppBean.Type.SYSTEM_APP -> {
                            tvUninstallable.text = "不可卸載"
                            tvUninstallBt.visibility = View.GONE
                        }
                        AppBean.Type.USER_APP -> {
                            tvUninstallable.visibility = View.GONE
                            BaseApplication.utilsHolder.utils.setFont(tvUninstallBt)
                            tvUninstallable.setTextColor(
                                BaseApplication.utilsHolder.utils.getColor(
                                    R.color.orange
                                )
                            )
                        }
                    }
                    tvUninstallBt.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DELETE)
                        intent.data = Uri.parse("package:" + item.applicationInfo.packageName)
                        it.context.startActivity(intent)
                    }
                }

            }
        }
    }

    companion object {
        const val TAG = "search_activity"
    }

    override fun getPresenter(): BaseInterface.Presenter {
        return BasePresenter<SearchActivity>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.llSearchSearchBarRoot.apply {
            setPadding(paddingLeft, paddingTop + getStatusBarHeight(), paddingRight, paddingBottom)
        }

        BaseApplication.utilsHolder.utils.setFont(binding.tvSearchSearchBar)
        imeShowUp()
        binding.rvSearchResult.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResult.adapter = AppsAdapter()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.nestedScrollView3.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                val isBottom = scrollY == binding.rvSearchResult.measuredHeight - binding.nestedScrollView3.measuredHeight
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

    data class AppBean(val applicationInfo: ApplicationInfo, val icon: Drawable, val type: Type) {
        enum class Type {
            SYSTEM_APP,
            USER_APP
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
        val pm = packageManager
        thread {
            val packages = pm.getInstalledApplications(0)
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "${packages.size}")
            }
            var type = AppBean.Type.SYSTEM_APP
            for (applicationInfo in packages) {
                Thread.sleep(500)
                if ((applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "system:${applicationInfo.packageName}")
                    }

                } else {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "user:${applicationInfo.packageName}")
                    }
                    type = AppBean.Type.USER_APP
                }
//                appQueue.add(
//                    AppBean(
//                        applicationInfo,
//                        packageManager.getIcon(applicationInfo),
//                        type
//                    )
//                )
                handler.sendMessage(Message().apply {
                        obj = AppBean(
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

    override fun onResume() {
        super.onResume()

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