package com.example.teamproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.teamproject.databinding.ActivityEditCalBinding

class EditCalActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditCalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra("type")
        if (type == "EDIT") {
            val cal = intent.getSerializableExtra("cal") as Cal
            binding.diaryContent.setText(cal.content)
        }

        binding.saveBtn.setOnClickListener {
            val content = binding.diaryContent.text.toString()
            val id = System.currentTimeMillis() // 임시로 ID 생성

            val cal = Cal(id, content)

            val resultIntent = Intent().apply {
                putExtra("schedule", cal)
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}