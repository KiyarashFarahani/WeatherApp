package lol.kiyarash.weatherapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import lol.kiyarash.weatherapp.R
import lol.kiyarash.weatherapp.data.CitiesModel


class SearchAdapter(private val cityData: MutableList<CitiesModel>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        //find view by id the items to display in the recycler view:
        val cityTextView: TextView = view.findViewById(R.id.cityItemTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView: View = layoutInflater.inflate(R.layout.search_item_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        //has to return the item count to recyclerview.
        return cityData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = cityData[position].city
        holder.cityTextView.text = data

        holder.cityTextView.setOnClickListener {

            val action = SearchFragmentDirections
                .actionSearchFragmentToWeatherFragment(data)

            holder.view.findNavController().navigate(action)

        }
    }


}
