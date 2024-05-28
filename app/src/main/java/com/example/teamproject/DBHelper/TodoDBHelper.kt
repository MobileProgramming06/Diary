package com.example.teamproject.DBHelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.Serializable

data class Todo (
    val id: Long,
    val title: String,
    val content: String,
    val timestamp: String,
    var isChecked: Boolean
): Serializable
class TodoDBHelper(context: Context) : SQLiteOpenHelper(context, "TodoList.db", null, 2) {

    // DB 이름을 TodoList.db로 설정
    companion object {
        const val DBNAME = "TodoList.db"
        const val TABLE_NAME = "todoTable"
        const val ID = "id"
        const val TITLE = "title"
        const val CONTENT = "content"
        const val TIMESTAMP = "timestamp"
        const val IsChecked = "isChecked"
    }

    // user 테이블 생성
    override fun onCreate(MyDB: SQLiteDatabase?) {
        Log.d("TodoDBHelper", "onCreate called.")
        val createSQL = "create table $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $TITLE TEXT, $CONTENT TEXT, $TIMESTAMP TEXT, $IsChecked INTEGER)"
        MyDB?.execSQL(createSQL)
    }

    // 정보 갱신
    override fun onUpgrade(MyDB: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val upgradeSQL = "drop Table if exists $TABLE_NAME"
        MyDB?.execSQL(upgradeSQL)
    }

    // id, password, nick 삽입
    fun insertData(title: String, content: String, timestamp: String, isChecked: Boolean): Boolean {
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TITLE, title)
        contentValues.put(CONTENT, content)
        contentValues.put(TIMESTAMP, timestamp)
        contentValues.put(IsChecked, if (isChecked) 1 else 0)
        val result = MyDB.insert(TABLE_NAME, null, contentValues)
        MyDB.close()
        // DB 삽입 실패 시 false, 성공 시 true
        return if (result == -1L) false else true
    }

    @SuppressLint("Range")
    fun getTodoById(id: Long): Todo? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(ID, TITLE, CONTENT, TIMESTAMP, IsChecked), "$ID=?", arrayOf(id.toString()), null, null, null)
        lateinit var todo: Todo
        cursor?.use {
            if (it.moveToFirst()) {
                var title = it.getString(it.getColumnIndex(TITLE))
                var content = it.getString(it.getColumnIndex(CONTENT))
                val timestamp = cursor.getString(cursor.getColumnIndex(TIMESTAMP))
                val isChecked = cursor.getInt(cursor.getColumnIndex(IsChecked)) == 1
                todo = Todo(id, title, content, timestamp, isChecked)
            }
        }
        cursor.close()
        return todo
    }

    @SuppressLint("Range")
    fun getAllTodos(): MutableList<Todo> {
        val todoList = mutableListOf<Todo>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(ID))
                val title = cursor.getString(cursor.getColumnIndex(TITLE))
                val content = cursor.getString(cursor.getColumnIndex(CONTENT))
                val timestamp = cursor.getString(cursor.getColumnIndex(TIMESTAMP))
                val isChecked = cursor.getInt(cursor.getColumnIndex(IsChecked)) == 1
                val todo = Todo(id, title, content, timestamp, isChecked)
                todoList.add(todo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todoList
    }

    fun update(todo: Todo?): Int {
        val db = this.readableDatabase
        val contentValues = ContentValues()
        contentValues.put(TITLE, todo?.title)
        contentValues.put(CONTENT, todo?.content)
        contentValues.put(TIMESTAMP, todo?.timestamp)
        contentValues.put(IsChecked, if (todo?.isChecked == true) 1 else 0)
        val result = db.update(TABLE_NAME, contentValues, "$ID = ?", arrayOf(todo?.id.toString()))
        db.close()
        return result
    }

    fun deleteTodoById(todoId: Long): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$ID = ?", arrayOf(todoId.toString()))
        db.close()
        return result
    }
}