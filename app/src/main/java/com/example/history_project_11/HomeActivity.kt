package com.example.history_project_11

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class HomeActivity : ComponentActivity() {
  lateinit var mapview: MapView
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MapKitFactory.setApiKey("3d98c528-9e5f-4cf8-847c-adb5af11b2df")
    MapKitFactory.initialize(this)
    setContentView(R.layout.activity_main)
    mapview = findViewById(R.id.mapview)
    mapview.map.move(CameraPosition(
      com.yandex.mapkit.geometry.Point(59.966169, 30.306153), // Петрограйский район
      11.0f,
      0.0f,
      0.0f
    ), Animation(Animation.Type.SMOOTH, 1f), null)
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

