package com.didichuxing.doraemonkit.kit.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.didichuxing.doraemonkit.DoKit
import com.didichuxing.doraemonkit.constant.BundleKey
import com.didichuxing.doraemonkit.constant.FragmentIndex
import kotlin.reflect.KClass

/**
 * 悬浮窗和全屏启动器
 */
object SimpleDokitStarter {
    /**
     * @JvmStatic:允许使用java的静态方法的方式调用
     * @JvmOverloads :在有默认参数值的方法中使用@JvmOverloads注解，则Kotlin就会暴露多个重载方法。
     */
    @JvmStatic
    @JvmOverloads
    fun startFloating(
        targetClass: Class<out AbsDokitView?>,
        bundle: Bundle? = null,
        mode: Int = DokitIntent.MODE_SINGLE_INSTANCE
    ) {
        val doKitIntent = DokitIntent(targetClass)
        doKitIntent.mode = mode
        doKitIntent.bundle = bundle
        DokitViewManager.getInstance().attach(doKitIntent)
    }

    @JvmStatic
    fun removeFloating(
        targetClass: Class<out AbsDokitView?>
    ) {
        DokitViewManager.getInstance().detach(targetClass)
    }


    /**
     * @JvmStatic:允许使用java的静态方法的方式调用
     * @JvmOverloads :在有默认参数值的方法中使用@JvmOverloads注解，则Kotlin就会暴露多个重载方法。
     */
    @JvmStatic
    @JvmOverloads
    fun startFullScreen(
        targetClass: Class<out BaseFragment?>,
        context: Context? = null,
        bundle: Bundle? = null,
        isSystemFragment: Boolean = false
    ) {
        val ctx = context ?: DoKit.APPLICATION.applicationContext
        ctx.startActivity(Intent(ctx, UniversalActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (isSystemFragment) {
                putExtra(BundleKey.FRAGMENT_INDEX, FragmentIndex.FRAGMENT_SYSTEM)
                putExtra(BundleKey.SYSTEM_FRAGMENT_CLASS, targetClass)
            } else {
                putExtra(BundleKey.FRAGMENT_INDEX, FragmentIndex.FRAGMENT_CUSTOM)
                putExtra(BundleKey.CUSTOM_FRAGMENT_CLASS, targetClass)
            }
            if (bundle != null) {
                putExtras(bundle)
            }
        })
    }


}