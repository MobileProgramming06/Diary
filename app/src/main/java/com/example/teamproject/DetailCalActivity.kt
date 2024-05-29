package com.example.teamproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teamproject.databinding.ActivityDetailCalBinding
import java.sql.Types.NULL

class DetailCalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCalBinding
    private lateinit var cal: Cal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // intent에서 일기 데이터 받아오기
        val receivedCal = intent.getSerializableExtra("cal") as? Cal
        if (receivedCal != null) {
            // 일기 데이터가 유효한 경우에만 cal 프로퍼티에 할당
            cal = receivedCal

            // 일기 내용 설정
            binding.detailContent.text = cal.content
        } else {
           Toast.makeText(applicationContext, "항목이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.action_edit -> {
                val intent = Intent(this, EditCalActivity::class.java).apply {
                    putExtra("cal", cal)
                }
                startActivityForResult(intent, EDIT_REQUEST_CODE)
                true
            }
            R.id.action_delete -> {
                val resultIntent = Intent().apply {
                    putExtra("calId", cal.id)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.getSerializableExtra("cal")?.let {
                val updatedCal = it as Cal
                cal = updatedCal
                binding.detailContent.text = updatedCal.content
            }
        }

    }

    companion object {
        const val EDIT_REQUEST_CODE = 1
    }

}
