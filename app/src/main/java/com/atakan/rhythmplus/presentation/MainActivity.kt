package com.atakan.rhythmplus.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.atakan.rhythmplus.presentation.screen.connected_screen.ConnectedDeviceScreen
import com.atakan.rhythmplus.presentation.viewmodel.SDKViewModel
import com.atakan.rhythmplus.presentation.screen.scan_device.ScannedDeviceScreen
import com.atakan.rhythmplus.presentation.viewmodel.ScannedDeviceViewModel
import com.atakan.rhythmplus.presentation.theme.RhythmPlusTheme
import com.atakan.rhythmplus.presentation.viewmodel.ConnectedViewModel
import com.atakan.rhythmplus.service.RhythmPlusService
import com.scosche.sdk24.ErrorType
import com.scosche.sdk24.FitFileContent
import com.scosche.sdk24.RhythmDevice
import com.scosche.sdk24.RhythmSDKDeviceCallback
import com.scosche.sdk24.RhythmSDKFitFileCallback
import com.scosche.sdk24.RhythmSDKScanningCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(){

    @Inject
    lateinit var viewModel: ScannedDeviceViewModel

    @Inject
    lateinit var sdkViewModel: SDKViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        val requiredPermissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_SCAN
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, requiredPermissions, 45)
            }
        }
        */
        val intent = Intent(this, RhythmPlusService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, intent)
        } else {
            this.startService(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        setContent {
            RhythmPlusTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Perform Bluetooth scanning and connection.
                    AppNavigation(context = this)
                }
            }
        }
    }

    @Composable
    fun AppNavigation(context: Context) {
        val navController = rememberNavController()

        NavHost(navController, startDestination = Screen.ScannedDeviceScreen.route) {
            composable(route = Screen.ScannedDeviceScreen.route) {
                ScannedDeviceScreen(callback = sdkViewModel.callback, fitFileCallback = sdkViewModel.fitFileCallback, navController = navController, context = context)
            }
            composable(route = Screen.ConnectedDeviceScreen.route){
                ConnectedDeviceScreen()
            }
            // Add more composable entries for other screens
        }
    }
}