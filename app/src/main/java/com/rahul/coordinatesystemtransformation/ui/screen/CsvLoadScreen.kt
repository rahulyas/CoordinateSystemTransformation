package com.rahul.coordinatesystemtransformation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.rahul.coordinatesystemtransformation.R
import com.rahul.coordinatesystemtransformation.utils.PreferencesHelper
import com.rahul.coordinatesystemtransformation.utils.readCsvFile
import com.rahul.coordinatesystemtransformation.viewmodel.CoordinateSystemViewModel

@Composable
fun CsvLoadScreen(navController: NavController, preferencesHelper: PreferencesHelper, viewModel: CoordinateSystemViewModel) {
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    // This will run once when the screen is first loaded
    LaunchedEffect(Unit) {
        // Load CSV from resources and save it to Room
        if (!preferencesHelper.isCsvLoaded()) {
            isLoading = true
            val parameters = readCsvFile(context, R.raw.coordinate_system) // Replace with actual file loading logic
            viewModel.insertParameters(parameters)
            // Mark CSV as loaded and navigate to the main screen
            preferencesHelper.setCsvLoaded(true)
            isLoading = false
            navController.navigate("main_screen")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(), // Make the Box fill the entire screen
        contentAlignment = Alignment.Center // Center the content inside the Box
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text("Loading CSV file...")
        }
    }
}
