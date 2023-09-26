package lol.kiyarash.weatherapp.network


import lol.kiyarash.weatherapp.data.GeocodingResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL =
    "https://api.openweathermap.org/geo/1.0/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface GeocodingApiService {

    @GET("direct")
    suspend fun getCityCode(
        @Query("q") city: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): List<GeocodingResponse>


//suspend fun getCityCode(@Path(value = "city") city: String): GeocodingResponse

    // https://api.openweathermap.org/geo/1.0/direct?q=Tehran&limit=1&appid=e361b47c26ee00554d31eba0583eebb8
}

object GeocodingApi {
    val retrofitService: GeocodingApiService by lazy {
        retrofit.create(GeocodingApiService::class.java)
    }
}