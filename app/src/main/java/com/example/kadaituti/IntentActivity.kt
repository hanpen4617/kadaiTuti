package com.example.kadaituti

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.kadaituti.databinding.ActivityIntentBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class IntentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntentBinding
    val cal = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.timeSetButton.setOnClickListener {
            showDialog()
        }

    binding.send.setOnClickListener {
        val intent = Intent(applicationContext, AlarmReciver::class.java)
        val pending = PendingIntent.getBroadcast(
            applicationContext, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val am = getSystemService(ALARM_SERVICE) as AlarmManager
        if (am != null) {
            am.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pending)
            println("ブロキャス送信")
            val currentDate = Calendar.getInstance()
            val timeDiff = cal.timeInMillis - currentDate.timeInMillis

            val request = OneTimeWorkRequestBuilder<WeatherWorker>().setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .addTag("weatherwork")
                .build()
            WorkManager.getInstance(this).enqueue(request)
        }
    }

        binding.stop.setOnClickListener{
            WorkManager.getInstance(this).cancelAllWorkByTag("weatherwork")
        }

    }

    fun showDialog(){
        //リスナーの設定
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, min ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, min)
            cal.set(Calendar.SECOND, 0)
            //EditTextに選択された時間を設定
            binding.timeText.setText(SimpleDateFormat("HH:mm").format(cal.time))
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE), true).show()

        }
    }
