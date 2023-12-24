package com.example.history_project_11

import android.os.Bundle
import android.util.Log
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

  private val markerInfoList = listOf(
    MarkerInfo(R.drawable.logo, "Название места 1", "Описание места 1"),
    MarkerInfo(R.drawable.movie, "Название места 2", "Описание места 2"),
    MarkerInfo(R.drawable.ic_close, "Название места 3", "Описание места 3"),
    // ... добавьте для каждой метки
  )

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
    // Определитель нажатия кнопки закрытия
    closeButton.setOnClickListener {
      closeMarkerInfo()
    }
  }

  private fun addMultiplePoints() {
    val points = listOf(
      Point(59.976607, 30.320863),
      Point(59.971258, 30.322857),
      Point(59.977242, 30.307253),
      // ... (другие точки)
    )

    val imageProvider = ImageProvider.fromResource(this, R.drawable.placemark_icon)

    points.forEachIndexed { index, point ->
      val placemark = mapview.map.mapObjects.addPlacemark(point, imageProvider)

      // Добавляем отладочное сообщение для отслеживания нажатия на метку
      Log.d("MarkerTap", "Marker added: $index")

      placemark.addTapListener { _, _ ->
        Log.d("MarkerTap", "Marker tapped: $index")
        // Загрузка и отображение информации о маркере
        if (placemark == selectedMarker) {
          closeMarkerInfo()
          selectedMarker = null
        } else {
          val markerInfo = markerInfoList[index]
          showMarkerInfo(markerInfo.photoResId, markerInfo.title, markerInfo.description)
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
