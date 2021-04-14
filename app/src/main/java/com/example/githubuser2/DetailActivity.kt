package com.example.githubuser2

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser2.databinding.ActivityDetailBinding
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.AVATAR_URL
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.COMPANY
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.FOLLOWERS
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.FOLLOWING
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.LOCATION
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.LOGIN
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.NAME
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.REPOSITORY
import com.example.githubuser2.db.DatabaseContract.UserColumns.Companion.URL
import com.example.githubuser2.entity.RoomUser
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var loginData : String
    private lateinit var userData : RoomUser
    private lateinit var uriWithUsername: Uri
    private var favorited : Boolean = false

    companion object {
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<RoomUser>(EXTRA_USER) as RoomUser?
        val url = user?.url.toString()
        loginData = user?.login.toString()

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        detailViewModel.getUserDetail().observe(this, { userItems ->
            if (userItems != null){
                binding.tvName.text = userItems[0].name
                binding.tvUsername.text = userItems[0].login
                binding.tvLocation.text = userItems[0].location
                binding.tvCompany.text = userItems[0].company
                binding.tvRepository.text = userItems[0].public_repos.toString()
                binding.tvFollowers.text = userItems[0].followers.toString()
                binding.tvFollowing.text = userItems[0].following.toString()
                Glide.with(this.applicationContext)
                        .load(userItems[0].avatar_url)
                        .apply(RequestOptions().override(350, 550))
                        .into(binding.ivAvatar)
                showLoading(false)
                userData = userItems[0]

                lifecycleScope.launch(Dispatchers.Default) {
                    uriWithUsername = Uri.parse(CONTENT_URI.toString() + "/username/" + loginData)
                    val cursor = contentResolver.query(uriWithUsername, null, null, null, null)
                    withContext(Dispatchers.Main) {
                        if (cursor?.count != 0) {
                            binding.fabAddFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                            favorited = true
                            cursor?.close()
                        }
                        binding.fabAddFavorite.visibility = View.VISIBLE
                    }
                }
            }
        })

        showLoading(true)
        detailViewModel.setUserDetail(url)

        val followsPagerAdapter = FollowPagerAdapter(this)
        val vpFollowPage: ViewPager2 = findViewById(R.id.vp_follow_page)
        vpFollowPage.adapter = followsPagerAdapter
        followsPagerAdapter.setData(loginData)
        val tlFollow: TabLayout = findViewById(R.id.tl_follow)
        TabLayoutMediator(tlFollow, vpFollowPage) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        binding.btnFollow.setOnClickListener {
            val followTxt = resources.getString(R.string.started_following, loginData)
            Toast.makeText(this, followTxt, Toast.LENGTH_SHORT).show()
        }

        binding.btnShare.setOnClickListener{
            val shareTxt = resources.getString(R.string.follow_on_github, loginData)
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareTxt)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.fabAddFavorite.setOnClickListener{
            if (favorited) {
                lifecycleScope.launch(Dispatchers.Default) {
                    contentResolver.delete(uriWithUsername, null, null)
                    favorited = false
                    withContext(Dispatchers.Main) {
                        showSnackBarMessage(resources.getString(R.string.delete_from_favorite))
                        binding.fabAddFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    }
                }
            } else {
                lifecycleScope.launch(Dispatchers.Default) {
                    val values = ContentValues()
                    values.put(LOGIN, userData.login)
                    values.put(URL, userData.url)
                    values.put(AVATAR_URL, userData.avatar_url)
                    values.put(NAME, userData.name)
                    values.put(LOCATION, userData.location)
                    values.put(COMPANY, userData.company)
                    values.put(FOLLOWING, userData.following)
                    values.put(FOLLOWERS, userData.followers)
                    values.put(REPOSITORY, userData.public_repos)
                    contentResolver.insert(CONTENT_URI, values)
                    favorited = true
                    withContext(Dispatchers.Main) {
                        showSnackBarMessage(resources.getString(R.string.added_to_favorite))
                        binding.fabAddFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                    }
                }
            }
        }
    }

    private fun showSnackBarMessage(message: String){
        Snackbar.make(binding.detailActivity, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}