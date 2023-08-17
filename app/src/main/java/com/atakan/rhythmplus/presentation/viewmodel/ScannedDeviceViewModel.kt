package com.atakan.rhythmplus.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.scosche.sdk24.RhythmDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScannedDeviceViewModel @Inject constructor() : ViewModel() {

    private val _scannedDevices = mutableStateListOf<RhythmDevice>()
    val scannedDevices: List<RhythmDevice> = _scannedDevices

    fun handleDeviceFound(device: RhythmDevice) {
        if (device.name != null && !scannedDevices.any { it.name == device.name }) {
            _scannedDevices.add(0, device)
        }
    }
}
