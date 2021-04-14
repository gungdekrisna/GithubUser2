package com.example.githubuser2

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser2.databinding.ActivityFavoriteBinding
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuser2.entity.RoomUser
import com.example.githubuser2.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityFavoriteBinding

    companion object{
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.favorite)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: RoomUser) {
                showSelectedUser(data)
            }
        })

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null){
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<RoomUser>(EXTRA_STATE)
            if (list != null){
                adapter.mData = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.mData)
    }

    private fun showSelectedUser(user: RoomUser){
        val moveIntentWithDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
        moveIntentWithDetail.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(moveIntentWithDetail)
    }

    private fun loadUserAsync(){
        GlobalScope.launch(Dispatchers.Main){
            binding.progressBar.visibility = View.VISIBLE
            val defferedNotes = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = defferedNotes.await()
            binding.progressBar.visibility = View.INVISIBLE
            if (users.size > 0){
                adapter.setData(users)
                binding.tvEmptyList.visibility = View.INVISIBLE
            } else {
                adapter.mData = ArrayList()
                binding.tvEmptyList.visibility = View.VISIBLE
            }
        }
    }
}