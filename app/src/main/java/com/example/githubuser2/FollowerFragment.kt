package com.example.githubuser2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser2.entity.RoomUser

class FollowerFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private lateinit var followViewModel: FollowViewModel
    private lateinit var rvFollows: RecyclerView
    private lateinit var progressBar: ProgressBar

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_LOGIN_NAME = "login_name"

        @JvmStatic
        fun newInstance(index: Int, login: String) =
            FollowerFragment().apply{
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(ARG_LOGIN_NAME, login)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFollows = view.findViewById(R.id.rv_follows)
        progressBar = view.findViewById(R.id.progressBar)
        showLoading(true)

        val index = arguments?.getInt(ARG_SECTION_NUMBER,0)
        val login = arguments?.getString(ARG_LOGIN_NAME)
        val textTitle: TextView = view.findViewById(R.id.tv_title)
        val tvEmptyFollow: TextView = view.findViewById(R.id.tv_empty_follow)

        val title = when(index){
            0 -> resources.getString(R.string.followers)
            1 -> resources.getString(R.string.following)
            else -> resources.getString(R.string.error)
        }

        textTitle.text = title

        followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowViewModel::class.java)
        followViewModel.getUsers().observe(viewLifecycleOwner, { userItems ->
            Log.d("cek userItem", userItems.toString())
            if (userItems.size != 0){
                adapter.setData(userItems)
                showLoading(false)
                tvEmptyFollow.visibility = View.GONE
            } else {
                showLoading(false)
                tvEmptyFollow.visibility = View.VISIBLE
            }
        })

        val follow = when(index){
            0 -> "followers"
            1 -> "following"
            else -> "error"
        }
        val url = "https://api.github.com/users/$login/$follow"

        followViewModel.setUsers(url)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        rvFollows.layoutManager = LinearLayoutManager(activity)
        rvFollows.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: RoomUser) {
                showSelectedUser(data)
            }
        })
    }

    private fun showLoading(state: Boolean){
        if (state){
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showSelectedUser(user: RoomUser){
        val moveIntentWithDetail = Intent(activity, DetailActivity::class.java)
        moveIntentWithDetail.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(moveIntentWithDetail)
    }

}