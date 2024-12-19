package com.rahul.coordinatesystemtransformation.ui.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rahul.coordinatesystemtransformation.presentation.CoordinateSystemAction
import com.rahul.coordinatesystemtransformation.presentation.CoordinateSystemEvent
import com.rahul.coordinatesystemtransformation.ui.components.ObserveAsEvents
import com.rahul.coordinatesystemtransformation.viewmodel.CoordinateSystemViewModel
import kotlin.system.exitProcess

@Composable
fun CoordinateConverterUI(viewModel: CoordinateSystemViewModel) {
    val inputNavController = rememberNavController()
    val outputNavController = rememberNavController()
    val coordinateSystemState = viewModel.coordinateSystemStates.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getAllGetAllParameters()
    }

    ObserveAsEvents(flow = viewModel.uiEvent) {
        when (it) {
            is CoordinateSystemEvent.Error -> Toast.makeText(
                context,
                it.message,
                Toast.LENGTH_SHORT
            ).show()

            is CoordinateSystemEvent.Success -> Unit
        }
    }

    var showExitDialog by remember { mutableStateOf(false) }

    // Handle the back button press
    BackHandler(enabled = true) {
        showExitDialog = true
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .height(16.dp)
    ) {
        // Input Coordinate System Section
        NavHost(inputNavController, startDestination = "dialog") {
            composable("dialog") {
                CoordinateSystemSectionWithSearch(
                    title = "Input Coordinate System",
                    defaultSystem = coordinateSystemState.selectedInputSystem,
                    viewModel = viewModel,
                    onCoordinateSystemAction = { action ->
                        viewModel.onCoordinateSystemAction(action)
                    },
                    onMoreClicked = { inputNavController.navigate("full_list") }
                )
            }
            composable("full_list") {
                FullCoordinateSystemListScreen(
                    navController = inputNavController,
                    coordinateSystems = viewModel.storedParameters,
                    onSelectedSystemChange = {
                        CoordinateSystemAction.OnSelectedOutputSystem(it)
                    },
                    onCoordinateSystemAction = { action ->
                        viewModel.onCoordinateSystemAction(action)
                    },
                    onBack = { inputNavController.popBackStack() }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Transform and Swap Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = { viewModel.transformCoordinates() }) {
                Text("Transform")
            }
            Button(onClick = { viewModel.swapCoordinateSystems() }) {
                Text("Swap ↔")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Output Coordinate System Section
        NavHost(outputNavController, startDestination = "dialog") {
            composable("dialog") {
                CoordinateSystemSectionWithSearch(
                    title = "Output Coordinate System",
                    defaultSystem = coordinateSystemState.selectedOutputSystem,
                    viewModel = viewModel,
                    isOutput = true,
                    onCoordinateSystemAction = { action ->
                        viewModel.onCoordinateSystemAction(action)
                    },
                    onMoreClicked = { outputNavController.navigate("full_list") }
                )
            }
            composable("full_list") {
                FullCoordinateSystemListScreen(
                    navController = outputNavController,
                    coordinateSystems = viewModel.storedParameters,
                    onSelectedSystemChange = {
                        CoordinateSystemAction.OnSelectedOutputSystem(it)
                    },
                    isOutput = true,
                    onCoordinateSystemAction = { action ->
                        viewModel.onCoordinateSystemAction(action)
                    },
                    onBack = { outputNavController.popBackStack() }
                )
            }
        }
    }

    // Exit confirmation dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Exit App") },
            text = { Text("Do you really want to exit the app?") },
            confirmButton = {
                Button(onClick = { exitApp() }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showExitDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

private fun exitApp() {
    // Finish the current activity to exit the app
    android.os.Process.killProcess(android.os.Process.myPid())
    exitProcess(0)
}