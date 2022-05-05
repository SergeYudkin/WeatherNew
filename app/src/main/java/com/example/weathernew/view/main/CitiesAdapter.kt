package com.example.weathernew.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weathernew.R
import com.example.weathernew.model.Weather

class CitiesAdapter(val listener: OnMyItemClickListener): RecyclerView.Adapter<CitiesAdapter.MainViewHolder>() {  // MainFragmentAdapter передаём экземпляр умеющий в себя принимать клики (val listener: OnMyItemClickListener)

    private var weatherData:List<Weather> = listOf()

    fun setWeather(data:List<Weather>){        // обновляет списки городов ( Россия - мир )
        this.weatherData = data
        notifyDataSetChanged()
    }
//------------------------------------------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesAdapter.MainViewHolder {
        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_recycler_city_item,parent,false))    // группа контейнеров
    }
//------------------------------------------------------------------------------------------------------------------------------------------

    override fun onBindViewHolder(holder: CitiesAdapter.MainViewHolder, position: Int) {          //  наполнение контейнеров новыми данными
        holder.bind(this.weatherData[position])
    }
//--------------------------------------------------------------------------------------------------------------------------------------------
    override fun getItemCount(): Int {
        return weatherData.size
    }

    inner class MainViewHolder(view:View):RecyclerView.ViewHolder(view) {         // inner внутренний класс  MainViewHolder по отношению к MainFragmentAdapter для получения доступа к listener
        fun bind(weather: Weather) {                                                // так как listener указан в адаптере как внутреннее свойство
            with(itemView){
                findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text = weather.city.name
                setOnClickListener{
                    listener.onItemClick(weather)
            }

            }
        }
    }
}