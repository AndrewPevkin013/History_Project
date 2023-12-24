package com.example.history_project_11

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

class HomeActivity : ComponentActivity() {

  private lateinit var mapview: MapView
  private lateinit var markerInfoLayout: FrameLayout
  private lateinit var photoImageView: ImageView
  private lateinit var titleTextView: TextView
  private lateinit var descriptionTextView: TextView
  private lateinit var closeButton: ImageButton
  private var selectedMarker: MapObject? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MapKitFactory.setApiKey("3d98c528-9e5f-4cf8-847c-adb5af11b2df")
    MapKitFactory.initialize(this)
    setContentView(R.layout.activity_main)

    mapview = findViewById(R.id.mapview)
    mapview.map.move(
      CameraPosition(Point(59.966169, 30.306153), 13.0f, 0.0f, 0.0f),
      Animation(Animation.Type.SMOOTH, 1f), null
    )
    markerInfoLayout = findViewById(R.id.markerInfoLayout)
    photoImageView = findViewById(R.id.photoImageView)
    titleTextView = findViewById(R.id.titleTextView)
    descriptionTextView = findViewById(R.id.descriptionTextView)
    closeButton = findViewById(R.id.closeButton)

    addMultiplePoints()
    setListeners()
  }

  private fun setListeners() {
    // Close button click listener
    closeButton.setOnClickListener {
      closeMarkerInfo()
    }
  }

  private fun addMultiplePoints() {
    val points = listOf(
      Point(59.976607, 30.320863),
      Point(59.971258, 30.322857),
      Point(59.977242, 30.307253),
      // ... (other points)
    )

    val imageProvider = ImageProvider.fromResource(this, R.drawable.placemark_icon)

    points.forEach { point ->
      val placemark = mapview.map.mapObjects.addPlacemark(point, imageProvider)

      // Set click listener for each placemark
      placemark.addTapListener { _, _ ->
        // Load and display marker information
        if (placemark == selectedMarker) {
          // If the tapped marker is already selected, close the info window
          closeMarkerInfo()
          selectedMarker = null
        } else {
          // If a different marker is tapped, close the existing info window
          // and open the info window for the new marker
          showMarkerInfo(R.drawable.logo, "Название места", "Описание места")
          selectedMarker = placemark
        }
        true
      }
    }
  }

  private fun showMarkerInfo(photoResId: Int, title: String, description: String) {
    // Load photo into ImageView
    photoImageView.setImageResource(photoResId)

    // Set title and description
    titleTextView.text = title
    descriptionTextView.text = description

    // Show the marker information layout
    markerInfoLayout.visibility = View.VISIBLE
  }

  private fun closeMarkerInfo() {
    // Hide the marker information layout
    markerInfoLayout.visibility = View.GONE
  }

  override fun onStart() {
    mapview.onStart()
    MapKitFactory.getInstance().onStart()
    super.onStart()
  }
}
