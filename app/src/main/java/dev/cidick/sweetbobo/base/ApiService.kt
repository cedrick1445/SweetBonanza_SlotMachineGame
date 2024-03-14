package dev.cidick.sweetbobo.base

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @GET("gameid")
    @Headers(
        "X-RapidAPI-Key: f674cefdefmsh718a916a57f2012p18cc20jsn5df43fe8bec0",
        "X-RapidAPI-Host: sweetbonanza.p.rapidapi.com"
    )
    suspend fun getGameData(
        @Query("appid") appid: String,
        @Query("package") packageName: String,
        @Query("clientip") client: String
    ): Response<ResponseBody>
}