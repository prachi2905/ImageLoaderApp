package com.pixabay.imageloaderapp.presentation.utils

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.pixabay.imageloaderapp.R


fun Fragment.setToolbar(toolbar: Toolbar) {
    (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
    (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    (requireActivity() as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
}

@RequiresApi(Build.VERSION_CODES.M)
fun Activity.changeStatusBar(shouldBeLight: Boolean) {
    WindowInsetsControllerCompat(window, window.decorView.rootView).isAppearanceLightStatusBars =
        shouldBeLight
    window.statusBarColor = if (shouldBeLight) getColor(R.color.white) else getColor(R.color.black)
}