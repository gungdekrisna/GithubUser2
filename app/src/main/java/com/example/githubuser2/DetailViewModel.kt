package com.example.githubuser2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel() {

    val userData = MutableLiveData<ArrayList<UserItems>>()

    fun getUserDetail() : LiveData<ArrayList<UserItems>> {
        return userData
    }

    fun setUserDetail(url : String){
        val dataItems = ArrayList<UserItems>()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token dedf53cdb45f487ca6aba6c1a86a6934be9041a6")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val user = JSONObject(result)
                    val userItems = UserItems()

                    userItems.login = user.getString("login")
                    userItems.avatar_url = user.getString("avatar_url")
                    userItems.name = user.getString("name")
                    userItems.location = user.getString("location")
                    userItems.company = user.getString("company")
                    userItems.public_repos = user.getInt("public_repos")
                    userItems.followers = user.getInt("followers")
                    userItems.following = user.getInt("following")
                    dataItems.add(userItems)

                    userData.postValue(dataItems)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }
}