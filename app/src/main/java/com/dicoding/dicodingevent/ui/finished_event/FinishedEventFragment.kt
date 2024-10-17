package com.dicoding.dicodingevent.ui.finished_event

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
import com.dicoding.dicodingevent.databinding.FragmentFinishedEventBinding
import com.dicoding.dicodingevent.viewmodel.AdapterVerticalEvent
import com.dicoding.dicodingevent.viewmodel.MainViewModel

class FinishedEventFragment : Fragment() {

    private var _binding: FragmentFinishedEventBinding? = null
    private val binding get() = _binding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapterVertical: AdapterVerticalEvent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedEventBinding.inflate(inflater, container, false)

        // Inisialisasi ViewModel tanpa SettingPreferences dan dataStore
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupSearchView()
        setupRecyclerView()
        observeViewModel()

        return requireNotNull(binding?.root) { "Binding is null!" }
    }

    private fun setupSearchView() {
        binding?.apply {
            searchView.setupWithSearchBar(binding?.searchBar)

            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val keyword = searchView.text.toString()
                mainViewModel.searchEvent(keyword)

                val currentText = searchView.text

                searchView.hide()

                searchView.editText.text = currentText

                true
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.apply {
            val verticalLayout = LinearLayoutManager(requireContext())
            rvFinishedEvent.layoutManager = verticalLayout
            val itemFinishedEventDecoration =
                DividerItemDecoration(requireContext(), verticalLayout.orientation)
            rvFinishedEvent.addItemDecoration(itemFinishedEventDecoration)
            adapterVertical = AdapterVerticalEvent { eventId ->
                val bundle = Bundle().apply {
                    if (eventId != null) {
                        putInt("eventId", eventId)
                    }
                }
                findNavController().navigate(R.id.navigation_detail, bundle)
            }
            rvFinishedEvent.adapter = adapterVertical
        }
    }

    private fun observeViewModel() {
        mainViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }

        mainViewModel.finishedEvent.observe(viewLifecycleOwner) { listItems ->
            adapterVertical.submitList(listItems)
        }

        mainViewModel.searchEvent.observe(viewLifecycleOwner) { listItems ->
            adapterVertical.submitList(listItems)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
