package com.example.githubuser2.helper

import android.content.ContentValues
import android.database.Cursor
import com.example.githubuser2.db.DatabaseContract
import com.example.githubuser2.entity.RoomUser

object MappingHelper {
    private lateinit var userData: RoomUser

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<RoomUser>{
        val userList = ArrayList<RoomUser>()

        userCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOGIN))
                val url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.URL))
                val avatar_url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
                userList.add(RoomUser(id, login, url, avatar_url))
            }
        }
        return userList
    }

    fun mapCursorToObject(usersCursor: Cursor?): RoomUser {
        var user = RoomUser()
        usersCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
            val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOGIN))
            val url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.URL))
            val avatar_url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
            user = RoomUser(id, login, url, avatar_url)
        }
        return user
    }

    fun fromContentValues(values: ContentValues?): RoomUser {
        userData = RoomUser()
        userData.login = values?.getAsString(DatabaseContract.UserColumns.LOGIN)
        userData.url = values?.getAsString(DatabaseContract.UserColumns.URL)
        userData.avatar_url = values?.getAsString(DatabaseContract.UserColumns.AVATAR_URL)
        userData.name = values?.getAsString(DatabaseContract.UserColumns.NAME)
        userData.location = values?.getAsString(DatabaseContract.UserColumns.LOCATION)
        userData.company = values?.getAsString(DatabaseContract.UserColumns.COMPANY)
        userData.following = values?.getAsInteger(DatabaseContract.UserColumns.FOLLOWING)
        userData.followers = values?.getAsInteger(DatabaseContract.UserColumns.FOLLOWERS)
        userData.public_repos = values?.getAsInteger(DatabaseContract.UserColumns.REPOSITORY)
        return userData
    }
}