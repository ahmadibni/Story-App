package com.example.submission1intermediate.ui.activity

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.submission1intermediate.R
import com.example.submission1intermediate.data.remote.response.ListStoryMapItem
import com.example.submission1intermediate.databinding.ActivityMapsBinding
import com.example.submission1intermediate.ui.viewmodel.MapViewModel
import com.example.submission1intermediate.utils.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.submission1intermediate.utils.Result

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var _binding: ActivityMapsBinding? = null
    private val binding get() = _binding

    private lateinit var token: String
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var storyList: List<ListStoryMapItem>

    private val viewModel by viewModels<MapViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        token = intent.getStringExtra(USER_TOKEN).toString()
        setupMap()
        viewModel.getMapStories(token).observe(this){result->
            if (result != null){
                when(result){
                    is Result.Loading -> {}
                    is Result.Error -> {}
                    is Result.Success -> {
                        storyList= result.data.listStory
                        addManyMarker(storyList)
                    }
                }
            }
        }
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun addManyMarker(storyList: List<ListStoryMapItem>) {
        val customInfoWindow = layoutInflater.inflate(R.layout.maps_detail, null)
        val imageView = customInfoWindow.findViewById<ImageView>(R.id.iv_map_photo)
        val titleTextView = customInfoWindow.findViewById<TextView>(R.id.tv_map_name)
        val descriptionTextView = customInfoWindow.findViewById<TextView>(R.id.tv_map_description)
        storyList.forEach { story ->
            val latLng = LatLng(
                story.lat as Double,
                story.lon as Double
            )
            val markerOptions = MarkerOptions()
                .position(latLng)
                .title(story.name)
                .snippet(story.description)

            Glide.with(applicationContext)
                .load(story.photoUrl)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        val marker = mMap.addMarker(markerOptions)
                        val markerData = MarkerData(customInfoWindow, resource)
                        marker?.tag = markerData
                        marker?.showInfoWindow()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Optional: Handle placeholder or clear resources if needed
                    }
                })

            boundsBuilder.include(latLng)
        }

        mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null // Return null to use the default info window
            }

            override fun getInfoContents(marker: Marker): View? {
                val markerData = marker.tag as? MarkerData
                val infoWindow = markerData?.customInfoWindow
                titleTextView.text = marker.title
                descriptionTextView.text = marker.snippet
                imageView.setImageDrawable(markerData?.image)
                return infoWindow
            }
        })

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    private data class MarkerData(val customInfoWindow: View, val image: Drawable)

    companion object {
        const val USER_TOKEN = "user_token"
    }
}