package com.mfahmi.myfundamentalandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mfahmi.myfundamentalandroid.databinding.ItemUserLayoutBinding
import com.mfahmi.myfundamentalandroid.model.User

class FollowingUserAdapter :
    RecyclerView.Adapter<FollowingUserAdapter.FollowingUserViewHolder>() {

    private val followingUsers = ArrayList<User>()

    internal fun setFollowingUsers(value: ArrayList<User>) {
        followingUsers.clear()
        followingUsers.addAll(value)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingUserViewHolder {
        return FollowingUserViewHolder(
            ItemUserLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FollowingUserViewHolder, position: Int) {
        holder.bindView(followingUsers[position])
    }

    override fun getItemCount(): Int = followingUsers.count()

    inner class FollowingUserViewHolder(private val binding: ItemUserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(user: User) {
            with(binding) {
                Glide.with(itemView.context).load(user.avatarUrl)
                    .apply(RequestOptions().override(80, 80)).into(imgUser)
                tvNama.text = user.username
                tvUsername.text = user.userType
            }
        }
    }
}