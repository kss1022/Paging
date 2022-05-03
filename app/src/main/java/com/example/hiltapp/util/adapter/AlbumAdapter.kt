package com.example.hiltapp.util.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.hiltapp.data.entity.AlbumItem
import com.example.hiltapp.data.entity.MediaItem
import com.example.hiltapp.databinding.ViewAlbumBinding
import com.example.hiltapp.databinding.ViewMediaBinding
import com.example.hiltapp.ui.Selection

class AlbumAdapter(private val context : Context, private val clickListener: AlbumClickListener) :
    ListAdapter<AlbumItem, AlbumViewHolder<ViewAlbumBinding>>(diffUtil) {

    var selection: Selection? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumViewHolder<ViewAlbumBinding> =
        AlbumViewHolder(
            ViewAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            context
        )

    override fun onBindViewHolder(holder: AlbumViewHolder<ViewAlbumBinding>, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bindData(item)
            holder.bindView(item, clickListener)
        }
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<AlbumItem>() {
            override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
                return oldItem.album.bucketId == newItem.album.bucketId
            }

            override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
                return oldItem.album == newItem.album
            }
        }
    }


}