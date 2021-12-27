package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding

class MainAdapter(private val clickListener: AsteroidListeners) :
    ListAdapter<Asteroid, MainAdapter.ViewHolder>(AsteroidDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) as Asteroid
        holder.bind(item, clickListener)
    }
    
    /**
     * View holder class for the list item.
     */
    class ViewHolder(private val binding: ListItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
        
        fun bind(asteroid: Asteroid, clickListener: AsteroidListeners) {
            binding.asteroid = asteroid
            binding.clickListener = clickListener
            
            //Always used.
            binding.executePendingBindings()
        }
    }
    
    /**
     * Callback for calculating the diff between two non-null items in a list.
     *
     * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
     * list that's been passed to `submitList`.
     */
    class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }
    
    class AsteroidListeners(val clickListeners: (asteroidId: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListeners(asteroid)
    }
}