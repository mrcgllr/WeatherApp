package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.Current
import com.example.weatherapp.model.Daily
import kotlinx.android.synthetic.main.list_item_day.view.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherDayAdapter(private val weatherList: List<Daily>) :
    RecyclerView.Adapter<WeatherDayAdapter.WeatherDayViewHolder>() {

    class WeatherDayViewHolder(container: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(container.context).inflate(R.layout.list_item_day, container, false)
    ) {

        fun bind(forecast: Daily) {
            itemView.list_item_day_txt_time.text = dayConverter(forecast.dt.toLong())
            itemView.list_item_day_txt_temperature.text = forecast.temp?.day.toString()
            itemView.list_item_day_txt_feels.text = forecast.feels_like?.day.toString()
            itemView.list_item_day_txt_humidity.text = forecast.humidity.toString()
            itemView.list_item_day_txt_pressure.text = forecast.pressure.toString()
            itemView.list_item_day_txt_wind.text = forecast.wind_speed.toString()

        }

        private fun dayConverter(time: Long): String {
            var converter = SimpleDateFormat("EEE, d MMM yyyy")
            var convertedDay = converter.format(Date(time * 1000))

            return convertedDay
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDayViewHolder =
        WeatherDayViewHolder(parent)

    override fun getItemCount(): Int = weatherList.size

    override fun onBindViewHolder(holder: WeatherDayViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }
}