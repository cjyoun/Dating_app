package com.example.datingapp.message.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.datingapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

// 유저의 토큰 정보를 받아와서 Firebase 서버로 메세지 보내라고 명령하고
// Firebase 서버에서 앱으로 메세지 보내고 앱에서는 메세지를 받고 알림을 띄워줌
class FirebaseService : FirebaseMessagingService(){

    private val TAG = "FirebaseService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    // Firebase에서 보낸 메세지 받는 부분
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // 이 방법은 Firebase console에서 메세지 보낼 때 제목과 내용
//        Log.d(TAG, "title - ${message.notification?.title.toString()} / body - ${message.notification?.body.toString()} ")
//        val title = message.notification?.title.toString()
//        val body = message.notification?.body.toString()

        // Retrofit을 이용해서 앱에서 다른 사람에게 보낼 때 제목과 내용 (NoTiModel에 담은 내용 - MyLikeListActivity에서 담음)
        val title = message.data["title"].toString()
        val body = message.data["content"].toString()

        // Firebase에서 보낸 메세지를 앱에서 notification 알람 띄워줌
        createNotificationChannel()
        sendNotification(title, body)

    }



    // Notification
    //------------------------------------------------------------------------
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Test_cChannel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(title:String, body:String){
        var builder = NotificationCompat.Builder(this, "Test_cChannel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(123,builder.build())
        }
    }
    //------------------------------------------------------------------------

}