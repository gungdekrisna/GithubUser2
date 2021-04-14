package com.example.githubuser2.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class RoomUser(
        @PrimaryKey (autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "login") var login: String? = null,
        @ColumnInfo(name = "url") var url: String? = null,
        @ColumnInfo(name = "avatar_url") var avatar_url: String? = null,
        @ColumnInfo(name = "name") var name: String? = null,
        @ColumnInfo(name = "location") var location: String? = null,
        @ColumnInfo(name = "company") var company: String? = null,
        @ColumnInfo(name = "public_repos") var public_repos: Int? = null,
        @ColumnInfo(name = "followers") var followers: Int? = null,
        @ColumnInfo(name = "following") var following: Int? = null,
    ): Parcelable
