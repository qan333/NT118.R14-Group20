package com.example.ielts.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ielts.R

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var btnSendReset: Button
    private lateinit var tvBackToLogin: TextView
    private lateinit var tvResendEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        edtEmail = findViewById(R.id.edtEmail)
        btnSendReset = findViewById(R.id.btnSendReset)
        tvBackToLogin = findViewById(R.id.tvBackToLogin)
        tvResendEmail = findViewById(R.id.tvResendEmail)

        btnSendReset.setOnClickListener {
            validateEmail()
        }

        tvResendEmail.setOnClickListener {
            validateEmail()
        }

        tvBackToLogin.setOnClickListener {
            finish()
        }

        findViewById<View?>(R.id.btnBack)?.setOnClickListener {
            finish()
        }
    }

    private fun validateEmail() {
        val email = edtEmail.text.toString().trim()

        if (email.isEmpty()) {
            edtEmail.error = "Vui lòng nhập email"
            edtEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.error = "Email không đúng định dạng"
            edtEmail.requestFocus()
            return
        }

        Toast.makeText(
            this,
            "Email hợp lệ. Chức năng gửi liên kết cần kết nối API.",
            Toast.LENGTH_LONG
        ).show()
    }
}
