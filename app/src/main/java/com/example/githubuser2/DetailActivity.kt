package com.example.githubuser2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser2.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var loginData : String

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

        val user = intent.getParcelableExtra<UserItems>(EXTRA_USER) as UserItems?
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
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}