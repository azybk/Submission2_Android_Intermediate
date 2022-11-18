package com.aink.submission1.ui

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aink.submission1.R
import com.aink.submission1.data.ListStoryItem
import com.aink.submission1.data.Result
import com.aink.submission1.databinding.ActivityMapsBinding
import com.aink.submission1.utils.UserPreference
import com.aink.submission1.utils.util.logout
import com.aink.submission1.viewmodel.ListMapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundBuilder = LatLngBounds.builder()

    private val listMapViewModel: ListMapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        showMap(mMap)
    }

    private fun showMap(mMap: GoogleMap) {
        val mUserPreference = UserPreference(this)
        val token = mUserPreference.getUser()

        if (token == null) {
            logout(this)

        } else {
            listMapViewModel.getMapStory(token).observe(this) { result ->
                if (result != null) {
                    when(result) {
                        is Result.Success -> {
                           showItemMap(mMap, result.data)
                        }

                        is Result.Error -> {
                            Log.d("MapsActivity", "something went wrong")
                        }
                    }
                }
            }
        }
    }

    private fun showItemMap(mMap: GoogleMap, listStory: List<ListStoryItem>) {
        if (listStory != null) {

            listStory.forEach {
                val latLng = LatLng(it.lat ?: 0.0, it.lon ?: 0.0)
                val name = it.name

                if((it.lat!! >= -90.0 && it.lat <= 90.0) && (it.lon!! >= -180.0 && it.lon <= 180.0)) {
                    val addressName = getAddressName(it.lat ?: 0.0, it.lon ?: 0.0)
                    mMap.addMarker(MarkerOptions().position(latLng).title(name).snippet(addressName))
                    boundBuilder.include(latLng)
                }
            }

            val bounds: LatLngBounds = boundBuilder.build()
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    300
                )
            )
        }
    }

    private fun getAddressName(lat: Double, long: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())

        try {
            val list = geocoder.getFromLocation(lat, long, 1)

            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                Log.d("MapsActivity", "getAddressName: $addressName")
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return addressName
    }
}