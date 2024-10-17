package com.dicoding.dicodingevent.utils

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.dicodingevent.data.response.ListEventsItem

class ListEventsDiffUtil : DiffUtil.ItemCallback<ListEventsItem>() {
    override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
        return oldItem == newItem
    }
}
