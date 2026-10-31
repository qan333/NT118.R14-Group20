
package com.example.ielts.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ielts.R

class ProfileActivity : AppCompatActivity() {

    private lateinit var edtProfileName: EditText
    private lateinit var edtProfileEmail: EditText
    private lateinit var edtProfilePhone: EditText

    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileEmail: TextView

    private lateinit var tvProfileTargetBand: TextView
    private lateinit var tvProfileExamDate: TextView
    private lateinit var tvProfileStudyTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Ánh xạ các thành phần hồ sơ.
        edtProfileName = findViewById(R.id.edtProfileName)
        edtProfileEmail = findViewById(R.id.edtProfileEmail)
        edtProfilePhone = findViewById(R.id.edtProfilePhone)

        tvProfileName = findViewById(R.id.tvProfileName)
        tvProfileEmail = findViewById(R.id.tvProfileEmail)

        tvProfileTargetBand = findViewById(R.id.tvProfileTargetBand)
        tvProfileExamDate = findViewById(R.id.tvProfileExamDate)
        tvProfileStudyTime = findViewById(R.id.tvProfileStudyTime)

        // Nhận dữ liệu từ màn hình Target Band.
        val band = intent.getDoubleExtra("TARGET_BAND", 7.0)

        val examDate = intent.getStringExtra("EXAM_DATE")
            ?: "Chưa chọn ngày thi"

        val studyTime = intent.getStringExtra("STUDY_TIME")
            ?: "30 phút/ngày"

        // Nếu không có dữ liệu từ màn hình trước,
        // dùng thông tin hồ sơ ban đầu đã được yêu cầu.
        val fullName = intent.getStringExtra("FULL_NAME")
            ?.takeIf { it.isNotBlank() }
            ?: "Lê Nguyễn Phương Vy"

        val email = intent.getStringExtra("EMAIL")
            ?.takeIf { it.isNotBlank() }
            ?: "lnpvy0516@gmail.com"

        val phone = intent.getStringExtra("PHONE")
            ?.takeIf { it.isNotBlank() }
            ?: "0949797399"

        // Hiển thị thông tin ở phần tóm tắt và các ô nhập.
        edtProfileName.setText(fullName)
        edtProfileEmail.setText(email)
        edtProfilePhone.setText(phone)

        tvProfileName.text = fullName
        tvProfileEmail.text = email

        // Hiển thị mục tiêu học tập.
        tvProfileTargetBand.text =
            "🏆 Band mục tiêu: %.1f Overall".format(band)

        tvProfileExamDate.text =
            "▣ Ngày thi dự kiến: $examDate"

        tvProfileStudyTime.text =
            "◷ Mục tiêu học mỗi ngày: $studyTime"

        findViewById<TextView>(R.id.tvCurrentBand).text =
            "◎ Band hiện tại: 6.5"

        // Quay lại màn hình trước.
        findViewById<TextView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Lưu thông tin hồ sơ.
        findViewById<Button>(R.id.btnSaveProfile).setOnClickListener {
            saveProfile()
        }

        // Cài đặt ngôn ngữ.
        findViewById<TextView>(R.id.btnLanguage).setOnClickListener {
            Toast.makeText(
                this,
                "Chức năng đổi ngôn ngữ sẽ được bổ sung sau.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Cài đặt quyền micro.
        findViewById<TextView>(R.id.btnMicrophone).setOnClickListener {
            Toast.makeText(
                this,
                "Phần quyền micro sẽ được xử lý khi tích hợp luyện Speaking.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Đổi mật khẩu.
        findViewById<TextView>(R.id.btnChangePassword).setOnClickListener {
            startActivity(
                Intent(this, ForgotPasswordActivity::class.java)
            )
        }

        // Đăng xuất và quay về màn hình chào mừng.
        findViewById<TextView>(R.id.btnLogout).setOnClickListener {
            val logoutIntent = Intent(
                this,
                WelcomeActivity::class.java
            )

            logoutIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(logoutIntent)
            finish()
        }

        // Bật/tắt nhắc học.
        findViewById<Switch>(R.id.swNotifications)
            .setOnCheckedChangeListener { _, isChecked ->

                val message = if (isChecked) {
                    "Đã bật tùy chọn nhắc học."
                } else {
                    "Đã tắt tùy chọn nhắc học."
                }

                Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        // Bật/tắt lựa chọn giao diện tối.
        // Hiện tại chỉ là tùy chọn giao diện, chưa đổi theme toàn ứng dụng.
        findViewById<Switch>(R.id.swDarkMode)
            .setOnCheckedChangeListener { _, isChecked ->

                val message = if (isChecked) {
                    "Đã bật lựa chọn chế độ ban đêm."
                } else {
                    "Đã tắt lựa chọn chế độ ban đêm."
                }

                Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun saveProfile() {
        val name = edtProfileName.text.toString().trim()
        val email = edtProfileEmail.text.toString().trim()
        val phone = edtProfilePhone.text.toString().trim()

        // Kiểm tra họ tên.
        if (name.isEmpty()) {
            edtProfileName.error = "Vui lòng nhập họ và tên"
            edtProfileName.requestFocus()
            return
        }

        // Kiểm tra email.
        if (
            email.isEmpty() ||
            !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        ) {
            edtProfileEmail.error = "Vui lòng nhập email hợp lệ"
            edtProfileEmail.requestFocus()
            return
        }

        // Kiểm tra số điện thoại.
        if (phone.count { it.isDigit() } < 9) {
            edtProfilePhone.error = "Số điện thoại chưa hợp lệ"
            edtProfilePhone.requestFocus()
            return
        }

        // Cập nhật phần tóm tắt trên màn hình.
        tvProfileName.text = name
        tvProfileEmail.text = email

        // Hiện tại chưa lưu vào API hoặc cơ sở dữ liệu.
        Toast.makeText(
            this,
            "Thông tin hợp lệ! Chức năng lưu hồ sơ qua API sẽ bổ sung sau.",
            Toast.LENGTH_LONG
        ).show()
    }
}
