package com.hyper.hyperweather.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.hyper.hyperweather.R
import com.hyper.hyperweather.data.model.WeatherModel
import com.hyper.hyperweather.databinding.FragmentMainWeatherBinding
import com.squareup.picasso.Picasso
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.util.Calendar
import java.util.Date

class WeatherFragment : Fragment() {

    private var _binding: FragmentMainWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentMainWeatherBinding.inflate(inflater, group, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val weather = requireArguments().getSerializable("WEATHER") as WeatherModel
            setupUI(weather)
            setupListeners()
        } catch (e: Exception) {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupUI(weather: WeatherModel) {

        setupWeatherType(weather.clouds.all)

        binding.run {

            Picasso.get()
                .load("https://countryflagsapi.com/png/${weather.sys.country}")
                .placeholder(R.drawable.ic_flag)
                .error(R.drawable.ic_flag)
                .into(ivCountryFlag)

            tvCityName.text = weather.name
            tvSunsetDesc.text = convertLongToTime(weather.sys.sunset)
            tvSunriseDesc.text = convertLongToTime(weather.sys.sunrise)

            tvWeatherTemp.text = buildString {
                append(weather.main.temp.toInt())
                append("°")
            }

            tvTempDesc.text = buildString {
                append(weather.main.temp_min.toInt())
                append("º")
                append("/")
                append(weather.main.temp_max.toInt())
                append("º")
            }

            tvCloudsDesc.text = buildString {
                append(weather.clouds.all)
                append("%")
            }

            tvHumidityDesc.text = buildString {
                append(weather.main.humidity)
                append("%")
            }
        }
    }

    private fun setupListeners() {
        binding.btnWeatherReturn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupWeatherType(percent: Int) {
        try {
            val image_url = when(percent) {
                in 0..15 -> sun_url
                in 16..50 -> sun_with_light_cloud_url
                in 51..99 -> sun_with_medium_cloud_url
                100 -> cloud_url
                else -> sun_url
            }

            Picasso.get()
                .load(image_url)
                .placeholder(R.drawable.ic_weather)
                .placeholder(R.drawable.ic_weather)
                .into(binding.ivWeatherIcon)

        } catch (e: Exception) {
            showErrorToast()
        }
    }

    private fun showErrorToast() {
        MotionToast.darkToast(
            context = requireActivity(),
            title = requireContext().resources.getString(R.string.error_name),
            message = requireContext().resources.getString(R.string.not_found_error),
            style = MotionToastStyle.ERROR,
            position = MotionToast.GRAVITY_BOTTOM,
            duration = MotionToast.SHORT_DURATION,
            font = ResourcesCompat.getFont(requireContext(), R.font.circular_std))
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time * 1000L)
        val cal = Calendar.getInstance().apply { this.time = date }
        return "${cal.get(Calendar.HOUR_OF_DAY)}h${cal.get(Calendar.MINUTE)}"
    }

    companion object {
        val cloud_url = "https://imgur.com/6NDnFsZ.png"
        val cloud_with_rain_url = "https://imgur.com/lWUkEme.png"
        val sun_url = "https://imgur.com/4byWY8n.png"
        val sun_with_medium_cloud_url = "https://imgur.com/hz9teFS.png"
        val sun_with_light_cloud_url = "https://imgur.com/d9yc4za.png"
    }
}