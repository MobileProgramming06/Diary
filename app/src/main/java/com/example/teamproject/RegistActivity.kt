package com.example.teamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.teamproject.DBHelper
import com.example.teamproject.LoginActivity
import com.example.teamproject.databinding.ActivityRegistBinding
import java.util.regex.Pattern

val IDPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{6,15}$"
val PWPattern = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{8,15}$"
val EngPattern = "^[A-Za-z]*$"

class RegistActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistBinding
    var DB : DBHelper? = null
    var CheckID: Boolean = false
    var CheckNick: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DB = DBHelper(this)

        // 아이디 중복 확인
        binding.checkIDBtn.setOnClickListener {
            val user = binding.editTextId.text.toString()

            if (user == "")
                Toast.makeText(this, "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            else {
                if (Pattern.matches(IDPattern, user)) {
                    val checkUsername = DB?.checkUser(user)
                    if (checkUsername == true)
                        Toast.makeText(this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
                    else {
                        CheckID = true
                        binding.editTextId.isClickable = false
                        binding.editTextId.isFocusable = false
                        Toast.makeText(this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                    Toast.makeText(this, "아이디 형식이 옳지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 닉네임 중복 확인
        binding.checkNicknameBtn.setOnClickListener {
            val nick = binding.editTextNickname.text.toString()

            if (nick == "")
                Toast.makeText(this, "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            else {
                if (Pattern.matches(EngPattern, nick)) {
                    val checkNick = DB?.checkNick(nick)
                    if (checkNick == true)
                        Toast.makeText(this, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    else {
                        CheckNick = true
                        binding.editTextNickname.isClickable = false
                        binding.editTextNickname.isFocusable = false
                        Toast.makeText(this, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                    Toast.makeText(this, "닉네임 형식이 옳지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 버튼 클릭
        binding.registBtn.setOnClickListener {
            val user = binding.editTextId.text.toString()
            val password = binding.editTextPassword.text.toString()
            val repass = binding.checkPassword.text.toString()
            val nick = binding.editTextNickname.text.toString()

            // 사용자 입력이 비었을 때
            if (user == "" || password == "" || repass == "" || nick == "")
                Toast.makeText(this, "빈 칸을 모두 입력해 주세요.", Toast.LENGTH_SHORT).show()
            else {
                // 아이디가 중복되지 않을 때
                if (CheckID == true) {
                    // 비밀번호 형식이 맞을 때
                    if (Pattern.matches(PWPattern, password)) {
                        // 비밀번호 재확인 성공
                        if (password == repass) {
                            // 닉네임 중복 확인
                            if (CheckNick == true) {
                                // 데이터 DB에 삽입
                                val insert = DB?.insertData(user, password, nick)

                                // 가입 성공
                                if (insert == true) {
                                    Toast.makeText(this, "가입되셨습니다.", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                                // 가입 실패
                                else
                                    Toast.makeText(this, "가입 실패하셨습니다.", Toast.LENGTH_SHORT).show()
                            }
                            else
                                Toast.makeText(this, "닉네임 중복확인 해주세요.", Toast.LENGTH_SHORT).show()
                        }
                        else
                            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                    else
                        Toast.makeText(this, "비밀번호를 형식에 맞게 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(this, "아이디를 형식에 맞게 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}