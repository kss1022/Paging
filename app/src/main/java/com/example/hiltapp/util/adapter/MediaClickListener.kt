package com.example.hiltapp.util.adapter

import android.view.View
import com.example.hiltapp.data.entity.MediaItem

interface MediaClickListener {

    fun itemClick(view : View, item: MediaItem, position: Int)
    fun checkBoxClick(item: MediaItem)
}