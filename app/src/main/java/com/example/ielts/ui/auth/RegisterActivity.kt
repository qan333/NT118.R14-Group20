
package com.example.ielts.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ielts.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtFullName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView
    private lateinit var tvPasswordStrength: TextView
    private lateinit var progressPasswordStrength: ProgressBar

    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtFullName = findViewById(R.id.edtFullName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        cbTerms = findViewById(R.id.cbTerms)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)
        tvPasswordStrength = findViewById(R.id.tvPasswordStrength)
        progressPasswordStrength = findViewById(R.id.progressPasswordStrength)

        btnRegister.setOnClickListener {
            validateRegister()
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        findViewById<View?>(R.id.btnBack)?.setOnClickListener {
            finish()
        }

        findViewById<View?>(R.id.btnTogglePassword)?.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            setPasswordVisibility(edtPassword, isPasswordVisible)
        }

        findViewById<View?>(R.id.btnToggleConfirmPassword)?.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            setPasswordVisibility(edtConfirmPassword, isConfirmPasswordVisible)
        }

        findViewById<View?>(R.id.btnGoogle)?.setOnClickListener {
            showNotIntegratedMessage("Google")
        }

        findViewById<View?>(R.id.btnApple)?.setOnClickListener {
            showNotIntegratedMessage("Apple")
        }

        setupPasswordStrength()
    }

    private fun setPasswordVisibility(
        editText: EditText,
        visible: Boolean
    ) {
        val cursorPosition = editText.selectionStart.coerceAtLeast(0)

        editText.inputType = if (visible) {
            InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        editText.setSelection(
            cursorPosition.coerceAtMost(editText.text.length)
        )
    }

    private fun setupPasswordStrength() {
        edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val password = s?.toString().orEmpty()
                val score = calculatePasswordStrength(password)

                progressPasswordStrength.max = 4
                progressPasswordStrength.progress = score

                tvPasswordStrength.text = when {
                    password.isEmpty() -> "Gồm chữ và số"
                    score <= 1 -> "Độ bảo mật: Yếu"
                    score == 2 -> "Độ bảo mật: Trung bình"
                    score == 3 -> "Độ bảo mật: Khá mạnh"
                    else -> "Độ bảo mật: Mạnh"
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
    }

    private fun calculatePasswordStrength(password: String): Int {
        if (password.isEmpty()) return 0

        var score = 0

        if (password.length >= 6) score++
        if (password.length >= 10) score++
        if (password.any { it.isLetter() } &&
            password.any { it.isDigit() }
        ) {
            score++
        }
        if (password.any { !it.isLetterOrDigit() }) score++

        return score.coerceIn(0, 4)
    }

    private fun validateRegister() {
        val fullName = edtFullName.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString()
        val confirmPassword = edtConfirmPassword.text.toString()

        if (fullName.isEmpty()) {
            edtFullName.error = "Vui lòng nhập họ và tên"
            edtFullName.requestFocus()
            return
        }

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

        if (password.length < 6) {
            edtPassword.error = "Mật khẩu phải có ít nhất 6 ký tự"
            edtPassword.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            edtConfirmPassword.error = "Vui lòng xác nhận mật khẩu"
            edtConfirmPassword.requestFocus()
            return
        }

        if (password != confirmPassword) {
            edtConfirmPassword.error = "Mật khẩu xác nhận không khớp"
            edtConfirmPassword.requestFocus()
            return
        }

        if (!cbTerms.isChecked) {
            Toast.makeText(
                this,
                "Vui lòng đồng ý với điều khoản sử dụng",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val intent = Intent(this, TargetBandActivity::class.java).apply {
            putExtra("FULL_NAME", fullName)
            putExtra("EMAIL", email)
        }

        startActivity(intent)
    }

    private fun showNotIntegratedMessage(provider: String) {
        Toast.makeText(
            this,
            "Đăng ký bằng $provider chưa được tích hợp.",
            Toast.LENGTH_SHORT
        ).show()
    }
}
