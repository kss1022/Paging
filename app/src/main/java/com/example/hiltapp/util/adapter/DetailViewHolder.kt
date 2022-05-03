package com.example.hiltapp.util.adapter

import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.MediaStoreSignature
import com.example.hiltapp.R
import com.example.hiltapp.data.entity.MediaItem
import com.example.hiltapp.databinding.ViewDetailBinding
import com.example.hiltapp.databinding.ViewMediaBinding
import com.example.hiltapp.ui.Selection

open class DetailViewHolder<VB : ViewBinding>(
    private val binding: ViewDetailBinding,
    private val selection: Selection?
) : RecyclerView.ViewHolder(binding.root) {


    open fun bindData(item: MediaItem) {
        Glide.with(binding.image)
            .load(item.getUri())
            .signature(
                MediaStoreSignature(
                    item.media.mimeType,
                    item.media.dateModified,
                    item.media.orientation
                )
            )
            .into(binding.image)
    }

    open fun bindView(item: MediaItem) {
    }


}