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


        /*activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                navigateToSearchFragment()
            }
        })*/


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this


        viewModel.resetBackground()
        viewModel.getWeatherData(city)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel
        //activity?.window?.statusBarColor = viewModel.statusBarColor.value!!

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


        //initialize the values
        //lastUpdate.text = "Updated Just Now"
        //statusImageView.setImageResource(R.drawable.partly_day_storm)
        //temperature.text = "23°C"


        //Log.v("tag3", "geo:" + viewModel.geocode.toString())
        //statusTextView.text = viewModel.geocode.value


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