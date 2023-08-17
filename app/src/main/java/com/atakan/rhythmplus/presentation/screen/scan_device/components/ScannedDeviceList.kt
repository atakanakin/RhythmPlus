package com.atakan.rhythmplus.presentation.screen.scan_device.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.scosche.sdk24.RhythmDevice

@Composable
fun ScannedDeviceList(devices: List<RhythmDevice>, onItemClick: (RhythmDevice) -> Unit) {
    Column {
        devices.forEach { device ->
            ScannedDeviceItem(device = device, onItemClick = onItemClick)
        }
    }
}