
package com.example.ielts.ui.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ielts.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TargetBandActivity : AppCompatActivity() {

    private var selectedBand = 7.0
    private var selectedDate: Calendar? = null

    private lateinit var tvSelectedBand: TextView
    private lateinit var tvExamDate: TextView
    private lateinit var rgStudyTime: RadioGroup
    private lateinit var progressBar: ProgressBar
    private lateinit var tvStep: TextView
    private lateinit var tvProgressStep: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_band)

        tvSelectedBand = findViewById(R.id.tvSelectedBand)
        tvExamDate = findViewById(R.id.tvExamDate)
        rgStudyTime = findViewById(R.id.rgStudyTime)
        progressBar = findViewById(R.id.progressBar)
        tvStep = findViewById(R.id.tvStep)
        tvProgressStep = findViewById(R.id.tvProgressStep)

        findViewById<TextView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.btnPrevious).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnBand60).setOnClickListener {
            selectBand(6.0)
        }

        findViewById<Button>(R.id.btnBand65).setOnClickListener {
            selectBand(6.5)
        }

        findViewById<Button>(R.id.btnBand70).setOnClickListener {
            selectBand(7.0)
        }

        findViewById<Button>(R.id.btnBand75).setOnClickListener {
            selectBand(7.5)
        }

        findViewById<Button>(R.id.btnBand80).setOnClickListener {
            selectBand(8.0)
        }

        findViewById<Button>(R.id.btnExamDate).setOnClickListener {
            showDatePicker()
        }

        selectBand(selectedBand)
        updateProgress()

        findViewById<Button>(R.id.btnContinue).setOnClickListener {
            if (selectedDate == null) {
                Toast.makeText(
                    this,
                    "Vui lòng chọn ngày thi dự kiến.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Hoàn thành bước thiết lập hiện tại: hiển thị bước 3/3.
            progressBar.progress = 3
            tvStep.text = "Bước 3/3"
            tvProgressStep.text = "Bước 3/3"

            val studyTime = when (rgStudyTime.checkedRadioButtonId) {
                R.id.rb15Minutes -> "15 phút/ngày"
                R.id.rb30Minutes -> "30 phút/ngày"
                R.id.rb1Hour -> "1 giờ/ngày"
                R.id.rb2Hours -> "2 giờ/ngày"
                else -> "30 phút/ngày"
            }

            val dateText = selectedDate?.let {
                SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                ).format(it.time)
            } ?: "Chưa chọn ngày thi"

            val fullName = intent.getStringExtra("FULL_NAME")
                ?.takeIf { it.isNotBlank() }
                ?: "Lê Nguyễn Phương Vy"

            val email = intent.getStringExtra("EMAIL")
                ?.takeIf { it.isNotBlank() }
                ?: "lnpvy0516@gmail.com"

            val phone = intent.getStringExtra("PHONE")
                ?.takeIf { it.isNotBlank() }
                ?: "0949797399"

            val profileIntent = Intent(
                this,
                ProfileActivity::class.java
            ).apply {
                putExtra("TARGET_BAND", selectedBand)
                putExtra("EXAM_DATE", dateText)
                putExtra("STUDY_TIME", studyTime)
                putExtra("FULL_NAME", fullName)
                putExtra("EMAIL", email)
                putExtra("PHONE", phone)
            }

            startActivity(profileIntent)
        }
    }

    private fun selectBand(band: Double) {
        selectedBand = band

        tvSelectedBand.text =
            "✦ Band mục tiêu: ${String.format(Locale.US, "%.1f", band)}"

        val bandButtons = listOf(
            R.id.btnBand60 to 6.0,
            R.id.btnBand65 to 6.5,
            R.id.btnBand70 to 7.0,
            R.id.btnBand75 to 7.5,
            R.id.btnBand80 to 8.0
        )

        bandButtons.forEach { (id, value) ->
            val button = findViewById<Button>(id)
            val isSelected = value == band

            button.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(
                    if (isSelected) "#154378" else "#E4F2FA"
                )
            )

            button.setTextColor(
                Color.parseColor(
                    if (isSelected) "#FFFFFF" else "#154378"
                )
            )
        }

        // Chọn Band hoàn tất bước đầu tiên.
        updateProgress()
    }

    private fun updateProgress() {
        val step = if (selectedDate != null) 2 else 1

        progressBar.progress = step
        tvStep.text = "Bước $step/3"
        tvProgressStep.text = "Bước $step/3"
    }

    private fun showDatePicker() {
        val today = Calendar.getInstance()

        val picker = DatePickerDialog(
            this,
            { _, year, month, day ->
                val chosenDate = Calendar.getInstance().apply {
                    set(year, month, day, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                selectedDate = chosenDate

                val formatter = SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                )

                tvExamDate.text =
                    "Ngày thi dự kiến: ${formatter.format(chosenDate.time)}"

                // Sau khi chọn ngày thi, tiến trình chuyển sang 2/3.
                updateProgress()
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )

        picker.datePicker.minDate = today.timeInMillis
        picker.show()
    }
}
