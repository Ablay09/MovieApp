package com.example.movieapp.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.movieapp.R
import com.example.movieapp.presentation.MainActivity

private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(messageBody: String?, applicationContext: Context) {

    // Create intent
    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    // Create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Add style
    val notificationImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.pop_corn_watching_movie
    )

    val bigPictureStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(notificationImage)
        .bigLargeIcon(null)

    // Get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.pop_corn_watching_movie)
        .setContentText(messageBody)
        .setStyle(bigPictureStyle)
        .setLargeIcon(notificationImage)
        .setContentIntent(contentPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    // Deliver the notification
    notify(NOTIFICATION_ID, builder.build())
}