package com.example.hiltapp.ui.mediafragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hiltapp.R
import com.example.hiltapp.data.entity.MediaItem
import com.example.hiltapp.data.entity.SystemUIType
import com.example.hiltapp.databinding.FragmentMediaBinding
import com.example.hiltapp.ui.MainActivitySharedViewModel
import com.example.hiltapp.ui.OptionMenuViewModel
import com.example.hiltapp.ui.ToolbarViewModel
import com.example.hiltapp.util.DeviceUtil
import com.example.hiltapp.util.GridSpaceDecoration
import com.example.hiltapp.util.PagingConstants.DEFAULT_SPAN_COUNT
import com.example.hiltapp.util.adapter.MediaAdapter
import com.example.hiltapp.util.adapter.MediaClickListener
import com.example.hiltapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MediaFragment : Fragment() {


    private val viewModel: MediaFragmentViewModel by viewModels()
    private lateinit var fetchJob: Job

    @Inject
    lateinit var optionMenuViewModel: OptionMenuViewModel

    @Inject
    lateinit var toolbarViewModel: ToolbarViewModel

    @Inject
    lateinit var systemUIEvent: SingleLiveEvent<SystemUIType>

    @Inject
    lateinit var sharedViewModel: MainActivitySharedViewModel


    private val navigateAlbumEvent = SingleLiveEvent<Unit>()


    private var _bindnig: FragmentMediaBinding? = null
    private val binding get() = _bindnig!!

    private val mediaAdapter: MediaAdapter by lazy {
        MediaAdapter(object : MediaClickListener {
            override fun itemClick(view :View, item: MediaItem, position: Int) {
                sharedViewModel.onItemClick(view, item, position )
            }

            override fun checkBoxClick(item: MediaItem) {
                sharedViewModel.onCheckBoxClick(item)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        systemUIEvent.value = SystemUIType.NORMAL
        optionMenuViewModel.setMenuRes(R.menu.sub_menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindnig = FragmentMediaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (DeviceUtil.hasPermission(requireContext())) {
            initView()
        } else {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    initView()
                    sharedViewModel.repository.invalidate()
                } else {
                    findNavController().popBackStack()
                }
            }.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }


        observeData()
    }


    private fun observeData() {
        lifecycleScope.launch {
            sharedViewModel.items.collectLatest {
                mediaAdapter.submitData(it)
            }
        }

        sharedViewModel.currentFolder.observe(viewLifecycleOwner){ album->
            toolbarViewModel.setTitle((album?.name ?: "최신항목") + " ▾").onChange()
        }

        sharedViewModel.itemClickEvent.observe(viewLifecycleOwner){ triple->

            triple?.let {
                val view = it.first
                val media = it.second
                val position = it.third

                sharedViewModel.bindingItemAdapterPosition.set(position)

                val navDirection =
                    MediaFragmentDirections.actionMediaFragmentToDetailFragment(
                        media,
                        position
                    )


                    findNavController().navigate(navDirection)

            }
        }
    }


    private fun initView() {
        scrollToPosition()

        fetchJob = viewModel.fetchData()

        toolbarViewModel.setVisible(true)
            .setNavIconRes(R.drawable.ic_baseline_arrow_back_24, R.color.white)
            .onChange()

        initRecyclerView()
        mediaAdapter.selection = sharedViewModel.selection


        bindViews()
    }



    private fun initRecyclerView() = with(binding) {
        recyclerView.apply {
            this.adapter = mediaAdapter
            layoutManager = GridLayoutManager(context, DEFAULT_SPAN_COUNT)
            addItemDecoration(GridSpaceDecoration())
            setHasFixedSize(true)
        }
    }

    private fun bindViews() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _bindnig = null
    }

    override fun onDestroy() {
        if (fetchJob.isActive) {
            fetchJob.cancel()
        }

        super.onDestroy()
    }



    private fun scrollToPosition() {
        binding.recyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                binding.recyclerView.removeOnLayoutChangeListener(this)
                val layoutManager = binding.recyclerView.layoutManager
                layoutManager?.let {
                    val currentPosition =
                        sharedViewModel.bindingItemAdapterPosition.get()
                    val viewAtPosition = layoutManager.findViewByPosition(currentPosition)
                    if (viewAtPosition == null || layoutManager.isViewPartiallyVisible(
                            viewAtPosition,
                            false,
                            true
                        )
                    ) {
                        binding.recyclerView.post { layoutManager.scrollToPosition(currentPosition) }
                    }
                }

            }
        })
    }

}