package com.example.githubuser2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser2.databinding.UserItemsBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val mData = ArrayList<UserItems>()
    fun setData(items: ArrayList<UserItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback : OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserItemsBinding.bind(itemView)
        fun bind(userItems: UserItems){
            with(itemView){
                binding.tvItemLogin.text = userItems.login
                binding.tvItemUrl.text = userItems.url
                Glide.with(itemView.context)
                    .load(userItems.avatar_url)
                    .apply(RequestOptions().override(350, 550))
                    .into(binding.imgItemAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.user_items, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
        holder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(mData[holder.adapterPosition])}
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data:UserItems)
    }
}