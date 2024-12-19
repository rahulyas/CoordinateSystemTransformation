package com.rahul.coordinatesystemtransformation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rahul.coordinatesystemtransformation.R
import com.rahul.coordinatesystemtransformation.data.model.Parameter
import com.rahul.coordinatesystemtransformation.presentation.CoordinateSystemAction
import com.rahul.coordinatesystemtransformation.ui.components.OutLinedInput
import com.rahul.coordinatesystemtransformation.viewmodel.CoordinateSystemViewModel

@Composable
fun CoordinateSystemSectionWithSearch(
    title: String,
    defaultSystem: String,
    viewModel: CoordinateSystemViewModel,
    isOutput: Boolean = false,
    onCoordinateSystemAction: (CoordinateSystemAction) -> Unit,
    onMoreClicked: () -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedSystem by remember { mutableStateOf(defaultSystem) }
    val latestSelectedSystem = rememberUpdatedState(selectedSystem)

    Column {
        TitleSection(title = title, latestSelectedSystem = latestSelectedSystem.value) {
            isDialogOpen = true
        }
        if (isOutput) {
            OutputFields(viewModel.coordinateSystemState.outputLongitude, viewModel.coordinateSystemState.outputLatitude)
        } else {
            InputFields(viewModel.coordinateSystemState.inputLongitude, viewModel.coordinateSystemState.inputLatitude, onCoordinateSystemAction)
        }
        if (isDialogOpen) {
            CoordinateSystemDialog(
                coordinateSystems = viewModel.storedParameters,
                isOutput = isOutput,
                onCoordinateSystemAction = onCoordinateSystemAction,
                onDismiss = { isDialogOpen = false },
                onMoreClicked = onMoreClicked,
                onSelectSystem = { selectedSystem = it }
            )
        }
    }
}

@Composable
private fun TitleSection(
    title: String,
    latestSelectedSystem: String,
    onChangeClicked: () -> Unit
) {
    Text(text = title, style = MaterialTheme.typography.titleMedium)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        Text(
            text = "EPSG:$latestSelectedSystem",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onChangeClicked) {
            Text("Change")
        }
    }
}

@Composable
private fun OutputFields(longitudeHint: String, latitudeHint: String) {
    Column {
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(longitudeHint) },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(latitudeHint) },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )
    }
}

@Composable
private fun InputFields(
    longitudeHint: String,
    latitudeHint: String,
    onCoordinateSystemAction: (CoordinateSystemAction) -> Unit
) {
    Column {
        OutLinedInput(
            label = stringResource(R.string.latitude),
            value = latitudeHint,
            onValueChange = { latitude ->
                onCoordinateSystemAction(
                    CoordinateSystemAction.OnChangeInputLatitude(latitude)
                )
            },
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutLinedInput(
            label = stringResource(R.string.longitude),
            value = longitudeHint,
            onValueChange = { longitude ->
                onCoordinateSystemAction(
                    CoordinateSystemAction.OnChangeInputLongitude(longitude)
                )
            },
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        )
    }
}

@Composable
private fun CoordinateSystemDialog(
    coordinateSystems: List<Parameter>,
    isOutput: Boolean,
    onCoordinateSystemAction: (CoordinateSystemAction) -> Unit,
    onDismiss: () -> Unit,
    onMoreClicked: () -> Unit,
    onSelectSystem: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Select Coordinate System") },
        text = {
            var searchQuery by remember { mutableStateOf("") }
            val filteredSystems = coordinateSystems.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.code.contains(searchQuery, ignoreCase = true) ||
                        it.type.contains(searchQuery, ignoreCase = true)
            }
            val limitedSystems = if (searchQuery.isEmpty()) filteredSystems.take(10) else filteredSystems

            Column {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search...") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(limitedSystems) { system ->
                        CoordinateSystemItem(
                            system = system,
                            onClick = {
                                val selectedSystem = "${system.code} - ${system.name} (${system.type})"
                                onSelectSystem(selectedSystem)
                                val action = if (isOutput) {
                                    CoordinateSystemAction.OnSelectedOutputSystem(selectedSystem)
                                } else {
                                    CoordinateSystemAction.OnSelectedInputSystem(selectedSystem)
                                }
                                onCoordinateSystemAction(action)
                                onDismiss()
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        onDismiss()
                        onMoreClicked()
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("More...")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun CoordinateSystemItem(system: Parameter, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = system.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Code: ${system.code}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Type: ${system.type}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
    }
}
