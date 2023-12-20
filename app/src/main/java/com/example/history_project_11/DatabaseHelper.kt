package com.example.history_project_11

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  companion object {
    const val DATABASE_NAME = "attractionsPD.db"
    const val DATABASE_VERSION = 1
    const val TABLE_ATTRACTIONS = "attractions"
    const val COLUMN_ID = "_id"
    const val COLUMN_NAME = "name"
    const val COLUMN_DESCRIPTION = "description"
    const val COLUMN_LATITUDE = "latitude"
    const val COLUMN_LONGITUDE = "longitude"
    const val COLUMN_PHOTO = "photo"
  }

  override fun onCreate(db: SQLiteDatabase?) {
    val createTableQuery = """
            CREATE TABLE $TABLE_ATTRACTIONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_LATITUDE REAL,
                $COLUMN_LONGITUDE REAL,
                $COLUMN_PHOTO BLOB
            )
        """
    db?.execSQL(createTableQuery)
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    db?.execSQL("DROP TABLE IF EXISTS $TABLE_ATTRACTIONS")
    onCreate(db)
  }

  fun insertAttraction(name: String, description: String, latitude: Double, longitude: Double, photoResId: Int): Long {
    val values = ContentValues().apply {
      put(COLUMN_NAME, name)
      put(COLUMN_DESCRIPTION, description)
      put(COLUMN_LATITUDE, latitude)
      put(COLUMN_LONGITUDE, longitude)
      put(COLUMN_PHOTO, photoResId)
    }

    return writableDatabase.insert(TABLE_ATTRACTIONS, null, values)
  }

  fun getAllAttractions(): Cursor {
    val selectQuery = "SELECT * FROM $TABLE_ATTRACTIONS"
    return readableDatabase.rawQuery(selectQuery, null)
  }
}
