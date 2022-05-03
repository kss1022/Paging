package com.example.hiltapp.ui.mainfragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hiltapp.data.entity.SystemUIType
import com.example.hiltapp.databinding.FragmentMainBinding
import com.example.hiltapp.ui.MainActivitySharedViewModel
import com.example.hiltapp.ui.OptionMenuViewModel
import com.example.hiltapp.ui.ToolbarViewModel
import com.example.hiltapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel : MainFragmentViewModel by viewModels()
    private lateinit var fetchJob : Job

    @Inject
    lateinit var optionMenuViewModel: OptionMenuViewModel

    @Inject
    lateinit var toolbarViewModel: ToolbarViewModel

    @Inject
    lateinit var systemUIEvent: SingleLiveEvent<SystemUIType>

    @Inject
    lateinit var sharedViewModel: MainActivitySharedViewModel


    private var _bindnig: FragmentMainBinding? = null
    private val binding get() = _bindnig!!


    private val permissionLauncher = this.registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted->
        if(granted){
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToMediaFragment())
        }else{
            showSystemSettingDialog(requireActivity())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        optionMenuViewModel.setMenuRes(null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindnig = FragmentMainBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fetchJob = viewModel.fetchData()
        observeData()

        initView()
    }



    private fun observeData(){

    }


    private fun initView() = with(binding){
        toolbarViewModel.setVisible(false).onChange()
        systemUIEvent.value = SystemUIType.FULLSCREEN
        button.setOnClickListener {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _bindnig = null
    }

    override fun onDestroy() {
        if(fetchJob.isActive) {
            fetchJob.cancel()
        }

        super.onDestroy()
    }






    private fun showSystemSettingDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setMessage("[애플리케이션 정보 > 권한]에서 [저장소] 권한을 허용하고 다시 해보세요")
            .setPositiveButton("상세설정") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .show()
    }
}