package com.example.fbpushnotification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.example.fbpushnotification"

class MyFirebaseMessagingService : FirebaseMessagingService(){

    //generate the notification
    //initialise channel id and name in the top
    //get remoteView
    //notification Manager
    //on message received
    //mp3 for custom notification sound



    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.getNotification() != null) {
            generateNotification(remoteMessage.notification!!.title!! , remoteMessage.notification!!.body!!)
        }//that !! means we are making it null safe


    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteview(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.example.fbpushnotification" , R.layout.notification)

        remoteView.setTextViewText(R.id.tvTitle, title)
        remoteView.setTextViewText(R.id.tvMessage, message)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.baseline_auto_awesome_24)

        return remoteView

    }

    fun generateNotification (title: String, message: String) {
       val intent = Intent(this , MainActivity::class.java)
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //clears all activity in activity stack and put this activity in top
        //pending intent means an intent that needs to be used in the future
       val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        //flag ome shot means we want to use this pending intent only once


        //initialise channel id and name in the top
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.baseline_auto_awesome_24).setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000)).setOnlyAlertOnce(true) //1000ms so 1 sec vibrate 1 sec relax like that
            .setContentIntent(pendingIntent)

        var mp = MediaPlayer()
        mp = MediaPlayer.create(this,R.raw.notify)
        mp.start()



        //type this and create a function with the same name above
        builder = builder.setContent(getRemoteview(title,message))

        //notification manager to build the notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)

        }
        notificationManager.notify(0, builder.build())


    }

}