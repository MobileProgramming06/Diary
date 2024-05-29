package com.example.teamproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teamproject.databinding.ActivityDetailCalBinding

class DetailCalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCalBinding
    private lateinit var cal: Cal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCalBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 선택된 일기 데이터 받기
        val receivedCal = intent.getSerializableExtra("cal") as? Cal
        if (receivedCal != null) {
            // 일기 데이터가 유효한 경우에만 cal 프로퍼티에 할당
            cal = receivedCal

            // 일기 내용 설정
            binding.detailContent.text = cal.content
        } else {
            Toast.makeText(applicationContext, "항목이 없습니다.", Toast.LENGTH_SHORT).show()
        }

        // 일기 내용 설정
        binding.detailContent.text = cal.content
    }
}
