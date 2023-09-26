package lol.kiyarash.weatherapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import lol.kiyarash.weatherapp.databinding.FragmentWeatherBinding
import lol.kiyarash.weatherapp.viewModel.WeatherViewModel


class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by activityViewModels()

    private lateinit var city: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            city = it.getString("cityName").toString()
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        viewModel.resetBackground()
        viewModel.getWeatherData(city)
        binding.viewModel = viewModel
        viewModel.statusBarColor.observe(viewLifecycleOwner) { color ->
            activity?.window?.statusBarColor = resources.getColor(color, activity?.theme)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // find the views
        val refreshButton = binding.refreshButton
        val searchButton = binding.searchButton
        val cityButton = binding.cityButton


        refreshButton.setOnClickListener {
            viewModel.getWeatherData(city)
        }
        cityButton.setOnClickListener {
            navigateToSearchFragment()
        }

        searchButton.setOnClickListener {
            navigateToSearchFragment()
        }

        cityButton.text = "City: ${this.city}"


    }

    private fun navigateToSearchFragment() {
        val action = WeatherFragmentDirections.actionWeatherFragmentToSearchFragment()
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}