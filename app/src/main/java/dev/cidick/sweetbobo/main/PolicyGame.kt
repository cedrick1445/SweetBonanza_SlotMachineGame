package dev.cidick.sweetbobo.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.webkit.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import dev.cidick.sweetbobo.databinding.PolicyGameBinding
import dev.cidick.sweetbobo.provider.NetConnection
import java.lang.reflect.InvocationTargetException
import java.util.*

class PolicyGame : AppCompatActivity() {
    companion object {
        private const val TAG = "Policy"
        private const val LOADURL = "file:///android_asset/NativePolicy.html"
    }

    private lateinit var pol : PolicyGameBinding
    private var mUploadCallBackAboveL: ValueCallback<Array<Uri>>? = null
    private var activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        val data = result.data
        val resultCode = result.resultCode
        if (resultCode == RESULT_OK && data != null) {
            handleFileChooseResult(data)
        } else {
            clearUploadMessage()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        pol = PolicyGameBinding.inflate(layoutInflater)
        setContentView(pol.root)
        if (TextUtils.isEmpty(LOADURL)) {
            finish()
            return
        }else if (!NetConnection.isConnected(this)) {
            this.startActivity(Intent(this, NoNetworkAct::class.java))
        } else setupWebView()
    }

    private fun setupWebView() {
        setWebViewSettings()
        setWebViewClients()
        pol.UserConsent.loadUrl(LOADURL)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebViewSettings() {
        val settings =  pol.UserConsent.getSettings()
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.setSupportMultipleWindows(true)
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.allowContentAccess = true
        settings.databaseEnabled = true
        settings.setGeolocationEnabled(true)
        settings.useWideViewPort = true
        settings.userAgentString = settings.userAgentString.replace("; wv", "")
        settings.mediaPlaybackRequiresUserGesture = false
        settings.setSupportZoom(false)
        enableUniversalAccessFromFileURLs(settings)
    }

    private fun enableUniversalAccessFromFileURLs(settings: WebSettings) {
        try {
            val clazz: Class<*> = settings.javaClass
            val method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", Boolean::class.javaPrimitiveType)
            method.invoke(settings, true)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    private fun setWebViewClients() {
        pol.UserConsent.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d(TAG, "onPageStarted: $url");
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                Log.d(TAG, "onPageFinished: $url");
            }
        })
        pol.UserConsent.addJavascriptInterface(JsInterface(), "jsBridge")
        pol.UserConsent.setWebChromeClient(object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                mUploadCallBackAboveL = filePathCallback
                openFileChooseProcess()
                return true
            }
        })
    }

    private fun injectWgPackage() {
        val wgPackage = ("javascript:window.WgPackage = {name:'" + packageName + "', version:'"
                + appVersionName + "'}")
        pol.UserConsent.evaluateJavascript(wgPackage) { _: String? -> }
    }

    private val appVersionName: String
        private get() {
            var appVersionName = ""
            try {
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                appVersionName = packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e(TAG, Objects.requireNonNull(e.message).toString())
            }
            return appVersionName
        }

    private fun openFileChooseProcess() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("image/*")
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private fun handleFileChooseResult(data: Intent) {
        val result = data.data
        if (result != null && mUploadCallBackAboveL != null) {
            mUploadCallBackAboveL!!.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(RESULT_OK, data))
            mUploadCallBackAboveL = null
        } else {
            clearUploadMessage()
        }
    }

    private fun clearUploadMessage() {
        if (mUploadCallBackAboveL != null) {
            mUploadCallBackAboveL!!.onReceiveValue(null)
            mUploadCallBackAboveL = null
        }
    }

    inner class JsInterface {
        private val context: Context

        // Parameterized constructor
        internal constructor(context: Context) {
            this.context = context
        }

        // Default constructor
        internal constructor() {
            // Initialize the context if needed
            context = this@PolicyGame
        }

        @JavascriptInterface
        fun postMessage(name: String, data: String) {
            Log.e(TAG, "name = $name    data = $data")
            if (!TextUtils.isEmpty(name) && "CloseButtonClicked" == name && context is Activity) {
                context.finish()
            }
        }
    }
}
