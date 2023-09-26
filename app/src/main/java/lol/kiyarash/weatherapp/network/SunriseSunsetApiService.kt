package lol.kiyarash.weatherapp.network

import lol.kiyarash.weatherapp.data.SunriseSunsetResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL =
    "https://api.sunrise-sunset.org/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface SunriseSunsetApiService {


    @GET("json")
    suspend fun getLocalTimes(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): SunriseSunsetResponse

    // https://api.sunrise-sunset.org/json?lat=36.7201600&lng=-4.4203400
}


object SunriseSunsetApi {
    val retrofitService: SunriseSunsetApiService by lazy {
        retrofit.create(SunriseSunsetApiService::class.java)
    }
}