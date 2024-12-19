package com.rahul.coordinatesystemtransformation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rahul.coordinatesystemtransformation.data.model.Parameter
import com.rahul.coordinatesystemtransformation.presentation.CoordinateSystemAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullCoordinateSystemListScreen(
    navController: NavHostController,
    coordinateSystems: List<Parameter>,
    onSelectedSystemChange: (String) -> Unit, // Update shared state
    isOutput: Boolean = false,
    onCoordinateSystemAction: (CoordinateSystemAction) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Coordinate System") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search...") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                val filteredSystems = coordinateSystems.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                            it.code.contains(searchQuery, ignoreCase = true) ||
                            it.type.contains(searchQuery, ignoreCase = true)
                }

                items(filteredSystems) { system ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .clickable {
                                val newSystem = "${system.code} - ${system.name} (${system.type})"
                                onSelectedSystemChange(newSystem) // Update shared state
                                val action = if (isOutput) {
                                    CoordinateSystemAction.OnSelectedOutputSystem(newSystem)
                                } else {
                                    CoordinateSystemAction.OnSelectedInputSystem(newSystem)
                                }
                                onCoordinateSystemAction(action)
                                navController.popBackStack()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = system.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Code: ${system.code}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Type: ${system.type}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
