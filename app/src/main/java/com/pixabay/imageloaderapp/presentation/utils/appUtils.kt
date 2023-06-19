package com.pixabay.imageloaderapp.presentation.utils

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


fun showSoftKeyboard(activity: Activity) {
    activity.apply {
        WindowInsetsControllerCompat(
            window, window.decorView
        ).show(WindowInsetsCompat.Type.ime())
    }
}

 fun hideSoftKeyboard(activity: Activity) {
     activity.apply {
        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).hide(WindowInsetsCompat.Type.ime())
    }
}

 @RequiresApi(Build.VERSION_CODES.M)
 fun changeStatusBarColorToBlack(activity: Activity) {
     activity.apply {
        changeStatusBar(false)
    }
}