package com.example.hiltapp.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.hiltapp.data.entity.MediaItem
import com.example.hiltapp.databinding.ViewDetailBinding
import com.example.hiltapp.databinding.ViewMediaBinding
import com.example.hiltapp.ui.Selection

class DetailAdapter : PagingDataAdapter<MediaItem, DetailViewHolder<ViewDetailBinding>>(diffUtil) {

    var selection: Selection? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailViewHolder<ViewDetailBinding> =
        DetailViewHolder(
            ViewDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            selection
        )

    override fun onBindViewHolder(holder: DetailViewHolder<ViewDetailBinding>, position: Int) {
        val item = getItem(position)
        item?.let {
//            selection.isSelected(item.getId())
            holder.bindData(item)
            holder.bindView(item)
        }
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MediaItem>() {
            override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
                return oldItem.media.id == newItem.media.id
            }

            override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
                return oldItem.media == newItem.media
            }
        }
    }
}