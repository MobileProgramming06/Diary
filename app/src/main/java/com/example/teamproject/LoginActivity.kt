package com.example.teamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teamproject.DBHelper.LoginDBHelper
import com.example.teamproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var DB: LoginDBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Login"

        DB = LoginDBHelper(this)

        binding.loginBtn.setOnClickListener {
            val user = binding.editTextId.text.toString()
            val password = binding.editTextPassword.text.toString()

            // 아이디와 비밀번호 둘 중 하나라도 빈 칸이면 Toast 메세지 출력
            if (user == "" || password == "")
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            else {
                val checkUserPassword = DB?.checkUserPassword(user, password)

                // id, password 일치 시
                if (checkUserPassword == true) {
                    Toast.makeText(this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // 회원가입 버튼 클릭 시
        binding.registBtn.setOnClickListener {
            val registIntent = Intent(this, RegistActivity:: class.java)
            startActivity(registIntent)
        }
    }
}