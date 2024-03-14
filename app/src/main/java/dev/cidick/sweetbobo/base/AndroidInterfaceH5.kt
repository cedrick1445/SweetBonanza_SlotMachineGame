package dev.cidick.sweetbobo.base

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.appsflyer.AppsFlyerLib
import com.just.agentweb.AgentWeb
import dev.cidick.sweetbobo.main.AppLaunch
import org.json.JSONException
import org.json.JSONObject

class AndroidInterfaceH5(private val agent: AgentWeb, private val context: Context) {
    private val deliver = Handler(Looper.getMainLooper())
    @JavascriptInterface
    fun openWebView(url: String) {
        Log.d(AppLaunch.TAG, "call: window.Android.openWebView : $url")
        AppsFlyerLib.getInstance().logEvent(context, "openWebView", null)
    }

    @JavascriptInterface
    fun eventTracker(eventType: String, eventValues: String) {
        Log.d(AppLaunch.TAG, "call: window.Android.eventTracker")
        Log.i(AppLaunch.TAG, "Event: " + eventType + " / Values: " + bundleFromJson(eventValues))
        AppsFlyerLib.getInstance().logEvent(context, eventType, bundleFromJson(eventValues))
    }

    @JavascriptInterface
    fun callAndroid(msg: String?) {
        deliver.post {
            Log.i("Info", "main Thread:" + Thread.currentThread())
            Toast.makeText(context.applicationContext, msg, Toast.LENGTH_LONG).show()
        }
        Log.i("Info", "Thread:" + Thread.currentThread())
    }

    private fun bundleFromJson(json: String): Map<String, Any> {
        val result: MutableMap<String, Any> = HashMap()
        try {
            val jsonObject = JSONObject(json)
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = jsonObject[key]
                if (value is String) {
                    result[key] = value
                } else if (value is Int) {
                    result[key] = value
                } else if (value is Double) {
                    result[key] = value
                } else {
                    Log.w(AppLaunch.TAG, "Value for key $key not one of [String, Integer, Double]")
                }
            }
        } catch (e: JSONException) {
            Log.w(AppLaunch.TAG, "Failed to parse JSON, returning empty Bundle.", e)
            return HashMap()
        }
        return result
    }
}
