package com.mfahmi.myfundamentalandroid.alarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.ui.activities.MainActivity
import com.shashank.sony.fancytoastlib.FancyToast
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val ID_DAILY = 101
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_MESSAGE = "message"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            showDailyNotification(context!!, intent.getStringExtra(EXTRA_TITLE).toString(),
                    intent.getStringExtra(EXTRA_MESSAGE).toString())
        }
    }

    fun setDailyReminder(context: Context, title: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TITLE, title)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0)
        val calendar: Calendar = Calendar.getInstance().apply{
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
        }
        alarmManager.setInexactRepeating(
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
        val channelId = "github_notification_1"
        val channelName = "github_notification"

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = TaskStackBuilder.create(context)
                .addParentStack(MainActivity::class.java)
                .addNextIntent(intent)
                .getPendingIntent(ID_DAILY, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManagerCompat =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, channelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_github)
                .setContentTitle(title)
                .setContentText(message)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        notificationManagerCompat.notify(ID_DAILY, builder.build())
    }

}