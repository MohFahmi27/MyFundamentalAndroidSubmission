package com.mfahmi.myfundamentalandroid.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mfahmi.myfundamentalandroid.R
import com.mfahmi.myfundamentalandroid.databinding.ItemUserLayoutBinding
import com.mfahmi.myfundamentalandroid.model.User
import com.mfahmi.myfundamentalandroid.ui.activities.DetailActivity
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
        val binding =
            ItemUserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GithubUserViewHolder(binding)
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
            itemView.setOnClickListener(
                CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            val intent = Intent(itemView.context, DetailActivity::class.java)
                            intent.putExtra(DetailActivity.EXTRA_DETAIL, user)
                            itemView.context.startActivity(intent)
                        }
                    })
            )
        }
    }
}

class CustomOnItemClickListener(
    private val position: Int,
    private val onItemClickCallback: OnItemClickCallback
) : View.OnClickListener {

    interface OnItemClickCallback {
        fun onItemClicked(view: View, position: Int)
    }

    override fun onClick(v: View?) {
        onItemClickCallback.onItemClicked(v!!, position)
    }
}

