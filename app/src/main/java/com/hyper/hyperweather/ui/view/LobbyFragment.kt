package com.hyper.hyperweather.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.hyper.hyperweather.R
import com.hyper.hyperweather.data.model.WeatherModel
import com.hyper.hyperweather.databinding.FragmentMainLobbyBinding
import com.hyper.hyperweather.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.fragment.android.replace
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class LobbyFragment : Fragment() {

    private var _binding: FragmentMainLobbyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModel()

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainLobbyBinding.inflate(inflater, group, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
        setupObservers()
    }

    private fun setupUI() {
        turnOffLoading()
    }

    private fun setupListeners() {
        binding.btnSearchByCityName.setOnClickListener {
            val input = binding.inputTxtCityName

            if(!input.text.toString().isNullOrBlank()) {
                searchWeather(input.text.toString())
                input.error = null
            } else {
                input.error = requireContext().resources.getString(R.string.place_holder_error)
            }
        }
    }

    private fun setupObservers() {
        viewModel.currentWeatherState.observe(viewLifecycleOwner) { state ->
            when(state) {
                MainViewModel.WeatherState.STOPPED -> turnOffLoading()
                MainViewModel.WeatherState.LOADING -> turnOnLoading()
                else -> turnOffLoading()
            }
        }
    }

    private fun turnOnLoading() = binding.piLoading.apply { visibility = View.VISIBLE }
    private fun turnOffLoading() = binding.piLoading.apply { visibility = View.GONE }

    private fun searchWeather(query: String) {
        lifecycleScope.launch {
            val (success, weather) = viewModel.getCityByName(query)

            if (success) {
               weather?.let { openWeather(weather) }
            } else {
                showErrorToast()
            }
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

    private fun openWeather(weather: WeatherModel) {
        requireActivity().supportFragmentManager.commit {
            setCustomAnimations(
                androidx.appcompat.R.anim.abc_fade_in,
                androidx.appcompat.R.anim.abc_fade_out,
                androidx.appcompat.R.anim.abc_fade_in,
                androidx.appcompat.R.anim.abc_fade_out
            )
            replace<WeatherFragment>(R.id.fcv_main, bundleOf("WEATHER" to weather))
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}