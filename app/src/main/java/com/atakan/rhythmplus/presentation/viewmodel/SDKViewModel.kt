package com.atakan.rhythmplus.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scosche.sdk24.RhythmDevice
import com.scosche.sdk24.RhythmSDKDeviceCallback
import com.scosche.sdk24.RhythmSDKFitFileCallback
import com.scosche.sdk24.ScoscheSDK24
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SDKViewModel @Inject constructor(private val sdk: ScoscheSDK24) : ViewModel() {
    val control = sdk
    lateinit var callback: RhythmSDKDeviceCallback
    lateinit var fitFileCallback: RhythmSDKFitFileCallback
}