package com.example.weatherapp

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.util.Constant.API_KEY
import com.example.weatherapp.util.Constant.METRIC
import com.example.weatherapp.viewmodel.CityWeatherViewModel
import im.delight.android.location.SimpleLocation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //  binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //  binding.lifecycleOwner = this



    }
}