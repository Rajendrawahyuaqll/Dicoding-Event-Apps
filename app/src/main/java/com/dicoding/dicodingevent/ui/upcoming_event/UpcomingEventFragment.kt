package com.dicoding.dicodingevent.ui.upcoming_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentUpcomingEventBinding
import com.dicoding.dicodingevent.utils.UiHandler.handleError
import com.dicoding.dicodingevent.utils.UiHandler.showLoading
import com.dicoding.dicodingevent.viewmodel.AdapterVerticalEvent
import com.dicoding.dicodingevent.viewmodel.MainViewModel

class UpcomingEventFragment : Fragment() {

    private var _binding: FragmentUpcomingEventBinding? = null
    private val binding get() = _binding

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapterVertical: AdapterVerticalEvent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingEventBinding.inflate(inflater, container, false)

        mainViewModel =
            ViewModelProvider(this)[MainViewModel::class.java]

        setupRecyclerView()
        setupAdapter()
        observeViewModel()

        return requireNotNull(binding?.root) { "Binding is null!" }
    }

    private fun setupRecyclerView() {
        binding?.apply {
            val verticalLayout = LinearLayoutManager(requireContext())
            rvUpcomingEvent.layoutManager = verticalLayout
            val itemUpcomingEventDecoration =
                DividerItemDecoration(requireContext(), verticalLayout.orientation)
            rvUpcomingEvent.addItemDecoration(itemUpcomingEventDecoration)
        }
    }

    private fun setupAdapter() {
        adapterVertical = AdapterVerticalEvent { eventId ->
            val bundle = Bundle().apply {
                eventId?.let { putInt("eventId", it) }
            }
            findNavController().navigate(R.id.navigation_detail, bundle)
        }

        binding?.rvUpcomingEvent?.adapter = adapterVertical
    }

    private fun observeViewModel() {
        binding?.apply {
            mainViewModel.upcomingEvent.observe(viewLifecycleOwner) { listItems ->
                setUpcomingEvent(listItems)
                mainViewModel.clearErrorMessage()
            }

            mainViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it, progressBar)
            }

            mainViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                handleError(
                    isError = errorMessage != null,
                    message = errorMessage,
                    errorTextView = tvErrorMessage,
                    refreshButton = btnRefresh,
                    recyclerView = rvUpcomingEvent
                ) {
                    mainViewModel.getUpcomingEvent()
                    mainViewModel.getFinishedEvent()
                }
            }
        }
    }

    private fun setUpcomingEvent(listUpcomingEvent: List<ListEventsItem>) {
        adapterVertical.submitList(listUpcomingEvent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
