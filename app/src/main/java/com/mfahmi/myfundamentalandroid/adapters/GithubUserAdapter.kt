package com.mfahmi.myfundamentalandroid.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mfahmi.myfundamentalandroid.databinding.ItemUserLayoutBinding
import com.mfahmi.myfundamentalandroid.model.User
import com.mfahmi.myfundamentalandroid.ui.activities.DetailActivity

class GithubUserAdapter :
    RecyclerView.Adapter<GithubUserAdapter.GithubUserViewHolder>() {
    private val arrayListUser = ArrayList<User>()

    fun setArrayListUser(value: ArrayList<User>) {
        arrayListUser.clear()
        arrayListUser.addAll(value)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        val binding =
            ItemUserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GithubUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        holder.bindView(arrayListUser[position])
    }

    override fun getItemCount(): Int = arrayListUser.size

    inner class GithubUserViewHolder(private val binding: ItemUserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(user: User) {
            with(binding) {
                Glide.with(itemView.context).load(user.avatarUrl)
                    .apply(RequestOptions().override(80, 80)).into(imgUser)
                tvNama.text = user.username
                tvUsername.text = user.userType
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DETAIL, user)
                itemView.context.startActivity(intent)
            }
        }
    }
}


