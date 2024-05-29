package com.example.teamproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teamproject.DBHelper.EmotionDBHelper
import com.example.teamproject.R

class EmotionActivity : AppCompatActivity() {

    private lateinit var editTextEmotion: EditText
    private lateinit var dbHelper: EmotionDBHelper
    private var selectedDate: String? = null

    companion object {
        const val EXTRA_EMOTION = "com.example.teamproject.EXTRA_EMOTION"
        const val EXTRA_DATE = "com.example.teamproject.EXTRA_DATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emotion)
        title="Emoji"

        editTextEmotion = findViewById(R.id.edit_text_emotion)
        dbHelper = EmotionDBHelper(this)

        selectedDate = intent.getStringExtra(EXTRA_DATE)

        val buttonSave: Button = findViewById(R.id.button_save)
        buttonSave.setOnClickListener {
            saveEmotion()
        }
    }

    private fun saveEmotion() {
        val emotion = editTextEmotion.text.toString()

        if (emotion.trim().isEmpty()) {
            Toast.makeText(this, "Please enter an emotion", Toast.LENGTH_SHORT).show()
            return
        }

        selectedDate?.let {
            dbHelper.insertEmotion(it, emotion)
        }

        val data = Intent().apply {
            putExtra(EXTRA_EMOTION, emotion)
        }

        setResult(Activity.RESULT_OK, data)
        finish()
    }
}