package com.example.hiltapp.ui.detailfragment


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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.hiltapp.R
import com.example.hiltapp.data.entity.AlbumItem
import com.example.hiltapp.data.entity.MediaItem
import com.example.hiltapp.data.entity.SystemUIType
import com.example.hiltapp.databinding.FragmentAlbumBinding
import com.example.hiltapp.databinding.FragmentDetailBinding
import com.example.hiltapp.databinding.FragmentMediaBinding
import com.example.hiltapp.ui.MainActivitySharedViewModel
import com.example.hiltapp.ui.OptionMenuViewModel
import com.example.hiltapp.ui.ToolbarViewModel
import com.example.hiltapp.util.GridSpaceDecoration
import com.example.hiltapp.util.PagingConstants
import com.example.hiltapp.util.adapter.*
import com.example.hiltapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailFragmentViewModel by viewModels()


    @Inject
    lateinit var optionMenuViewModel: OptionMenuViewModel

    @Inject
    lateinit var toolbarViewModel: ToolbarViewModel

    @Inject
    lateinit var systemUIEvent: SingleLiveEvent<SystemUIType>

    @Inject
    lateinit var sharedViewModel: MainActivitySharedViewModel

    private var _bindnig: FragmentDetailBinding? = null
    private val binding get() = _bindnig!!

    private val detailAdapter: DetailAdapter by lazy {
        DetailAdapter()
    }
    private val pagerSnapHelper = PagerSnapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        systemUIEvent.value = SystemUIType.NORMAL
        optionMenuViewModel.setMenuRes(R.menu.sub_menu)


        lifecycleScope.launchWhenCreated {
            sharedViewModel.items.collectLatest { detailAdapter.submitData(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindnig = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }


    private fun initView() {
        scrollToPosition()

        toolbarViewModel.setVisible(true)
            .setTitle("앨범")
            .setNavIconRes(R.drawable.ic_baseline_arrow_back_24, R.color.white)
            .onChange()

        initRecyclerView()
        detailAdapter.selection = sharedViewModel.selection


        bindViews()
    }

    private fun observeData() {
        viewModel.checkBoxClickEvent.observe(viewLifecycleOwner) { item ->
            item?.let {
                Log.e("Clicked", item.media.name)
                sharedViewModel.selection.toggle(item.getId(), item.media)
                viewModel.isChecked.value = sharedViewModel.selection.isSelected(item.getId())
                setCheckedImage()
            }
        }
    }



    private fun initRecyclerView() = with(binding) {
        recyclerView.apply {
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addOnScrollListener(onScrollListener)
        }
        pagerSnapHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun bindViews() {
        binding.checked.setOnClickListener {
            viewModel.onCheckBoxClick()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        val currentPosition =
            (binding.recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        sharedViewModel.bindingItemAdapterPosition.set(currentPosition)

        _bindnig = null
    }


    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        fun onScrolled() {
            val currentPosition =
                (binding.recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (currentPosition != RecyclerView.NO_POSITION) {
                viewModel.currentMediaItem = detailAdapter.peek(currentPosition)
                viewModel.currentMediaItem?.let { item ->
                    viewModel.isChecked.value = sharedViewModel.selection.isSelected(item.getId())
                    setCheckedImage()
                    toolbarViewModel.setTitle("${currentPosition + 1} / ${sharedViewModel.itemCount.value}")
                        .onChange()

                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            viewModel.checkBoxEnabled.value = (newState == RecyclerView.SCROLL_STATE_IDLE)
            onScrolled()
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            onScrolled()
        }
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
                    val currentPosition = sharedViewModel.bindingItemAdapterPosition.get()
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


    private fun setCheckedImage() {
        viewModel.isChecked.value?.let { checked ->
            if (checked) {
                binding.checked.setImageResource(R.drawable.check_circle_on)
            } else {
                binding.checked.setImageResource(R.drawable.check_circle_off)
            }
        }
    }
}