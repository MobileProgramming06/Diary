package com.example.teamproject.DBHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EmotionDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "emotions.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "emotions"
        const val COLUMN_DATE = "date"
        const val COLUMN_EMOTION = "emotion"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_DATE TEXT PRIMARY KEY, "
                + "$COLUMN_EMOTION TEXT)")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertEmotion(date: String, emotion: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, date)
            put(COLUMN_EMOTION, emotion)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getEmotion(date: String): String? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_EMOTION),
            "$COLUMN_DATE = ?",
            arrayOf(date),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            val emotion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMOTION))
            cursor.close()
            emotion
        } else {
            cursor.close()
            null
        }
    }
}
