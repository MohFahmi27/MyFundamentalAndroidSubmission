package com.mfahmi.consumerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mfahmi.consumerapp.R
import com.mfahmi.consumerapp.databinding.ItemUserLayoutBinding
import com.mfahmi.consumerapp.model.User
import java.util.*

class MainUserAdapter(private val context: Context) :
    RecyclerView.Adapter<MainUserAdapter.GithubUserViewHolder>() {
    var arrayListUser = ArrayList<User>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        return GithubUserViewHolder(
            ItemUserLayoutBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        holder.bindView(arrayListUser[position])
        setAnimation(holder.itemView)
    }

    private fun setAnimation(view: View) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.recyclerview_main_anim)
        view.startAnimation(animation)
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
        }
    }
}

