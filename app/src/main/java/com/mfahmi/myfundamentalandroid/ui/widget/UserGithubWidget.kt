package com.mfahmi.myfundamentalandroid.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.mfahmi.myfundamentalandroid.R
import com.shashank.sony.fancytoastlib.FancyToast

class UserGithubWidget : AppWidgetProvider() {

    companion object {
        private const val TOAST_ACTION = "com.mfahmi.myfundamentalandroid.TOAST_ACTION"

        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, UserGithubWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.user_github_widget)
            views.setRemoteAdapter(R.id.list_widget, intent)
            views.setEmptyView(R.id.list_widget, R.id.empty_view)

            val toastIntent = Intent(context, UserGithubWidgetService::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val toastPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.list_widget, toastPendingIntent)
            appWidgetManager.notifyAppWidgetViewDataChanged(
                appWidgetId,
                R.layout.user_github_widget
            )
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        FancyToast.makeText(
            context,
            context!!.resources.getString(R.string.widget_remove_message),
            FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false
        ).show()
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        FancyToast.makeText(
            context,
            context!!.resources.getString(R.string.widget_enable_message),
            FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false
        ).show()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent!!.action.let {
            if (intent.action == TOAST_ACTION) {
                FancyToast.makeText(
                    context,
                    context!!.resources.getString(R.string.favorite_user),
                    FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false
                ).show()
            }
        }
        super.onReceive(context, intent)
    }
}