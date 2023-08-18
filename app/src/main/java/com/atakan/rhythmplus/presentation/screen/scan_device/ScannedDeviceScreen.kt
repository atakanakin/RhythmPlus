package com.atakan.rhythmplus.presentation.screen.scan_device

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.atakan.rhythmplus.presentation.Screen
import com.atakan.rhythmplus.presentation.screen.scan_device.components.ScannedDeviceList
import com.atakan.rhythmplus.presentation.viewmodel.SDKViewModel
import com.atakan.rhythmplus.presentation.viewmodel.ScannedDeviceViewModel
import com.atakan.rhythmplus.service.RhythmPlusService
import com.scosche.sdk24.RhythmSDKDeviceCallback
import com.scosche.sdk24.RhythmSDKFitFileCallback

@Composable
fun ScannedDeviceScreen(viewModel: ScannedDeviceViewModel = hiltViewModel(),
                        sdk: SDKViewModel = hiltViewModel(),
                        callback: RhythmSDKDeviceCallback,
                        fitFileCallback: RhythmSDKFitFileCallback,
                        navController: NavController,
                        context: Context
) {

    val scannedDevices = viewModel.scannedDevices
    Column {
        ScannedDeviceList(devices = scannedDevices.toList()) { device ->
            // Handle device click here
            // You can connect the device or update UI as needed
            sdk.control.connectDevice(device, callback, fitFileCallback)
            navController.navigate(Screen.ConnectedDeviceScreen.route)
        }
    }
}

