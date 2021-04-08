package com.mfahmi.myfundamentalandroid.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService

class UserGithubWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        UserWidgetViewsFactory(this.applicationContext)
}