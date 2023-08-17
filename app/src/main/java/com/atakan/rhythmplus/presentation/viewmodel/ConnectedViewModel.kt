package com.atakan.rhythmplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atakan.rhythmplus.data.ProvidedInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConnectedViewModel  @Inject constructor() : ViewModel(){
    private val _connected = MutableLiveData<ProvidedInfo>()
    val connected: LiveData<ProvidedInfo> = _connected

    fun updateVal(heartRate: String? = connected.value?.heartRate, batteryLevel: Int? = connected.value?.batteryLevel, firmwareVersion: String? = connected.value?.firmwareVersion){
        val newProvided = ProvidedInfo(
            heartRate?.toString() ?: "null",
            batteryLevel?.toInt() ?: -1,
            firmwareVersion?.toString() ?: "null"
        )
        _connected.postValue(newProvided)
    }
}