package com.example.history_project_11

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.PlacemarkMapObject
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
      val name = attractionsCursor.getString(attractionsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
      val description = attractionsCursor.getString(attractionsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION))
      val latitude = attractionsCursor.getDouble(attractionsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LATITUDE))
      val longitude = attractionsCursor.getDouble(attractionsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LONGITUDE))
      val photo = attractionsCursor.getString(attractionsCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHOTO))

      // Создаем метку и добавляем в коллекцию
      val marker = createMarker(
        com.yandex.mapkit.geometry.Point(latitude, longitude),
        name,
        description,
        photo
      )
      mapview.map.mapObjects.addPlacemark(marker)
    }

    attractionsCursor.close()
  }

  private fun createMarker(point: Point, title: String, description: String, photoPath: String): Placemark {
    val view = LayoutInflater.from(this).inflate(R.layout.custom_marker_layout, null)
    val titleTextView: TextView = view.findViewById(R.id.titleTextView)
    val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
    val photoImageView: ImageView = view.findViewById(R.id.photoImageView)

    titleTextView.text = title
    descriptionTextView.text = description

    // Преобразуйте путь к файлу в Bitmap
    val bitmap = BitmapFactory.decodeFile(photoPath)
    photoImageView.setImageBitmap(bitmap)

    val placemarkMapObject = mapview.map.mapObjects.addPlacemark(point)

    // Используйте ViewProvider для установки макета
    placemarkMapObject.setViewProvider(
      CustomViewProvider(view)
    )

    // Добавьте обработчик событий для метки
    placemarkMapObject.addTapListener { _, _ ->
      // Отобразите окно с фото, названием и описанием
      showAttractionDetails(title, description, bitmap)
      true
    }

    return placemarkMapObject
  }

  // Метод для отображения окна с деталями достопримечательности
  private fun showAttractionDetails(title: String, description: String, photo: Bitmap) {
    val dialogBuilder = AlertDialog.Builder(this)
    val view = layoutInflater.inflate(R.layout.attraction_details_layout, null)

    // Настройте элементы интерфейса в соответствии с вашим макетом
    val titleTextView: TextView = view.findViewById(R.id.attractionTitleTextView)
    val descriptionTextView: TextView = view.findViewById(R.id.attractionDescriptionTextView)
    val photoImageView: ImageView = view.findViewById(R.id.attractionPhotoImageView)

    titleTextView.text = title
    descriptionTextView.text = description
    photoImageView.setImageBitmap(photo)

    dialogBuilder.setView(view)
      .setPositiveButton("Закрыть") { dialog, _ -> dialog.dismiss() }

    val alertDialog = dialogBuilder.create()
    alertDialog.show()
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

