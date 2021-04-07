package com.mfahmi.myfundamentalandroid.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns._ID
import com.mfahmi.myfundamentalandroid.db.DatabaseContract.Companion.TABLE_NAME
import java.sql.SQLException

class UserFavoriteHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: UserFavoriteHelper? = null
        fun getInstance(context: Context): UserFavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserFavoriteHelper(context)
            }

        private lateinit var mySqlLiteDatabase: SQLiteDatabase
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        mySqlLiteDatabase = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (mySqlLiteDatabase.isOpen) mySqlLiteDatabase.close()
    }

    fun queryAll(): Cursor =
        mySqlLiteDatabase.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )

    fun queryById(idUser: String): Cursor =
        mySqlLiteDatabase.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(idUser),
            null,
            null,
            null,
            null
        )

    fun insert(values: ContentValues): Long =
        mySqlLiteDatabase.insert(DATABASE_TABLE, null, values)

    fun delete(idUser: String): Int =
        mySqlLiteDatabase.delete(DATABASE_TABLE, "$_ID = ?", arrayOf(idUser))
}