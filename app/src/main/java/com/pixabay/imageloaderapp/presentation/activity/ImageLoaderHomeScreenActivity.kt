package com.pixabay.imageloaderapp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pixabay.imageloaderapp.R
import com.pixabay.imageloaderapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageLoaderHomeScreenActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        window?.setBackgroundDrawableResource(R.color.white)
        setContentView(binding.root)
    }
}