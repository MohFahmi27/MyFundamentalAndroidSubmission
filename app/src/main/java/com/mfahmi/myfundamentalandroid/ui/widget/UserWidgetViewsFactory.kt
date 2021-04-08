package com.mfahmi.myfundamentalandroid.ui.widget

import android.content.Context
import android.database.Cursor
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.db.DatabaseContract.Companion.CONTENT_URI
import com.mfahmi.myfundamentalandroid.db.UserFavoriteHelper
import com.mfahmi.myfundamentalandroid.helper.MappingHelper
import com.mfahmi.myfundamentalandroid.model.User

internal class UserWidgetViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private val userWidgetItems = ArrayList<User>()
    private lateinit var userFavoriteHelper: UserFavoriteHelper
    private var cursor: Cursor? = null

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        if (cursor != null) {
            cursor!!.close()
        }

        val identityToken = Binder.clearCallingIdentity()
        cursor = context.contentResolver.query(
            CONTENT_URI,
            null,
            null,
            null,
            null
        )
        userWidgetItems.addAll(MappingHelper.mapCursorToArrayList(cursor))
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDestroy() {
        userFavoriteHelper.close()
        userWidgetItems.clear()
    }

    override fun getCount(): Int = userWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)

        rv.setTextViewText(R.id.tv_username_widget, userWidgetItems[position].username)
        rv.setTextViewText(R.id.tv_user_type_widget, userWidgetItems[position].userType)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}