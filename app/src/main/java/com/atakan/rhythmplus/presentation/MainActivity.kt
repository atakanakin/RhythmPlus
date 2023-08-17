package com.atakan.rhythmplus.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.atakan.rhythmplus.Screen
import com.atakan.rhythmplus.presentation.screen.connected_screen.ConnectedDeviceScreen
import com.atakan.rhythmplus.presentation.viewmodel.SDKViewModel
import com.atakan.rhythmplus.presentation.screen.scan_device.ScannedDeviceScreen
import com.atakan.rhythmplus.presentation.viewmodel.ScannedDeviceViewModel
import com.atakan.rhythmplus.presentation.theme.RhythmPlusTheme
import com.atakan.rhythmplus.presentation.viewmodel.ConnectedViewModel
import com.scosche.sdk24.ErrorType
import com.scosche.sdk24.FitFileContent
import com.scosche.sdk24.RhythmDevice
import com.scosche.sdk24.RhythmSDKDeviceCallback
import com.scosche.sdk24.RhythmSDKFitFileCallback
import com.scosche.sdk24.RhythmSDKScanningCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), RhythmSDKScanningCallback, RhythmSDKDeviceCallback,
    RhythmSDKFitFileCallback {

    @Inject
    lateinit var viewModel: ScannedDeviceViewModel

    @Inject
    lateinit var sdkViewModel: SDKViewModel

    @Inject
    lateinit var providedModel: ConnectedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sdk = sdkViewModel.control
        sdkViewModel.callback = this
        sdkViewModel.fitFileCallback = this
        setContent {
            RhythmPlusTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Perform Bluetooth scanning and connection.
                    sdk.startScan(this)
                    AppNavigation(context = this)
                }
            }
        }
    }

    override fun deviceFound(device: RhythmDevice?) {
        if(device?.name != null){
            viewModel.handleDeviceFound(device)
        }
        else{
            Log.w("Bluetooth", "No device")
        }
    }

    override fun error(p0: ErrorType?) {
        TODO("Not yet implemented")
    }

    override fun deviceLost(device: RhythmDevice?) {
        Log.d("Atakan", "Disconnected")
        if (device != null) {
            Log.d("Atakan", device.name)
        }
        else{
            Log.d("Atakan", "null")
        }
    }

    override fun deviceConnected(device: RhythmDevice?) {
        Log.d("deviceConnected", "Connected")
        if (device != null) {
            Log.d("Atakan", device.name)
        }
        else{
            Log.d("Atakan", "null")
        }
    }

    override fun updateHeartRate(p0: String?) {
        providedModel.updateVal(heartRate = p0)
    }

    override fun monitorStateInvalid() {
        Log.d("monitorStateInvalid", "monitorStateInvalid")
    }

    override fun updateBatteryLevel(p0: Int) {
        providedModel.updateVal(batteryLevel = p0)
    }

    override fun updateZone(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun updateSportMode(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun updateFirmwareVersion(p0: String?) {
        providedModel.updateVal(firmwareVersion = p0)
    }

    override fun fitFilesFound(p0: MutableList<FitFileContent.FitFileInfo>?) {
        TODO("Not yet implemented")
    }

    override fun fitFileDownloadComplete(p0: ByteArray?, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun fitFileDeleteComplete(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun downloadProgressUpdate(p0: Int, p1: FitFileContent.FitFileInfo?) {
        TODO("Not yet implemented")
    }

    @Composable
    fun AppNavigation(context: Context) {
        val navController = rememberNavController()

        NavHost(navController, startDestination = Screen.ScannedDeviceScreen.route) {
            composable(route = Screen.ScannedDeviceScreen.route) {
                ScannedDeviceScreen(callback = sdkViewModel.callback, fitFileCallback = sdkViewModel.fitFileCallback, navController = navController)
            }
            composable(route = Screen.ConnectedDeviceScreen.route){
                ConnectedDeviceScreen()
            }
            // Add more composable entries for other screens
        }
    }
}