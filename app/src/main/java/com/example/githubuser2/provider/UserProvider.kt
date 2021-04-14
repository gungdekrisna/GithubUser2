package com.example.githubuser2.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubuser2.db.DatabaseContract.AUTHORITY
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.example.githubuser2.db.UserDao
import com.example.githubuser2.db.UserDatabase
import com.example.githubuser2.entity.RoomUser
import com.example.githubuser2.helper.MappingHelper

class UserProvider : ContentProvider(){

    companion object{
        private const val USER = 1
        private const val USER_ID = 2
        private const val USER_USERNAME = 3
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private var userDao: UserDao? = null
        private lateinit var roomUser : RoomUser
        private var queryResult : Cursor? = null
        private var added : Long? = null
        private var deleted : Int = 0

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/username/*", USER_USERNAME)
        }
    }

    override fun onCreate(): Boolean {
        context?.let {
            val context = it
            userDao = UserDatabase.getDatabase(context).userDao()
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        queryResult = when(sUriMatcher.match(uri)){
            USER -> userDao?.queryAll()
            USER_USERNAME -> userDao?.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
        return queryResult
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        roomUser = MappingHelper.fromContentValues(values)
        added = when (USER) {
            sUriMatcher.match(uri) -> userDao?.insert(roomUser)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        if (userDao != null){
            deleted = when(USER_USERNAME){
                sUriMatcher.match(uri) -> userDao!!.deleteByUsername(uri.lastPathSegment.toString())
                else -> 0
            }
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        /*val updated: Int = when(USER_ID){
            sUriMatcher.match(uri) -> userHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated*/
        return 1
    }
}