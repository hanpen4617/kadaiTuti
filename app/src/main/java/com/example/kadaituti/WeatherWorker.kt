package com.example.kadaituti


import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.*
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

class WeatherWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        //次回の起動時間をセット
        val request = OneTimeWorkRequestBuilder<WeatherWorker>().setInitialDelay(10000, TimeUnit.MILLISECONDS)
            .addTag("weatherwork")
            .build()
        WorkManager.getInstance(applicationContext).enqueue(request)

        //天気取得メソッド
    notification()
        return Result.success()
    }

    fun notification(){
        val ic = R.drawable.ic_notifiction
        val title = "タイトル"
        val main = "内容"
        val builder = NotificationCompat.Builder(applicationContext,"1")
            .setSmallIcon(ic)
            .setContentTitle(title)
            .setContentText(main)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "タイトル"
            val descriptionText = "説明"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(1,builder.build())
    }
    /*@OptIn(DelicateCoroutinesApi::class)
  fun test(): Job = GlobalScope.launch {
    val lat = 33.33
      val lon = 122.12
      val key = "9c5b8afab877ebe11f361113ac477602"
      val API_URL =
          "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&units=metric&lang=ja&APPID=$key"
      val url = URL(API_URL)
      //APIから情報を取得する.
      val br = BufferedReader(InputStreamReader(withContext(Dispatchers.IO) {
          url.openStream()
      }))
      //取得した情報を文字列に変換
      val str = br.readText()
      //json形式のデータとして識別
      val json = JSONObject(str)
      //時間別の配列を取得
      val hourly = json.getJSONArray("hourly")
      //12時間分の天気予報を取得
      for (i in 0..3) {
          val firstObject = hourly.getJSONObject(i)
          val weather = firstObject.getJSONArray("weather").getJSONObject(0)
          val descriptionText = weather.getString("description")
          withContext(Dispatchers.IO) {
              Thread.sleep(1000)
          }
          println(descriptionText)
      }
  }*/
}




