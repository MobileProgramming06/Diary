package com.example.teamproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.teamproject.databinding.ActivityEditCalBinding

class EditCalActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditCalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra("type")
        var cal: Cal? = null
        if (type == "EDIT") {
            cal = intent.getSerializableExtra("cal") as Cal
            binding.diaryContent.setText(cal.content)
        }

        binding.saveBtn.setOnClickListener {
            val content = binding.diaryContent.text.toString()
            val id = cal?.id ?: System.currentTimeMillis()

            val newCal = Cal(id, content)

            val resultIntent = Intent().apply {
                putExtra("schedule", newCal)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
