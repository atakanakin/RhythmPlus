package com.atakan.rhythmplus.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.atakan.rhythmplus.data.ProvidedInfo
import com.atakan.rhythmplus.presentation.MainActivity
import com.atakan.rhythmplus.presentation.viewmodel.ConnectedViewModel
import com.atakan.rhythmplus.presentation.viewmodel.SDKViewModel
import com.atakan.rhythmplus.presentation.viewmodel.ScannedDeviceViewModel
import com.scosche.sdk24.ErrorType
import com.scosche.sdk24.FitFileContent
import com.scosche.sdk24.RhythmDevice
import com.scosche.sdk24.RhythmSDKDeviceCallback
import com.scosche.sdk24.RhythmSDKFitFileCallback
import com.scosche.sdk24.RhythmSDKScanningCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RhythmPlusService: Service(), RhythmSDKScanningCallback, RhythmSDKDeviceCallback, RhythmSDKFitFileCallback {

    @Inject
    lateinit var viewModel: ScannedDeviceViewModel

    @Inject
    lateinit var sdkViewModel: SDKViewModel

    @Inject
    lateinit var providedModel: ConnectedViewModel

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        val sdk = sdkViewModel.control
        sdkViewModel.callback = this
        sdkViewModel.fitFileCallback = this
        sdk.startScan(this)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                // Add the extra value for fragment identification if necessary
                //putExtra("FRAGMENT_ID", R.id.navigation_plus)
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationTitle = "Rhythm Plus"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Rhythm Plus"
            val descriptionText = "Rhythm Plus working."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("RhythmPlus", name, importance).apply {
                description = descriptionText
                enableVibration(false)
            }
            notificationManager = getSystemService(NotificationManager::class.java)
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)

            notificationBuilder = NotificationCompat.Builder(this, "RhythmPlus")
                .setContentTitle(notificationTitle)
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setContentIntent(pendingIntent)
        }
        else{
            notificationBuilder = NotificationCompat.Builder(this, "RhythmPlus")
                .setContentTitle(notificationTitle)
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setContentIntent(pendingIntent)
            val noVibrationPattern = longArrayOf(0)

            notificationBuilder.setVibrate(noVibrationPattern)
        }
        startForeground(1, notificationBuilder.build())

        val hearRateObserver = Observer<ProvidedInfo> {
            val notificationText = it.toString()
            updateNotificationText(notificationText)
        }
        // Observe the ViewModel's LiveData to update the notification text
        providedModel.connected.observeForever(hearRateObserver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)
        startService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)
    }

    override fun deviceFound(device: RhythmDevice?) {
        if(device?.name != null){
            viewModel.handleDeviceFound(device)
        }
        else{
            Log.w("deviceFound", "No device")
        }
    }

    override fun error(p0: ErrorType?) {
        TODO("Not yet implemented")
    }

    override fun deviceLost(device: RhythmDevice?) {
        Log.d("deviceLost", device?.name.toString())

    }

    override fun deviceConnected(device: RhythmDevice?) {
        Log.d("deviceConnected", "Connected")
        if (device != null) {
            Log.d("deviceConnected", device.name)
        }
        else{
            Log.d("deviceConnected", "null")
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

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun createNotification() {
        startForeground(1, notificationBuilder.build())
    }

    private fun updateNotificationText(notificationText: String) {
        notificationBuilder.setContentText(notificationText)
        val updatedNotification = notificationBuilder.build()
        notificationManager.notify(1, updatedNotification)
    }
}