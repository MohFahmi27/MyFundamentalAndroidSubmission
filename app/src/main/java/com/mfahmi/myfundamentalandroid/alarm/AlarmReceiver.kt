package com.mfahmi.myfundamentalandroid.alarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.ui.activities.MainActivity
import com.shashank.sony.fancytoastlib.FancyToast
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val ID_DAILY = 101
        const val EXTRA_TITLE = "title"
        const val EXTRA_MESSAGE = "message"
        private const val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            showDailyNotification(context!!, intent.getStringExtra(EXTRA_TITLE).toString(),
                    intent.getStringExtra(EXTRA_MESSAGE).toString())
        }
        Log.d(TAG, "onReceive: Alarm Receive")
    }

    fun setDailyReminder(context: Context, title: String, message: String) {
        Log.d(TAG, "setDailyReminder: ")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TITLE, title)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0)
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        )
        FancyToast.makeText(
                context,
                context.getString(R.string.message_daily_reminder_on),
                FancyToast.LENGTH_LONG,
                FancyToast.INFO,
                false
        ).show()
    }

    internal fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
        FancyToast.makeText(
                context,
                context.getString(R.string.message_daily_reminder_off),
                FancyToast.LENGTH_LONG,
                FancyToast.INFO,
                false
        ).show()
    }

    private fun showDailyNotification(context: Context, title: String, message: String) {
        val CHANNEL_ID = "github_notification_1"
        val CHANNEL_NAME = "github_notification"

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = TaskStackBuilder.create(context)
                .addParentStack(MainActivity::class.java)
                .addNextIntent(intent)
                .getPendingIntent(ID_DAILY, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManagerCompat =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        notificationManagerCompat.notify(ID_DAILY, builder.build())
    }

}