package com.atakan.rhythmplus.di

import android.content.Context
import com.atakan.rhythmplus.presentation.viewmodel.ConnectedViewModel
import com.atakan.rhythmplus.presentation.viewmodel.SDKViewModel
import com.atakan.rhythmplus.presentation.viewmodel.ScannedDeviceViewModel
import com.scosche.sdk24.ScoscheSDK24
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDeviceViewModel() : ScannedDeviceViewModel {
        return ScannedDeviceViewModel()
    }

    @Provides
    @Singleton
    fun provideSDK(@ApplicationContext appContext: Context) : ScoscheSDK24 {
        return ScoscheSDK24(appContext)
    }


    @Provides
    @Singleton
    fun provideSDKViewModel(sdk: ScoscheSDK24) : SDKViewModel {
        return SDKViewModel(sdk)
    }

    @Provides
    @Singleton
    fun provideConnectViewModel() : ConnectedViewModel{
        return ConnectedViewModel()
    }

}