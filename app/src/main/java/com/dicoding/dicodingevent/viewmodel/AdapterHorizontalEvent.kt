package com.dicoding.dicodingevent.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.CardItemHorizontalBinding
import com.dicoding.dicodingevent.utils.ListEventsDiffUtil

class AdapterHorizontalEvent(private val onItemClick: ((Int?) -> Unit)? = null) :
    ListAdapter<ListEventsItem, AdapterHorizontalEvent.HorizontalEventViewHolder>(ListEventsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalEventViewHolder {

        val binding = CardItemHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HorizontalEventViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: HorizontalEventViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    class HorizontalEventViewHolder(
        private val binding: CardItemHorizontalBinding,
        private val onItemClick: ((Int?) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: ListEventsItem) {

            binding.titleEvent.text = event.name
            Glide.with(binding.imageEvent.context)
                .load(event.imageLogo)
                .into(binding.imageEvent)

            binding.root.setOnClickListener {
                onItemClick?.invoke(event.id)
            }
        }
    }
}
