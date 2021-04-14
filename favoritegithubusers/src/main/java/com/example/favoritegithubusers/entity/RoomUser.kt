package com.example.favoritegithubusers.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomUser(
        var id: Int = 0,
        var login: String? = null,
        var url: String? = null,
        var avatar_url: String? = null,
        var name: String? = null,
        var location: String? = null,
        var company: String? = null,
        var public_repos: Int? = null,
        var followers: Int? = null,
        var following: Int? = null,
): Parcelable
