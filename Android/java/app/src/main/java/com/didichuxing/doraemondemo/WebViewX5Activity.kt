package com.didichuxing.doraemondemo

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.tencent.smtt.export.external.interfaces.ConsoleMessage
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

/**
 * Created by wanglikun on 2018/11/13.
 */
class WebViewX5Activity : AppCompatActivity() {
    val TAG = "WebViewActivity"
//    val url = "file:///android_asset/dokit_index.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_x5_webview)
        val webView = findViewById<WebView>(R.id.x5_web_view)
        initWebView(webView)
//        webView.loadUrl("https://page-daily.kuaidadi.com/m/ddPage_0sTyVhyq.html")
//        WebViewHook.inject(webView)
        //webView.loadUrl(url)
//        webView.loadUrl("file:///android_asset/dokit_index.html")
        webView.loadUrl("https://www.dokit.cn")
//        webView.loadUrl("http://xingyun.xiaojukeji.com/docs/dokit/#/intro")
    }


    @SuppressLint("JavascriptInterface")
    private fun initWebView(webView: WebView) {
        val webSettings: WebSettings = webView.settings
        webSettings.pluginState = WebSettings.PluginState.ON
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = false
        webSettings.loadsImagesAutomatically = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = false
        webSettings.defaultTextEncodingName = "UTF-8"
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.javaScriptCanOpenWindowsAutomatically = false
        webSettings.setAllowFileAccessFromFileURLs(true)
        webSettings.setAllowUniversalAccessFromFileURLs(true)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.removeJavascriptInterface("searchBoxJavaBridge_")
            webView.removeJavascriptInterface("accessibilityTraversal")
            webView.removeJavascriptInterface("accessibility")
        }


        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }

        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                //LogHelper.i(TAG, "consoleMessage===>${consoleMessage?.message()}")
                return super.onConsoleMessage(consoleMessage)
            }
        }
    }


}