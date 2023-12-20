package com.example.history_project_11

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

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
    mapview.map.move(CameraPosition(
      com.yandex.mapkit.geometry.Point(59.966169, 30.306153), // Петрограйский район
      11.0f,
      0.0f,
      0.0f
    ), Animation(Animation.Type.SMOOTH, 1f), null)

    // Добавление меток
    addMarkersFromDatabase()
  }

  private fun addMarkersFromDatabase() {
    val mapObjects = mapview.map.mapObjects.addCollection()

    // Получаем данные из базы данных
    val attractionsCursor = databaseHelper.getAllAttractions()

    while (attractionsCursor.moveToNext()) {
      val name = attractionsCursor.getString(attractionsCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME))
      val description = attractionsCursor.getString(attractionsCursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION))
      val latitude = attractionsCursor.getDouble(attractionsCursor.getColumnIndex(DatabaseHelper.COLUMN_LATITUDE))
      val longitude = attractionsCursor.getDouble(attractionsCursor.getColumnIndex(DatabaseHelper.COLUMN_LONGITUDE))
      val photo = attractionsCursor.getBlob(attractionsCursor.getColumnIndex(DatabaseHelper.COLUMN_PHOTO))

      // Создаем метку и добавляем в коллекцию
      val marker = createMarker(
        com.yandex.mapkit.geometry.Point(latitude, longitude),
        name,
        description,
        photo
      )
      mapObjects.add(marker)
    }

    attractionsCursor.close()
  }

  private fun createMarker(point: com.yandex.mapkit.geometry.Point, title: String, description: String, photoResId: Int): PlacemarkMapObject {
    val view = LayoutInflater.from(this).inflate(R.layout.custom_marker_layout, null)
    val titleTextView: TextView = view.findViewById(R.id.titleTextView)
    val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
    val photoImageView: ImageView = view.findViewById(R.id.photoImageView)

    titleTextView.text = title
    descriptionTextView.text = description
    photoImageView.setImageResource(photoResId)

    val placemarkMapObject = mapview.map.mapObjects.addPlacemark(point)
    placemarkMapObject.setView(view)

    return placemarkMapObject
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

