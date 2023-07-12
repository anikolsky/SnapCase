package com.omtorney.snapcase.common.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.omtorney.snapcase.R
import com.omtorney.snapcase.common.domain.model.Case
import com.omtorney.snapcase.common.presentation.MainActivity
import com.omtorney.snapcase.common.util.Constants.DEEPLINK_URI

class NotificationHelper(
    private val context: Context
) {

    @SuppressLint("ObsoleteSdkInt")
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = Constants.NOTIFICATION_CHANNEL_DESCRIPTION
            }
            manager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    fun createNotification(
        title: String,
        eventMessage: String,
        case: Case,
        notificationId: Int
    ) {
        val courtTitle = case.courtTitle
        val number = Uri.encode(case.number)
        val event = Uri.encode(eventMessage)
        val participants = Uri.encode(case.participants)
        val url = Uri.encode(case.url)
        val permanentUrl = Uri.encode(case.permanentUrl)
        val uid = case.uid
        val hearingDateTime = Uri.encode(case.hearingDateTime)
        val actDateForce = Uri.encode(case.actDateForce)
        val actTextUrl = Uri.encode(case.actTextUrl)

        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            (DEEPLINK_URI +
                    "?event=$event" +
                    "&number=$number" +
                    "&uid=$uid" +
                    "&url=$url" +
                    "&permanentUrl=$permanentUrl" +
                    "&courtTitle=$courtTitle" +
                    "&participants=$participants" +
                    "&hearingDateTime=$hearingDateTime" +
                    "&actDateForce=$actDateForce" +
                    "&actTextUrl=$actTextUrl").toUri(),
            context,
            MainActivity::class.java
        )

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val clickPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(clickIntent)
            getPendingIntent(0, flag)
        }

        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_round_case)
            .setContentTitle(title)
            .setContentText(eventMessage)
            .setContentIntent(clickPendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat
            .from(context)
            .notify(notificationId, notification) // Constants.NOTIFICATION_ID overwrites notifications
    }
}
