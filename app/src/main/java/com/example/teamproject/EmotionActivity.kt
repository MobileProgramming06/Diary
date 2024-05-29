package com.example.teamproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddEmotionActivity : AppCompatActivity() {

    private lateinit var editTextEmotion: EditText

    companion object {
        const val EXTRA_EMOTION = "com.example.teamproject.EXTRA_EMOTION"
        const val EXTRA_DATE = "com.example.teamproject.EXTRA_DATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emotion)

        editTextEmotion = findViewById(R.id.edit_text_emotion)

        val date = intent.getStringExtra(EXTRA_DATE)
        title = "Emotion for $date"

        val buttonSave: Button = findViewById(R.id.button_save)
        buttonSave.setOnClickListener {
            saveEmotion(date ?: "")
        }
    }

    private fun saveEmotion(date: String) {
        val emotion = editTextEmotion.text.toString()

        if (emotion.trim().isEmpty()) {
            Toast.makeText(this, "Please enter an emotion", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent().apply {
            putExtra(EXTRA_EMOTION, emotion)
            putExtra(EXTRA_DATE, date)
        }

        setResult(Activity.RESULT_OK, data)
        finish()
    }
}
