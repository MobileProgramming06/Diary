package com.example.teamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.teamproject.DBHelper.Todo
import com.example.teamproject.DBHelper.TodoDBHelper
import com.example.teamproject.databinding.ActivityTodoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoActivity : AppCompatActivity() {
    lateinit var binding: ActivityTodoBinding
    lateinit var DB: TodoDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DB 초기화
        DB = TodoDBHelper(this)
        val todos = DB.getAllTodos()

        binding.addBtn.setOnClickListener {
            var intent = Intent(this, EditTodoActivity:: class.java).apply {
                putExtra("type", "ADD")
            }
            requestActivity.launch(intent)
        }
    }

    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result -> if (result.resultCode == RESULT_OK) {
            val data = result.data
            val todo = data?.getSerializableExtra("todo") as Todo

            when (data?.getIntExtra("flag", -1)) {
                0 -> {      // 추가
                    CoroutineScope(Dispatchers.IO).launch {
                        val isSuccess = DB?.insertData(todo.title, todo.content, todo.timestamp, todo.isChecked)
                        withContext(Dispatchers.Main) {
                            if (isSuccess == true)
                                Toast.makeText(this@TodoActivity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                            else
                                Toast.makeText(this@TodoActivity, "추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                1 -> {      // 수정
                    
                }
            }
        }
    }
}