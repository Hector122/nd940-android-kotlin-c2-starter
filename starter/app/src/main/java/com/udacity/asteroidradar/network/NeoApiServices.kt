package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * A retrofit service to fetch a NASA neo data.
 */
interface NeoService {
    @GET("planetary/apod")
    fun getImageOfTheDayAsync(@Query("api_key") apiKey: String): Deferred<PictureOfDay>
    
    @GET("neo/rest/v1/feed")
    fun getNeoFeedAsync(@Query("start_date") startDate: String,
                        @Query("end_date") endDate: String,
                        @Query("api_key") apiKey: String): Deferred<String>
}

//Interceptor
private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}

private val client: OkHttpClient = OkHttpClient.Builder().apply {
    this.addInterceptor(interceptor)
    this.connectTimeout(30, TimeUnit.SECONDS); // connect timeout
    this.readTimeout(30, TimeUnit.SECONDS);    // socket timeout
}.build()


//Build the Moshi object that Retrofit will be using
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .client(client)
    .build()

//Singleton object to export
object NeoApi {
    val retrofitService: NeoService by lazy {
        retrofit.create(NeoService::class.java)
    }
}