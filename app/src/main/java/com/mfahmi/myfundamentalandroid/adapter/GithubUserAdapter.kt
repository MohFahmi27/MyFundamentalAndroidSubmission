package com.mfahmi.myfundamentalandroid.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mfahmi.myfundamentalandroid.ui.DetailActivity
import com.mfahmi.myfundamentalandroid.databinding.ItemUserLayoutBinding
import com.mfahmi.myfundamentalandroid.model.User

class GithubUserAdapter(private val arrayListUser: Sequence<User>) :
    RecyclerView.Adapter<GithubUserAdapter.GithubUserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        val binding =
            ItemUserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GithubUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        holder.bindView(arrayListUser.elementAt(position))
    }

    override fun getItemCount(): Int = arrayListUser.count()

    inner class GithubUserViewHolder(private val binding: ItemUserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(user: User) {
            with(binding) {
                Glide.with(itemView.context).load(user.profilePict)
                    .apply(RequestOptions().override(80, 80)).into(imgUser)
                tvNama.text = user.name
                tvUsername.text = user.username
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DETAIL, user)
                itemView.context.startActivity(intent)
            }
        }
    }
}


