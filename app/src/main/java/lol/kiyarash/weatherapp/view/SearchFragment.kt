package lol.kiyarash.weatherapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.gson.Gson
import lol.kiyarash.weatherapp.R
import lol.kiyarash.weatherapp.data.CitiesModel
import lol.kiyarash.weatherapp.databinding.*
import lol.kiyarash.weatherapp.viewModel.WeatherViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by activityViewModels()


    //private var cityData: Array<CitiesModel>? = null
    private var cityData = mutableListOf<CitiesModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        cityData = readJson()
        binding.recyclerView.adapter = SearchAdapter(cityData)
        val itemDecorator =
            MaterialDividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                dividerColor = resources.getColor(R.color.light_gray, activity?.theme)
            }
        binding.recyclerView.addItemDecoration(itemDecorator)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel



        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeSearchView()
    }

    private fun initializeSearchView() {
        val searcher = binding.searchView
        searcher.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String): Boolean {
                return if (text.isNotEmpty() && text.length < 3) {
                    false
                } else {
                    val resultList = mutableListOf<CitiesModel>()
                    for (city in cityData) {
                        if (city.city.lowercase().contains(text.lowercase())) {
                            resultList.add(city)
                        }
                    }
                    binding.recyclerView.adapter = SearchAdapter(resultList)
                    true
                }
            }

        })


    }


    private fun readJson(): MutableList<CitiesModel> {
        val json = context?.assets?.open("cities.json")?.bufferedReader().use { it?.readText() }
        return Gson().fromJson(json, Array<CitiesModel>::class.java).asList().toMutableList()
    }


}