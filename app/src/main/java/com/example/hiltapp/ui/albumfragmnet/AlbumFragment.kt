package com.example.hiltapp.ui.albumfragmnet


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hiltapp.R
import com.example.hiltapp.data.entity.AlbumItem
import com.example.hiltapp.data.entity.MediaItem
import com.example.hiltapp.data.entity.SystemUIType
import com.example.hiltapp.data.repository.AlbumRepositoryImpl
import com.example.hiltapp.databinding.FragmentAlbumBinding
import com.example.hiltapp.databinding.FragmentMediaBinding
import com.example.hiltapp.ui.MainActivitySharedViewModel
import com.example.hiltapp.ui.OptionMenuViewModel
import com.example.hiltapp.ui.ToolbarViewModel
import com.example.hiltapp.util.GridSpaceDecoration
import com.example.hiltapp.util.PagingConstants
import com.example.hiltapp.util.adapter.AlbumAdapter
import com.example.hiltapp.util.adapter.AlbumClickListener
import com.example.hiltapp.util.adapter.MediaAdapter
import com.example.hiltapp.util.adapter.MediaClickListener
import com.example.hiltapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private val viewModel: AlbumFragmentViewModel by viewModels()
    private lateinit var fetchJob: Job


    @Inject
    lateinit var optionMenuViewModel: OptionMenuViewModel

    @Inject
    lateinit var toolbarViewModel: ToolbarViewModel

    @Inject
    lateinit var systemUIEvent: SingleLiveEvent<SystemUIType>

    @Inject
    lateinit var sharedViewModel: MainActivitySharedViewModel

    @Inject
    lateinit var  repository : AlbumRepositoryImpl

    private var _bindnig: FragmentAlbumBinding? = null
    private val binding get() = _bindnig!!

    private val albumAdapter: AlbumAdapter by lazy {
        AlbumAdapter(requireContext(), object : AlbumClickListener {
            override fun itemClick(item: AlbumItem) {
                sharedViewModel.setBucketId(item?.album)
                findNavController().navigateUp()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        systemUIEvent.value = SystemUIType.NORMAL
        optionMenuViewModel.setMenuRes(R.menu.sub_menu)
        viewModel.repository = repository
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindnig = FragmentAlbumBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }


    private fun initView() {
        fetchJob = viewModel.fetchData()

        toolbarViewModel.setVisible(true)
            .setTitle("앨범")
            .setNavIconRes(R.drawable.ic_baseline_arrow_back_24, R.color.white)
            .onChange()

        initRecyclerView()

        bindViews()
    }

    private fun observeData() {
        viewModel.items.observe(viewLifecycleOwner) {
            albumAdapter.submitList(it)
        }

    }

    private fun initRecyclerView() = with(binding) {
        recyclerView.apply {
            this.adapter = albumAdapter
            layoutManager = GridLayoutManager(context, PagingConstants.DEFAULT_SPAN_COUNT)
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


}