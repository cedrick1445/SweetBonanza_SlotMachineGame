package dev.cidick.sweetbobo.base

import android.content.Context
import android.content.Intent
import android.util.Log
import dev.cidick.sweetbobo.main.MainGame
import dev.cidick.sweetbobo.main.NoNetworkAct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
class ApiNetwork(private val context: Context) {
    companion object {
        const val APP_ID = "TC"
        const val method = "AES/CBC/PKCS5Padding"
        const val iV = "fedcba9876543210"
        lateinit var myIP: String
        var APP_URL = ""
        var gameKey = ""
    }

    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val apiService: ApiService = Retrofit.Builder()
        .baseUrl("https://sweetbonanza.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(ApiService::class.java)

    suspend fun getUrl() {
        myIP = getClientIP()

        try {
            val response = apiService.getGameData(
                APP_ID,
                "dev.cidick.sweetbobo",
                myIP
            )

            if (response.isSuccessful) {
                val responseBody = response.body()!!.string()
                Log.d("API Response", responseBody)
                responseBody.let {
                    // Log the response for debugging
                    val jsonData = JSONObject(responseBody)
                    Log.d("API ResponseJ", jsonData.toString())
                    // Check if the "data" key exists in the JSON response
                    if (jsonData.has("data")) {
                        val encryptedData = jsonData.getString("data")
                        val decryptedData = decrypt(encryptedData, "21913618CE86B5D53C7B84A75B3774CD")
                        val gameData = JSONObject(decryptedData)
                        Log.d("Decrypted Data", gameData.toString()) // Log the decrypted data for debugging
                        APP_URL = gameData.optString("gameURL", "")
                        gameKey = gameData.optString("gameKey", "")
                        Log.d("DataGame: ", APP_URL + gameKey)
//                        if (gameKey.toBoolean()) {
//                            // Load URL
//                        } else {
//                            navigateToHomepage()
//                        }
                    } else {
                        Log.e("Error", "Missing 'data' key in JSON response")
                        // Handle missing 'data' key in JSON response
                    }
                }
            } else {
                context.startActivity(Intent(context, NoNetworkAct::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle network request failure
            navigateToHomepage()
        }
    }
    private suspend fun getClientIP(): String {
        return withContext(Dispatchers.IO) {
            val url = URL("https://api.ipify.org")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            connection.disconnect()
            response.toString()
        }
    }

    @Throws(Exception::class)
    fun decrypt(message: String?, key: String): String {
        message ?: throw IllegalArgumentException("Message cannot be null")
        val secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance(method)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(iV.toByteArray()))
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(message))
        val trimmedBytes = decryptedBytes.copyOfRange(16, decryptedBytes.size)
        return String(trimmedBytes, StandardCharsets.UTF_8)
    }

    private fun navigateToHomepage() {
        context.startActivity(Intent(context, MainGame::class.java))
    }
}