package com.example.kadaituti


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
//ワーカーに日付ごとの天気も取得するようにして天気データを残せるようにする
class WeatherWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    private lateinit var current: Calendar

    override fun doWork(): Result {
        notification()
        //現在ミリ秒取得
        current = Calendar.getInstance()

       //予約時間ミリ秒取得
        val hour = inputData.getInt("hour",0)
        val min = inputData.getInt("min",0)

        val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY,hour)
            calendar.set(Calendar.MINUTE,min)
            calendar.set(Calendar.SECOND,0)
            calendar.add(Calendar.HOUR_OF_DAY,24)

        //(現時刻 ー 予約時刻)
        val nextTime = calendar.timeInMillis - current.timeInMillis
        current.add(Calendar.MILLISECOND,nextTime.toInt())
        //次のWorkerに渡す値
        val data = Data.Builder().apply {
            putInt("hour",calendar.get(Calendar.HOUR_OF_DAY))
            putInt("min",calendar.get(Calendar.MINUTE))
        }.build()

        //次回の起動時間をセット
        val request = OneTimeWorkRequestBuilder<WeatherWorker>()
            .setInitialDelay(nextTime, TimeUnit.MILLISECONDS)
            .addTag("weatherwork")
            .setInputData(data)
            .build()
        //Woker送信
        WorkManager.getInstance(applicationContext).enqueue(request)

        return Result.success()
    }

    fun notification(/*date: Date,pop: Double*/){
        val ic = R.drawable.ic_notifiction
        val title = "雨のお知らせ"
        val dateFormat = SimpleDateFormat("a HH", Locale.US)
        /*val houry = dateFormat.format(date)
        val rain = Math.round(pop * 100)*/
        //val main = "$houry 時から雨です。降水確率は$rain%です。"
        val main = "起動"
        val builder = NotificationCompat.Builder(applicationContext,"1")
            .setSmallIcon(ic)
            .setContentTitle(title)
            .setContentText(main)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val name = "タイトル"
        val descriptionText = "内容"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("1", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(1,builder.build())
    }


 /* @SuppressLint("SimpleDateFormat")
  @OptIn(DelicateCoroutinesApi::class)
  fun weatherGetter(): Job = GlobalScope.launch {
      val lat = 33.8343
      val lon = 132.7659
      val key = "9c5b8afab877ebe11f361113ac477602"
      val API_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&units=metric&lang=ja&APPID=$key"
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
      current = Calendar.getInstance()
      val count = current.get(Calendar.HOUR_OF_DAY)
        //12時間分の天気を取得
      for (i in count..count+12) {
          val firstObject = hourly.getJSONObject(i)
          val pop = firstObject.getDouble("pop")
          val weather = firstObject.getJSONArray("weather").getJSONObject(0)
          val weatherId = weather.getInt("id")
          val dt = firstObject.getLong("dt")
          val date = Date(dt*1000)

          //雨だった場合通知
          if(weatherId >= 616){
              notification(date,pop)
              break
          }
      }
        println("終了")
  }*/
}




