package com.mfahmi.myfundamentalandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mfahmi.myfundamentalandroid.databinding.ItemUserLayoutBinding
import com.mfahmi.myfundamentalandroid.model.User

class FollowersUserAdapter: RecyclerView.Adapter<FollowersUserAdapter.FollowersUsersViewHolder>() {
    private val followersUsers = ArrayList<User>()

    internal fun setFollowersUsers(value: ArrayList<User>) {
        followersUsers.clear()
        followersUsers.addAll(value)
        notifyDataSetChanged()
    }

    inner class FollowersUsersViewHolder(private val binding: ItemUserLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(user: User) {
            with(binding) {
                Glide.with(itemView.context).load(user.avatarUrl)
                    .apply(RequestOptions().override(80, 80)).into(imgUser)
                tvNama.text = user.username
                tvUsername.text = user.userType
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersUsersViewHolder {
        return FollowersUsersViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FollowersUsersViewHolder, position: Int) {
        holder.bindView(followersUsers[position])
    }

    override fun getItemCount(): Int = followersUsers.size
}