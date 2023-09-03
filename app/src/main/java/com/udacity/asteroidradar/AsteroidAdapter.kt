package com.udacity.asteroidradar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemBinding

class AsteroidAdapter (private val clickListener: AsteroidClickListener) : ListAdapter<Asteroid,AsteroidAdapter.ViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val asteroid = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClickListener(asteroid)
        }
        holder.bind(asteroid,clickListener)
    }

    class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
       fun bind(asteroid: Asteroid,clickListener : AsteroidClickListener){
           binding.asteroid = asteroid
           binding.onAsteroidClickListener = clickListener
           binding.executePendingBindings()
       }

    }


    class DiffUtilCallBack : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id

        }

    }

    class AsteroidClickListener(val clickListener: (asteroid : Asteroid) -> Unit) {
        fun onClickListener(asteroid: Asteroid) = clickListener(asteroid)
    }

}