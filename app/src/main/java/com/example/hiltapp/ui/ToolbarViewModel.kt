package com.example.hiltapp.ui

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.hilt.Assisted
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.hiltapp.R
import com.example.hiltapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ToolbarViewModel @Inject constructor(
    private val app: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {


    companion object {
        private const val KEY_TITLE = "title"
        private const val KEY_TITLE_COLOR = "titleColor"
        private const val KEY_BACKGROUND_COLOR = "backgroundColor"
        private const val KEY_VISIBLE = "visible"
        private const val KEY_NAV_ICON_RES = "navIconRes"
        private const val KEY_NAV_ICON_TINT = "navIconTint"
    }

    var title: LiveData<CharSequence> = savedStateHandle.getLiveData(KEY_TITLE, app.getString(R.string.app_name))
    var titleColor: LiveData<Int> = savedStateHandle.getLiveData(KEY_TITLE_COLOR, ContextCompat.getColor(app, R.color.white))
    var backgroundColor: LiveData<Int> = savedStateHandle.getLiveData(KEY_BACKGROUND_COLOR, ContextCompat.getColor(app, R.color.colorPrimaryDark))
    var visible: LiveData<Boolean> = savedStateHandle.getLiveData(KEY_VISIBLE, true)
    var navIconRes: LiveData<Int> = savedStateHandle.getLiveData(KEY_NAV_ICON_RES, R.drawable.toolbar_icon)
    var navIconTint: LiveData<Int?> = savedStateHandle.getLiveData(KEY_NAV_ICON_TINT)

    val navClickEvent = SingleLiveEvent<Unit>()
    val navChangeEvent = SingleLiveEvent<Unit>()

    fun setTitle(charSequence: CharSequence): ToolbarViewModel {
        savedStateHandle.set(KEY_TITLE, charSequence)
        return this
    }

    fun setTitleColor(color: Int): ToolbarViewModel {
        savedStateHandle.set(KEY_TITLE_COLOR, color)
        return this
    }

    fun setBackgroundColor(color: Int): ToolbarViewModel {
        savedStateHandle.set(KEY_BACKGROUND_COLOR, color)
        return this
    }

    fun setVisible(visible: Boolean): ToolbarViewModel {
        savedStateHandle.set(KEY_VISIBLE, visible)
        return this
    }

    fun setNavIconRes(iconRes: Int, tintColor: Int? = null): ToolbarViewModel {
        savedStateHandle.set(KEY_NAV_ICON_RES, iconRes)
        savedStateHandle.set(KEY_NAV_ICON_TINT, tintColor)
        return this
    }

    fun setDefaultNavIcon(){
        setNavIconRes(R.drawable.ic_baseline_arrow_back_24, R.color.white)
    }

    fun onClick(){
        navClickEvent.call()
    }

    fun onChange(){
        navChangeEvent.call()
    }
}