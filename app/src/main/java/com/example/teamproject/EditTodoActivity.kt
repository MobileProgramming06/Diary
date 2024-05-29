package com.example.teamproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.teamproject.DBHelper.Todo
import com.example.teamproject.DBHelper.TodoDBHelper
import com.example.teamproject.databinding.ActivityEditTodoBinding
import java.text.SimpleDateFormat

class EditTodoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditTodoBinding
    var DB: TodoDBHelper? = null
    private var todo: Todo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Add List"

        DB = TodoDBHelper(this)

        val type = intent.getStringExtra("type")
        if (type.equals("ADD"))
            binding.saveBtn.text = "추가하기"
        else {
            todo = intent.getSerializableExtra("item") as Todo?
            binding.todoTitle.setText(todo?.title)
            binding.todoContent.setText(todo?.content)
            binding.saveBtn.text = "수정하기"
        }

        binding.saveBtn.setOnClickListener {
            val title = binding.todoTitle.text.toString()
            val content = binding.todoContent.text.toString()
            val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis())
            
            if (type.equals("ADD")) {
                // 추가
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    // 데이터 저장
                    val todo = Todo(0, title, content, currentDate, false)
                    // 데이터 전송
                    val intent = Intent().apply {
                        putExtra("todo", todo)
                        putExtra("flag", 0)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
            else {
                // 수정
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    val todo = Todo(todo?.id!!, title, content, currentDate, todo?.isChecked == true)
                    val intent = Intent().apply {
                        putExtra("todo", todo)
                        putExtra("flag", 1)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}