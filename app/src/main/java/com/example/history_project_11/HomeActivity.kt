package com.example.history_project_11

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

class HomeActivity : ComponentActivity() {

  private lateinit var mapview: MapView
  private lateinit var databaseHelper: DatabaseHelper

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MapKitFactory.setApiKey("3d98c528-9e5f-4cf8-847c-adb5af11b2df")
    MapKitFactory.initialize(this)
    setContentView(R.layout.activity_main)

    databaseHelper = DatabaseHelper(this)

    mapview = findViewById(R.id.mapview)
    mapview.map.move(
      CameraPosition(Point(59.966169, 30.306153), 13.0f, 0.0f, 0.0f),
      Animation(Animation.Type.SMOOTH, 1f), null
    )
    addMultiplePoints()
  }

  private fun addMarkersFromDatabase() {
    val attractions = databaseHelper.getAllAttractions()

    for (attraction in attractions) {
      addMultiplePoints()
    }
  }

  fun addMultiplePoints() {
    // Создание PlacemarkMapObject с иконкой изображения и обработчиком событий при тапе на метку
    val points = listOf(
      Point(59.976607, 30.320863),
      Point(59.971258, 30.322857),
      Point(59.977242, 30.307253),
      Point(59.971956, 30.320306),
      Point(59.975842, 30.311161),
      Point(59.969551, 30.314557),
      Point(59.965313, 30.321860),
      Point(59.954128, 30.314036),
      Point(59.974189, 30.305537),
      Point(59.955592, 30.337151),
      Point(59.954394, 30.333089),
      Point(59.955687, 30.336197),
      Point(59.950330, 30.315401),
      Point(59.954863, 30.324797),
      Point(59.954592, 30.325067),
      Point(59.955683, 30.312733),
      Point(59.956363, 30.310029),
      Point(59.953453, 30.308942),
      Point(59.956940, 30.309364),
      Point(59.958278, 30.316991),
      Point(59.962260, 30.313317),
      Point(59.952281, 30.285514),
      Point(59.965890, 30.284454),
      Point(59.974707, 30.316335),
      Point(59.957363, 30.320333),
      Point(59.968624, 30.312634),
      Point(59.953646, 30.326796),
      Point(59.952921, 30.327852),
      Point(59.958931, 30.300489),
      Point(59.955912, 30.296429),
      Point(59.954723, 30.294901),
      Point(59.954451, 30.295225),
      Point(59.952490, 30.290877),
      Point(59.950164, 30.288069),
      Point(59.950294, 30.292109),
      Point(59.966502, 30.311442),
      Point(59.966444, 30.311421),
      Point(59.952686, 30.325957),
      Point(59.951754, 30.325732),
      Point(59.973325, 30.325112),
      Point(59.965710, 30.295105),
      Point(59.963438, 30.298992),
      Point(59.965998, 30.313011),
      Point(59.955514, 30.324891)
    )




    val imageProvider = ImageProvider.fromResource(this, R.drawable.placemark_icon)

    points.forEach { point ->
      val placemark = mapview.map.mapObjects.addPlacemark(point).apply {
        geometry = point
        setIcon(imageProvider)
      }
    }



  }

  override fun onStop() {
    mapview.onStop()
    MapKitFactory.getInstance().onStop()
    super.onStop()
  }

  override fun onStart() {
    mapview.onStart()
    MapKitFactory.getInstance().onStart()
    super.onStart()
  }
}


