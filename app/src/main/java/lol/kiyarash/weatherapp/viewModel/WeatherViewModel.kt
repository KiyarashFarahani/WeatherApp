package lol.kiyarash.weatherapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lol.kiyarash.weatherapp.R
import lol.kiyarash.weatherapp.data.WeatherResponse
import lol.kiyarash.weatherapp.network.GeocodingApi
import lol.kiyarash.weatherapp.network.WeatherApi
import java.sql.Time
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import kotlin.math.roundToInt

private const val API_KEY: String = "e361b47c26ee00554d31eba0583eebb8"
private const val UNITS: String = "metric"

class WeatherViewModel : ViewModel() {

    private var _lat: Double? = null
    private val lat: Double get() = _lat!!

    private var _lon: Double? = null
    private val lon: Double get() = _lon!!

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status


    private val _temperature = MutableLiveData<String>()
    val temperature: LiveData<String> = _temperature


    private val _imgSrc = MutableLiveData<Int>()
    val imgSrc: LiveData<Int> = _imgSrc

    private val _progressVisibility = MutableLiveData<Boolean>()
    val progressVisibility: LiveData<Boolean> = _progressVisibility

    private val _timeVisibility = MutableLiveData<Boolean>()
    val timeVisibility: LiveData<Boolean> = _timeVisibility


    private val _backgroundColor = MutableLiveData<Int>()
    val backgroundColor: LiveData<Int> = _backgroundColor


    private val _statusBarColor = MutableLiveData<Int>()
    val statusBarColor: LiveData<Int> = _statusBarColor

    private val _localTime = MutableLiveData<String>()
    val localTime: LiveData<String> = _localTime


    init {
        _progressVisibility.value = false
        _backgroundColor.value = R.drawable.day_background
    }


    private var weatherResponse: WeatherResponse? = null


    fun getWeatherData(city: String) {
        viewModelScope.launch {
            try {
                _imgSrc.value = android.R.color.transparent
                _temperature.value = ""
                _status.value = ""
                _timeVisibility.value = false
                _progressVisibility.value = true
                // Get lat and lon from Geocoding API
                val geocodeResponse = GeocodingApi.retrofitService.getCityCode(city, 1, API_KEY)[0]
                _lat = geocodeResponse.lat
                _lon = geocodeResponse.lon

                // Get Weather Data from Weather API
                weatherResponse = WeatherApi.retrofitService.getWeather(lat, lon, API_KEY, UNITS)
                val isDay = isDayOrNot(weatherResponse!!.sys.sunrise, weatherResponse!!.sys.sunset)
                _localTime.value = getLocalTime(weatherResponse!!)
                _timeVisibility.value = true
                when (isDay) {
                    true -> {
                        _backgroundColor.value = R.drawable.day_background
                        _statusBarColor.value = R.color.blue
                    }

                    false -> {
                        _backgroundColor.value = R.drawable.night_background
                        _statusBarColor.value = R.color.dark_blue
                    }
                }
                _progressVisibility.value = false
                _temperature.value = weatherResponse!!.main.temp.roundToInt().toString() + "°C"
                _status.value = weatherResponse!!.weather[0].main

                val statusId = weatherResponse!!.weather[0].id

                when (statusId / 100) {
                    2 -> {
                        _imgSrc.value = if (isDay)
                            R.drawable.thunderstorm
                        else
                            R.drawable.night_thunderstorm
                    } //thunderstorm
                    3 -> {
                        _imgSrc.value = if (isDay)
                            R.drawable.rainy
                        else
                            R.drawable.night_rainy
                    } //drizzle
                    5 -> {
                        _imgSrc.value = if (isDay)
                            R.drawable.rainy
                        else
                            R.drawable.night_rainy
                    } //rain
                    6 -> {
                        _imgSrc.value = if (isDay)
                            R.drawable.snowy
                        else
                            R.drawable.night_snowy
                    } //snow
                    7 -> {
                        _imgSrc.value = if (isDay)
                            R.drawable.cloudy
                        else
                            R.drawable.night_cloudy
                    } //dust
                    8 -> {
                        if (statusId == 800) {
                            _imgSrc.value = if (isDay)
                                R.drawable.clear
                            else
                                R.drawable.night_clear
                            //clear
                        } else {
                            _imgSrc.value = if (isDay)
                                R.drawable.cloudy
                            else
                                R.drawable.night_cloudy
                            //cloudy
                        }

                    }

                }


            } catch (e: Exception) {
                Log.v("tag1", "Exception :   $e")
                _imgSrc.value = R.drawable.emoji
                _status.value = "Network Disconnected"
                _progressVisibility.value = false
            }


        }


    }

    private fun getLocalTime(weatherResponse: WeatherResponse): String {
        val offset = weatherResponse.timezone
        val localOffset = TimeZone.getDefault().getOffset(System.currentTimeMillis() / 1000) / 1000
        val time = (System.currentTimeMillis() / 1000) - localOffset + offset

        return LocalTime.parse(
            Time(time * 1000).toString(),
            DateTimeFormatter.ofPattern("HH:mm:ss")
        )
            .format(DateTimeFormatter.ofPattern("hh:mm a"))

    }

    fun resetBackground() {
        _backgroundColor.value = R.drawable.day_background
        _statusBarColor.value = R.color.blue

    }

    private fun isDayOrNot(sunrise: Int, sunset: Int): Boolean {
        val time = System.currentTimeMillis() / 1000
        return time > sunrise && time < sunset

    }


}