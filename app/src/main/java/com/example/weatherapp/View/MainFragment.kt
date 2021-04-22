package com.example.weatherapp.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.ViewModel.MainViewModel
import com.example.weatherapp.databinding.MainFragmentBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private var senior = false;
    private var city = "";

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settings = context?.getSharedPreferences("Settings", 0)
        senior = settings?.getBoolean("Senior", false) ?: false
        city = settings?.getString("City", "") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.currentWeather.observe(this, Observer {
            binding.cityNameText.text = it.name
            binding.temperatureText.text = "${it.main.temp.roundToInt()}°C"
            binding.pressureText.text = "${it.main.pressure.toString()} hPa"
            binding.descriptionText.text = it.weather[0].description.capitalize()
            binding.sunriseText.text = getCorrectDate(it.sys.sunrise, it.timezone, true)
            binding.sunsetText.text = getCorrectDate(it.sys.sunset, it.timezone, true)
            binding.dateText.text = getCorrectDate(it.dt, it.timezone, false)
            binding.timeText.text = getCorrectDate(it.dt, it.timezone, true)
            binding.imageView.setImageResource(resources.getIdentifier("icon_${it.weather[0].icon}",
                "drawable",
                activity?.packageName))
        })
        viewModel.getWeather(if (city == "") "Warsaw" else city)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchButton.setOnClickListener {
            city = binding.cityNameEditText.text.toString()
            viewModel.getWeather(city)
            val settings = context?.getSharedPreferences("Settings", 0)
            val editor = settings?.edit()
            editor?.putString("City", city)
            editor?.apply()
        }
        binding.accessibilityButton.setOnClickListener {
            senior = !senior
            val settings = context?.getSharedPreferences("Settings", 0)
            val editor = settings?.edit()
            editor?.putBoolean("Senior", senior)
            editor?.apply()
            val i: Intent? = context?.packageName?.let { it1 ->
                context?.packageManager?.getLaunchIntentForPackage(it1)
            }
            i?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCorrectDate(time: Long, timezone: Int, onlyTime: Boolean) : String
    {
        val date = Date((time + timezone) * 1000)
        val format = if (onlyTime) SimpleDateFormat("HH:mm") else SimpleDateFormat("EEE d MMMM", Locale("pl", "PL"))
        return format.format(date)
    }
}