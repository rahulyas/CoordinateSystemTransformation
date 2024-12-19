package com.rahul.coordinatesystemtransformation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.rahul.coordinatesystemtransformation.ui.screen.AppNavHost
import com.rahul.coordinatesystemtransformation.utils.PreferencesHelper
import com.rahul.coordinatesystemtransformation.viewmodel.CoordinateSystemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            val preferencesHelper = PreferencesHelper(applicationContext)
            val navController = rememberNavController()
            val viewModel: CoordinateSystemViewModel by viewModels()

            AppNavHost(
                navController = navController,
                preferencesHelper = preferencesHelper,
                viewModel = viewModel
            )
        }
    }
}
