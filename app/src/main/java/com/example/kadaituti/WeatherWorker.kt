package com.example.kadaituti


import android.annotation.SuppressLint
import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.*
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.TimeUnit

class WeatherWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    private lateinit var realm: Realm
    override fun doWork(): Result {
        //次回の起動時間をセット
        val request = OneTimeWorkRequestBuilder<WeatherWorker>().setInitialDelay(10000, TimeUnit.MILLISECONDS)
            .addTag("weatherwork")
            .build()
        WorkManager.getInstance(applicationContext).enqueue(request)

        //天気取得メソッド
    notification()
    test()
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

        val name = "タイトル"
        val descriptionText = "説明"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("1", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(1,builder.build())
    }


  @SuppressLint("SimpleDateFormat")
  @OptIn(DelicateCoroutinesApi::class)
  fun test(): Job = GlobalScope.launch {
    val lat = 33.8343
      val lon = 132.7659
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
      val daily = json.getJSONArray("daily")
     //7日分の天気を取得
        for (i in 0..6) {
            val firstObject = daily.getJSONObject(i)
            //Jsonファイルから要素抽出↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
            val weather = firstObject.getJSONArray("weather").getJSONObject(0)
            val temp = firstObject.getJSONObject("temp")
            //日付Long型
            val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
            val dt = firstObject.getLong("dt")
            val da = dateFormat.format(Date(dt*1000))
            //天気
            val descriptionText = weather.getString("description")
            //温度
            val tempText = temp.getString("day")
            //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
            //ここにDB保存処理
            realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                val date = realm.where<WeatherData>().equalTo("dt", da).findFirst()
                if (date?.dt == da) {
                    date?.weather = descriptionText
                    date?.temp = tempText


                    println("上書き")
                } else {
                    val id = realm.where<WeatherData>().max("id")
                    val nextId = (id?.toLong()?:0L) + 1L

                    val newDate = realm.createObject<WeatherData>(nextId)
                    newDate.dt = da
                    newDate.weather = descriptionText
                    newDate.temp = tempText
                    realm.insert(newDate)
                    println("新規書き込み")
                }
            }
        }
  }
}




