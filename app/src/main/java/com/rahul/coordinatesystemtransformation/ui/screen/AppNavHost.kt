package com.rahul.coordinatesystemtransformation.ui.screen


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rahul.coordinatesystemtransformation.utils.PreferencesHelper
import com.rahul.coordinatesystemtransformation.viewmodel.CoordinateSystemViewModel

@Composable
fun AppNavHost(navController: NavHostController, preferencesHelper: PreferencesHelper, viewModel: CoordinateSystemViewModel) {
    NavHost(navController, startDestination = "permission_request") {
        composable("permission_request") {
            PermissionRequestScreen(onPermissionsGranted = {
                // Navigate based on CSV loading state
                if (preferencesHelper.isCsvLoaded()) {
                    navController.navigate("main_screen") {
                        popUpTo("permission_request") { inclusive = true }
                    }
                } else {
                    navController.navigate("csv_load_screen") {
                        popUpTo("permission_request") { inclusive = true }
                    }
                }
            })
        }
        composable("csv_load_screen") {
            CsvLoadScreen(navController, preferencesHelper, viewModel)
        }
        composable("main_screen") {
            CoordinateConverterUI(viewModel)
        }
    }
}
