package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.databinding.FragmentDetailBinding
import com.dicoding.dicodingevent.viewmodel.MainViewModel
import com.dicoding.dicodingevent.viewmodel.ViewModelFactory

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        // SettingPreferences and dataStore have been removed, so we directly initialize ViewModel without those dependencies.
        mainViewModel =
            ViewModelProvider(this, ViewModelFactory())[MainViewModel::class.java]

        val eventId = arguments?.getInt("eventId")

        // Fetch event details if eventId is available
        if (eventId != null) {
            mainViewModel.getDetailEvent(eventId)
        }

        // Observe detailEvent LiveData and update the UI
        mainViewModel.detailEvent.observe(viewLifecycleOwner) { event ->
            binding?.apply {
                tvEventName.text = event.name
                tvOwnerName.text = event.ownerName
                tvEventTime.text = getString(R.string.event_time, event.beginTime)
                val remainingQuota = event.quota?.minus(event.registrants ?: 0) ?: 0
                tvQuota.text = getString(R.string.quota_remaining, remainingQuota)

                tvDescription.text = event.description?.let {
                    HtmlCompat.fromHtml(
                        it,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
                btnEventLink.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                    startActivity(intent)
                }
            }

            // Load media cover image using Glide
            binding?.ivMediaCover?.let {
                Glide.with(this)
                    .load(event.mediaCover)
                    .into(it)
            }
        }

        // Observe loading state and display/hide progress bar accordingly
        mainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        // Observe error messages and handle retry logic
        mainViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                binding?.apply {
                    handlingLayout.visibility = View.VISIBLE
                    tvErrorMessage.text = errorMessage
                    btnRefresh.visibility = View.VISIBLE
                    btnRefresh.setOnClickListener {
                        if (eventId != null) {
                            mainViewModel.getDetailEvent(eventId)
                        }
                    }
                }
            } else {
                binding?.handlingLayout?.visibility = View.GONE
            }
        }

        return requireNotNull(binding?.root) { "Binding is null!" }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
