package com.example.githubuser2

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FollowPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private lateinit var loginData : String
    fun setData(items: String) {
        loginData = items
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return FollowerFragment.newInstance(position, loginData)
    }
}