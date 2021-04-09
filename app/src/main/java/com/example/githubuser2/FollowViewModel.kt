package com.example.githubuser2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<UserItems>>()

    fun getUsers() : LiveData<ArrayList<UserItems>> {
        return listUsers
    }

    fun setUsers(url: String){
        val listItems = ArrayList<UserItems>()
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
                    val responseArray = JSONArray(result)

                    for (i in 0 until responseArray.length()){
                        val user = responseArray.getJSONObject(i)
                        val userItems = UserItems()
                        userItems.login = user.getString("login")
                        userItems.url = user.getString("url")
                        userItems.avatar_url = user.getString("avatar_url")
                        listItems.add(userItems)
                    }
                    listUsers.postValue(listItems)
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