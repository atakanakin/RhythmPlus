package com.atakan.rhythmplus.presentation.screen.connected_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.atakan.rhythmplus.presentation.viewmodel.ConnectedViewModel

@Composable
fun ConnectedDeviceScreen(viewModel: ConnectedViewModel = hiltViewModel()) {

    val state by viewModel.connected.observeAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ){
        Row {
            Text(text = "Heart Rate:\t")
            Text(text = state?.heartRate ?: "null")
        }
        Row {
            Text(text = "Battery Level:\t")
            Text(text = state?.batteryLevel.toString())
        }
        Row {
            Text(text = "Firmware Version:\t")
            Text(text = state?.firmwareVersion ?: "null")
        }
    }
}