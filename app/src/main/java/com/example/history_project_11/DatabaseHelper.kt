package com.example.history_project_11

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.SQLException
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DatabaseHelper(private val myContext: Context) : SQLiteOpenHelper(myContext, DATABASE_NAME, null, DATABASE_VERSION) {
  companion object {
    const val DATABASE_NAME = "attractionsPD.db"
    const val DATABASE_VERSION = 1
    const val TABLE_ATTRACTIONS = "attractionsPD"
    private var DB_PATH: String? = null
    const val COLUMN_ID = "id"
    const val COLUMN_LONGITUDE = "longitude"
    const val COLUMN_LATITUDE = "latitude"
    const val COLUMN_DESCRIPTION = "description"
    const val COLUMN_NAME = "name"
    const val COLUMN_PHOTO = "photo"
  }

  init {
    DB_PATH = myContext.filesDir.path + DATABASE_NAME
  }

  override fun onCreate(db: SQLiteDatabase?) {}

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

  fun createDb() {
    val file = File(DB_PATH!!)
    if (!file.exists()) {
      try {
        myContext.assets.open(DATABASE_NAME).use { myInput ->
          FileOutputStream(DB_PATH!!).use { myOutput ->
            myInput.copyTo(myOutput)
          }
        }
      } catch (ex: IOException) {
        Log.d("DatabaseHelper", ex.message!!)
      }
    }
  }

  @Throws(SQLException::class)
  fun open(): SQLiteDatabase = SQLiteDatabase.openDatabase(DB_PATH.toString(), null, SQLiteDatabase.OPEN_READWRITE)

  fun getAllAttractions(): List<Attraction> {
    val attractions = mutableListOf<Attraction>()
    readableDatabase.rawQuery("SELECT * FROM $TABLE_ATTRACTIONS", null).use { cursor ->
      try {
        while (cursor.moveToNext()) {
          val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
          val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
          val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE))
          val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
          val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
          val photoPath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO))

          val attraction = Attraction(id, longitude, latitude, name, description, photoPath)
          attractions.add(attraction)
        }
      } catch (e: Exception) {
        Log.e("DatabaseHelper", "Error while fetching attractions: ${e.message}")
      }
    }

    return attractions
  }
}
