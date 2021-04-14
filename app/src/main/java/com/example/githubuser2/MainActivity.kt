package com.example.githubuser2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser2.databinding.ActivityMainBinding
import com.example.githubuser2.entity.RoomUser

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        userViewModel.getUsers().observe(this, { userItems ->
            if (userItems.size != 0){
                adapter.setData(userItems)
                showLoading(false)
                binding.tvEmptyList.visibility = View.GONE
            } else {
                showLoading(false)
                Toast.makeText(this@MainActivity, R.string.not_found, Toast.LENGTH_SHORT).show()
            }
        })

        binding.svSearchUser.queryHint = resources.getString(R.string.search_hint)
        binding.svSearchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()){
                    Toast.makeText(this@MainActivity, R.string.empty_input, Toast.LENGTH_SHORT).show()
                }
                showLoading(true)
                userViewModel.setUsers(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: RoomUser) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: RoomUser){
        val moveIntentWithDetail = Intent(this@MainActivity, DetailActivity::class.java)
        moveIntentWithDetail.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(moveIntentWithDetail)
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_favorite -> {
                val favorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(favorite)
            }
            R.id.action_settings -> {
                val settings = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(settings)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}