package com.example.kadaituti

import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*



class AlarmReciver: BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {


        Log.i("a", "起動")
        val pendingIntent = PendingIntent.getActivity(
            context, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "default"
        // app name
        // app name
        val title = "タイトル"

        val currentTime = System.currentTimeMillis()
        val dataFormat = SimpleDateFormat("HH:mm:ss", Locale.JAPAN)
        val cTime: String = dataFormat.format(currentTime)

        // メッセージ　+ 11:22:331

        // メッセージ　+ 11:22:331
        val message = "時間になりました。 $cTime"


        // Notification　Channel 設定


        // Notification　Channel 設定
        val channel = NotificationChannel(
            channelId, title,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = message


        val notificationManager =
            //                (NotificationManager)context.getSystemService(NotificationManager.class);
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context!!, channelId)
            .setSmallIcon(R.drawable.btn_star)
            .setContentTitle("My notification")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManagerCompat = NotificationManagerCompat.from(
            context!!
        )

        // 通知

        // 通知
        notificationManagerCompat.notify(1, builder.build())

    }
}