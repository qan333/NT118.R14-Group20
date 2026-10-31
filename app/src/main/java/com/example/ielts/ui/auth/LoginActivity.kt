
package com.example.ielts.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ielts.R

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnTogglePassword: TextView
    private lateinit var cbRemember: CheckBox
    private lateinit var tvForgotPassword: TextView
    private lateinit var tvRegister: TextView

    private var isPasswordVisible = false

    private val preferences by lazy {
        getSharedPreferences("ielts_login_preferences", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnTogglePassword = findViewById(R.id.btnTogglePassword)
        cbRemember = findViewById(R.id.cbRemember)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        tvRegister = findViewById(R.id.tvRegister)

        val savedEmail = preferences.getString("saved_email", "").orEmpty()
        if (savedEmail.isNotEmpty()) {
            edtEmail.setText(savedEmail)
            cbRemember.isChecked = true
        }

        btnTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }

        btnLogin.setOnClickListener {
            validateLogin()
        }

        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        findViewById<View?>(R.id.btnBack)?.setOnClickListener {
            finish()
        }

        findViewById<View?>(R.id.btnGoogle)?.setOnClickListener {
            showNotIntegratedMessage("Google")
        }

        findViewById<View?>(R.id.btnApple)?.setOnClickListener {
            showNotIntegratedMessage("Apple")
        }
    }

    private fun togglePasswordVisibility() {
        val cursorPosition = edtPassword.selectionStart.coerceAtLeast(0)
        isPasswordVisible = !isPasswordVisible

        edtPassword.inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        edtPassword.setSelection(
            cursorPosition.coerceAtMost(edtPassword.text.length)
        )

        btnTogglePassword.text = if (isPasswordVisible) "◉" else "◎"
    }

    private fun validateLogin() {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString()

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

        if (password.isEmpty()) {
            edtPassword.error = "Vui lòng nhập mật khẩu"
            edtPassword.requestFocus()
            return
        }

        preferences.edit().apply {
            if (cbRemember.isChecked) {
                putString("saved_email", email)
            } else {
                remove("saved_email")
            }
            apply()
        }

        Toast.makeText(
            this,
            "Thông tin hợp lệ. Chức năng đăng nhập cần kết nối API.",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showNotIntegratedMessage(provider: String) {
        Toast.makeText(
            this,
            "Đăng nhập bằng $provider chưa được tích hợp.",
            Toast.LENGTH_SHORT
        ).show()
    }
}
