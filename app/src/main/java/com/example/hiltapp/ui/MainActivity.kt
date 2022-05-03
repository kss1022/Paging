package com.example.hiltapp.ui

import android.Manifest
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.example.hiltapp.R
import com.example.hiltapp.data.entity.SystemUIType
import com.example.hiltapp.databinding.ActivityMainBinding
import com.example.hiltapp.ui.mainfragment.MainFragment
import com.example.hiltapp.util.DeviceUtil
import com.example.hiltapp.util.MeasureUtil
import com.example.hiltapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val optionMenuViewModel:OptionMenuViewModel by viewModels()
    private val toolbarViewModel: ToolbarViewModel by viewModels()
    private val sharedViewModel: MainActivitySharedViewModel by viewModels()

    @Inject
    lateinit var systemUIEvent: SingleLiveEvent<SystemUIType>

    @Inject
    lateinit var contentObserver : ContentObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        observeData()
    }



    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { toolbarViewModel.onClick() }
        binding.toolbar.setOnClickListener {
            val navController =binding.fragmentContainer.findNavController()

            if(navController.currentDestination?.id == R.id.mediaFragment){
                navController.navigate(R.id.action_mediaFragment_to_albumFragment)
            }else if(navController.currentDestination?.id == R.id.albumFragment){
                navController.popBackStack()
            }

        }
        setToolbar()
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            toolbarViewModel.backgroundColor.value?.let { setBackgroundColor(it) }
            toolbarViewModel.navIconRes.value?.let { setNavigationIcon(it) }
            toolbarViewModel.navIconTint.value?.let { navigationIcon?.setTint(it) }
            title = toolbarViewModel.title.value
            toolbarViewModel.titleColor.value?.let { setTitleTextColor(it) }
            toolbarViewModel.visible.value?.let { isVisible = it }
        }
    }



    private fun observeData(){
        lifecycle.addObserver(sharedViewModel)
        lifecycle.addObserver(contentObserver)


        optionMenuViewModel.menuRes.observe(this){
            invalidateOptionsMenu()
        }


        toolbarViewModel.navClickEvent.observe(this){
            val navController =binding.fragmentContainer.findNavController()
            if(navController.currentDestination?.id == R.id.mainFragment){
                // nothing to do
            }else{
                navController.popBackStack()
            }
        }

        toolbarViewModel.navChangeEvent.observe(this){
            setToolbar()
        }

        systemUIEvent.observe(this) {
            when (it) {
                SystemUIType.NORMAL -> disableFullscreen()
                SystemUIType.FULLSCREEN -> enableFullscreen()
                SystemUIType.FULLSCREEN_WITHOUT_SYSTEM_UI -> hideSystemUI()
                else->Unit
            }
        }

        contentObserver.getContentChangedEvent().observe(this) {
            sharedViewModel.repository.invalidate()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return optionMenuViewModel.onCreateOptionMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return optionMenuViewModel.onOptionsItemSelected(item)
    }


    private fun disableFullscreen() {
        if (DeviceUtil.isAndroid5Later()) {
            window.apply {
                val systemUIBackgroundColor =
                    ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryDark)
                statusBarColor = systemUIBackgroundColor
                navigationBarColor =  ContextCompat.getColor(this@MainActivity, R.color.navigation_60)
                clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    private fun enableFullscreen() {
        if (DeviceUtil.isAndroid5Later()) {
            val colorRes = R.color.white
            val systemUIBackgroundColor = ContextCompat.getColor(this, colorRes)
            window.statusBarColor = systemUIBackgroundColor
            window.navigationBarColor =  ContextCompat.getColor(this@MainActivity, R.color.navigation_60)
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }

            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
    }

    private fun hideSystemUI() {
        if (DeviceUtil.isAndroid5Later()) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)

        }
    }

}