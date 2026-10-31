package com.example.ielts.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ielts.R

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnExistingAccount =
            findViewById<Button>(R.id.btnExistingAccount)

        // Bắt đầu hành trình học: chuyển đến đăng ký.
        btnStart.setOnClickListener {
            openRegister()
        }

        // Đã có tài khoản: chuyển đến đăng nhập.
        btnExistingAccount.setOnClickListener {
            openLogin()
        }
    }

    private fun openLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun openRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
