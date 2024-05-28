package com.example.teamproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
    lateinit var todoAdapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DB 초기화
        DB = TodoDBHelper(this)

        todoAdapter = TodoAdapter(this)
        binding.todoList.layoutManager = LinearLayoutManager(this)
        binding.todoList.adapter = todoAdapter

        // 데이터베이스에서 테이터 가져와서 업데이트
        loadTodos()

        // 체크박스 클릭 리스너 설정
        todoAdapter.setItemCheckBoxClickListener(object: TodoAdapter.ItemCheckBoxClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = DB.getTodoById(itemId)
                    todo?.let {
                        it.isChecked = !it.isChecked
                        DB.update(it)
                        withContext(Dispatchers.Main) {
                            loadTodos()
                        }
                    }
                }
            }
        })

        todoAdapter.setItemClickListener(object: TodoAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int, itemId: Long) {
                Toast.makeText(this@TodoActivity, "$itemId", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = DB.getTodoById(itemId)
                    val intent = Intent(this@TodoActivity, EditTodoActivity::class.java).apply {
                        putExtra("type", "EDIT")
                        putExtra("item", todo)
                    }
                    withContext(Dispatchers.Main) {
                        requestActivity.launch(intent)
                    }
                }
            }
        })

        binding.addBtn.setOnClickListener {
            var intent = Intent(this, EditTodoActivity:: class.java).apply {
                putExtra("type", "ADD")
            }
            requestActivity.launch(intent)
        }
    }


    private fun loadTodos() {
        CoroutineScope(Dispatchers.IO).launch {
            val todos = DB.getAllTodos()
            withContext(Dispatchers.Main) {
                todoAdapter.update(todos.toMutableList())
            }
        }
    }

    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val todo = it.data?.getSerializableExtra("todo") as Todo

            when (it.data?.getIntExtra("flag", -1)) {
                0 -> {      // 추가
                    CoroutineScope(Dispatchers.IO).launch {
                        val isSuccess = DB.insertData(todo.title, todo.content, todo.timestamp, todo.isChecked)
                        withContext(Dispatchers.Main) {
                            if (isSuccess) {
                                Toast.makeText(this@TodoActivity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                                loadTodos()
                            }
                            else
                                Toast.makeText(this@TodoActivity, "추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                1 -> {      // 수정
                    CoroutineScope(Dispatchers.IO).launch {
                        DB.update(todo)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@TodoActivity, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                            loadTodos()
                        }
                    }
                }
            }
        }
    }
}