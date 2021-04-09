package com.example.githubuser2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserItems (
    var login: String? = null,
    var url: String? = null,
    var avatar_url: String? = null,
    var name: String? = null,
    var location: String? = null,
    var company: String? = null,
    var public_repos: Int? = null,
    var followers: Int? = null,
    var following: Int? = null,
) : Parcelable