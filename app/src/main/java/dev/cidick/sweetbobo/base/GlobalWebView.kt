package dev.cidick.sweetbobo.base

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Window
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.appsflyer.AppsFlyerLib
import com.flyingpigeon.library.ServiceManager
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import dev.cidick.sweetbobo.R
import dev.cidick.sweetbobo.main.AppLaunch
import dev.cidick.sweetbobo.main.MainWeb
import dev.cidick.sweetbobo.main.NoNetworkAct
import dev.cidick.sweetbobo.provider.NetConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

abstract class GlobalWebView : AppCompatActivity(), CoroutineScope by MainScope() {
    private var mAgentWeb: AgentWeb? = null
    private lateinit var apiNetwork: ApiNetwork
    private lateinit var mLinearLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_globalweb)

      mLinearLayout = findViewById(R.id.container)
        val p = System.currentTimeMillis()
        AppsFlyerLib.getInstance().init(AppLaunch.AF_ID, null, this)
        AppsFlyerLib.getInstance().start(this)
        apiNetwork = ApiNetwork(this)

        if (!NetConnection.isConnected(this)) {
            this.startActivity(Intent(this, NoNetworkAct::class.java))
        } else {
            launch {
                apiNetwork.getUrl()
                initializeAgentWeb(mLinearLayout)
            }
        }

        val n = System.currentTimeMillis()
        Log.i("Info", "init used time:" + (n - p))
    }

    private fun initializeAgentWeb(linearLayout: LinearLayout) {
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(linearLayout, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView, title: String) {
                    super.onReceivedTitle(view, title)
                }
            })
            .setWebViewClient(object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    Log.w("url", "URL: " + request.url)
                    return super.shouldOverrideUrlLoading(view, request)
                }
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    Log.i("Info", "BaseWebActivity onPageStarted")
                }
            })
            .setMainFrameErrorView(com.just.agentweb.R.layout.agentweb_error_page, -1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setWebLayout(GlobalWebLayout(this))
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
            .createAgentWeb()
            .ready()
            .go(ApiNetwork.APP_URL)
        Log.w("URLna", ApiNetwork.APP_URL)

        val webSettings: WebSettings = mAgentWeb!!.agentWebSettings?.webSettings ?: return
        webSettings.allowFileAccess = true
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.allowFileAccessFromFileURLs = true

        mAgentWeb!!.let { agentWeb ->
            agentWeb.jsInterfaceHolder.addJavaObject("Android", AndroidInterfaceH5(agentWeb, this))
        }
    }

    open fun getGame(){
        if (!NetConnection.isConnected(this)) {
            this.startActivity(Intent(this, NoNetworkAct::class.java))
        } else {
            mAgentWeb!!.urlLoader.loadUrl(ApiNetwork.APP_URL)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (mAgentWeb?.handleKeyEvent(keyCode, event) == true) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        mAgentWeb!!.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        if (mAgentWeb != null) {
            mAgentWeb!!.webLifeCycle.onResume()
        }
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("Info", "onResult:$requestCode onResult:$resultCode")
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb!!.webLifeCycle?.onDestroy()
    }
}