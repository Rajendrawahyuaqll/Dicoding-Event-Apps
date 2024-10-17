package com.dicoding.dicodingevent.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.Event
import com.dicoding.dicodingevent.data.response.EventDetailResponse
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvent: LiveData<List<ListEventsItem>> = _upcomingEvent

    private val _finishedEvent = MutableLiveData<List<ListEventsItem>>()
    val finishedEvent: LiveData<List<ListEventsItem>> = _finishedEvent

    private val _detailEvent = MutableLiveData<Event>()
    val detailEvent: LiveData<Event> = _detailEvent

    private val _searchEvent = MutableLiveData<List<ListEventsItem>>()
    val searchEvent: LiveData<List<ListEventsItem>> = _searchEvent

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        getUpcomingEvent()
        getFinishedEvent()
    }

    fun getUpcomingEvent() {
        _isLoading.value = true
        val getUpcomingEvent = ApiConfig.getApiService().getAllActiveEvent()

        getUpcomingEvent.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _upcomingEvent.value = response.body()?.listEvents
                    clearErrorMessage()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load data from API"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _errorMessage.value = "No internet connection or server error"
            }
        })
    }

    fun getFinishedEvent() {
        _isLoading.value = true
        val getFinishedEvent = ApiConfig.getApiService().getAllFinishedEvent()

        getFinishedEvent.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _finishedEvent.value = response.body()?.listEvents
                    clearErrorMessage()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load data from API"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _errorMessage.value = "No internet connection or server error"
            }
        })
    }

    fun getDetailEvent(id: Int) {
        _isLoading.value = true
        val getDetailEvent = ApiConfig.getApiService().getDetailEvent(id)

        getDetailEvent.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(call: Call<EventDetailResponse>, response: Response<EventDetailResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailEvent.value = response.body()?.event
                    clearErrorMessage()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load data from API"
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _errorMessage.value = "No internet connection or server error"
            }
        })
    }

    fun searchEvent(keyword: String) {
        _isLoading.value = true
        val getSearchEvent = ApiConfig.getApiService().searchEvent(keyword)

        getSearchEvent.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _searchEvent.value = response.body()?.listEvents
                    clearErrorMessage()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _errorMessage.value = "Failed to load data from API"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _errorMessage.value = "No internet connection or server error"
            }
        })
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
