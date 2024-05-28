package com.example.teamproject.DBHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LoginDBHelper(context: Context) : SQLiteOpenHelper(context, "Login.db", null, 1) {

    // DB 이름을 Login.db로 설정
    companion object {
        const val DBNAME = "Login.db"
    }

    // user 테이블 생성
    override fun onCreate(MyDB: SQLiteDatabase?) {
        MyDB?.execSQL("create Table users(id TEXT primary key, password TEXT, nick TEXT)")
    }

    // 정보 갱신
    override fun onUpgrade(MyDB: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        MyDB?.execSQL("drop Table if exists users")
    }

    // id, password, nick 삽입
    fun insertData(id: String?, password: String?, nick: String?): Boolean {
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("id", id)
        contentValues.put("password", password)
        contentValues.put("nick", nick)
        val result = MyDB.insert("users", null, contentValues)
        MyDB.close()
        // DB 삽입 실패 시 false, 성공 시 true
        return if (result == -1L) false else true
    }

    // 사용자 아이디가 없으면 false, 이미 존재하면 true
    fun checkUser(id: String?): Boolean {
        val MyDB = this.readableDatabase
        var res = true
        val cursor = MyDB.rawQuery("Select * from users where id = ?", arrayOf(id))
        if (cursor.count <= 0)      // DB에 아이디가 없는 경우
            res = false
        return res
    }

    // 사용자 닉네임이 없으면 false, 이미 존재하면 true
    fun checkNick(nick: String?): Boolean {
        val MyDB = this.readableDatabase
        var res = true
        val cursor = MyDB.rawQuery("Select * from users where nick = ?", arrayOf(nick))
        if (cursor.count <= 0)      // DB에 닉네임이 없는 경우
            res = false
        return res
    }

    // 해당 id, password가 있는지 확인 (없으면 false)
    fun checkUserPassword(id: String, password: String): Boolean {
        val MyDB = this.writableDatabase
        var res = true
        val cursor = MyDB.rawQuery(
            "Select * from users where id = ? and password = ?",
            arrayOf(id, password)
        )
        if (cursor.count <= 0)
            res = false
        return res
    }
}