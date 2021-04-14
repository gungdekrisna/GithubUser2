package com.example.githubuser2.db

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.githubuser2.entity.RoomUser

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table")
    fun queryAll(): Cursor

    @Query("SELECT * FROM user_table WHERE login = :username")
    fun queryByUsername(username : String): Cursor

    @Insert
    fun insert(values: RoomUser): Long

    @Query("DELETE FROM user_table WHERE login = :login")
    fun deleteByUsername(login : String): Int
}