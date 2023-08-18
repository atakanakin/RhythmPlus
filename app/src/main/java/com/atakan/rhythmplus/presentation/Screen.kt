package com.atakan.rhythmplus.presentation

sealed class Screen (val route: String){
    object ConnectedDeviceScreen: Screen("connected_screen")
    object ScannedDeviceScreen: Screen("scan_screen")
}