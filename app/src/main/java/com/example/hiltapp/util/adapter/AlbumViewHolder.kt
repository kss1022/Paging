package com.example.hiltapp.util.adapter

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.signature.MediaStoreSignature
import com.example.hiltapp.R
import com.example.hiltapp.data.entity.AlbumItem
import com.example.hiltapp.data.entity.MediaItem
import com.example.hiltapp.databinding.ViewAlbumBinding
import com.example.hiltapp.databinding.ViewMediaBinding
import com.example.hiltapp.ui.Selection
import com.example.hiltapp.util.MeasureUtil
import com.example.hiltapp.util.glide.RoundedCornersLineOverDrawTransFormation

open class AlbumViewHolder<VB : ViewBinding>(
    private val binding: ViewAlbumBinding,
    context: Context
) : RecyclerView.ViewHolder(binding.root) {

    private val folderRadius =
        MeasureUtil.getDimension(context, resId = R.dimen.pickle_folder_corner_radius)
    private val outlineWidth =
        MeasureUtil.getDimension(context, resId = R.dimen.pickle_folder_outline_width)
    private val outlineColor = ContextCompat.getColor(context, R.color.light_white)


    open fun bindData(item: AlbumItem) {
        Glide.with(binding.image)
            .load(item.getRecentMediaUri())
            .transform(
                CenterCrop(),
                RoundedCornersLineOverDrawTransFormation(
                    folderRadius.toInt(),
                    outlineWidth,
                    outlineColor
                )
            )
            .into(binding.image)

        binding.name.text = item.album.name
        binding.count.text = item.album.count.toString()
    }

    open fun bindView(item: AlbumItem, clickListener: AlbumClickListener) {
        binding.image.setOnClickListener {
            clickListener.itemClick(item)
        }
    }


}