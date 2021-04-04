package com.mfahmi.myfundamentalandroid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mfahmi.myfundamentalandroid.databinding.ItemUserLayoutBinding
import com.mfahmi.myfundamentalandroid.model.User

class DetailUserAdapter: RecyclerView.Adapter<DetailUserAdapter.DetailUsersViewHolder>() {
    var usersDetailLists = ArrayList<User>()
       set(value) {
           field.clear()
           field.addAll(value)
           notifyDataSetChanged()
       }

    inner class DetailUsersViewHolder(private val binding: ItemUserLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(user: User) {
            with(binding) {
                Glide.with(itemView.context).load(user.avatarUrl)
                    .apply(RequestOptions().override(80, 80)).into(imgUser)
                tvNama.text = user.username
                tvUsername.text = user.userType
                imgItemArrowRight.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailUsersViewHolder {
        return DetailUsersViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DetailUsersViewHolder, position: Int) {
        holder.bindView(usersDetailLists[position])
    }

    override fun getItemCount(): Int = usersDetailLists.size
}