package com.example.hiltapp.util.adapter

import com.example.hiltapp.data.entity.AlbumItem

interface AlbumClickListener {
    fun itemClick(item: AlbumItem)
}