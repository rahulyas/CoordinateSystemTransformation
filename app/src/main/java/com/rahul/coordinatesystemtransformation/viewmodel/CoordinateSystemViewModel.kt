package com.rahul.coordinatesystemtransformation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.rahul.coordinatesystemtransformation.data.model.Parameter
import com.rahul.coordinatesystemtransformation.data.usecase.GetAllParametersUseCase
import com.rahul.coordinatesystemtransformation.data.usecase.InsertParametersUseCase
import com.rahul.coordinatesystemtransformation.presentation.CoordinateSystemAction
import com.rahul.coordinatesystemtransformation.presentation.CoordinateSystemEvent
import com.rahul.coordinatesystemtransformation.presentation.CoordinateSystemState
import com.rahul.coordinatesystemtransformation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.locationtech.proj4j.CRSFactory
import org.locationtech.proj4j.CoordinateTransformFactory
import org.locationtech.proj4j.ProjCoordinate
import javax.inject.Inject


@HiltViewModel
class CoordinateSystemViewModel @Inject constructor(
    private val getAllParametersUseCase: GetAllParametersUseCase,
    private val insertParametersUseCase: InsertParametersUseCase
): ViewModel() {

    var coordinateSystemState by mutableStateOf(CoordinateSystemState())

    private val _coordinateSystemState = MutableStateFlow(CoordinateSystemState())
    val coordinateSystemStates: StateFlow<CoordinateSystemState> get() = _coordinateSystemState

    val storedParameters = mutableListOf<Parameter>() // List to store the data
    private val _uiEvent = Channel<CoordinateSystemEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    /**
     * Logic to handle swapping of input and output coordinate systems.
     */
    fun swapCoordinateSystems() {
        val temp = _coordinateSystemState.value.selectedInputSystem
        _coordinateSystemState.value = _coordinateSystemState.value.copy(
            selectedInputSystem = _coordinateSystemState.value.selectedOutputSystem,
            selectedOutputSystem = temp
        )
    }

    fun onCoordinateSystemAction(action: CoordinateSystemAction){
        coordinateSystemState = when(action){
            is CoordinateSystemAction.OnChangeInputLatitude -> {
                coordinateSystemState.copy(
                    inputLatitude = action.inputLatitude
                )
            }

            is CoordinateSystemAction.OnChangeInputLongitude -> {
                coordinateSystemState.copy(
                    inputLongitude = action.inputLongitude
                )
            }

            is CoordinateSystemAction.OnSelectedInputSystem -> {
                coordinateSystemState.copy(
                    selectedInputSystem = action.selectedInputSystem
                )
            }

            is CoordinateSystemAction.OnSelectedOutputSystem -> {
                coordinateSystemState.copy(
                    selectedOutputSystem = action.selectedOutputSystem
                )
            }

        }
    }

    fun transformCoordinates() {
        viewModelScope.launch {
            try {
                if(coordinateSystemState.selectedInputSystem.isNotEmpty() && coordinateSystemState.selectedOutputSystem.isNotEmpty())
                {
                    val crsFactory = CRSFactory()
                    Log.i("Testing", "transformCoordinates: ${coordinateSystemState.selectedInputSystem} == ${coordinateSystemState.selectedOutputSystem}")
                    // Define source and target CRS
                    val sourceCRS = crsFactory.createFromName("EPSG:${coordinateSystemState.selectedInputSystem.substringBefore(" - ").trim()}")
                    val targetCRS = crsFactory.createFromName("EPSG:${coordinateSystemState.selectedOutputSystem.substringBefore(" - ").trim()}")

                    // Create a CoordinateTransform
                    val transformFactory = CoordinateTransformFactory()
                    val transform = transformFactory.createTransform(sourceCRS, targetCRS)

                    // Transform coordinates
                    val inputCoord = ProjCoordinate(
                        coordinateSystemState.inputLongitude.toDouble(),
                        coordinateSystemState.inputLatitude.toDouble()
                    )
                    val outputCoord = ProjCoordinate()
                    transform.transform(inputCoord, outputCoord)

                    // Update the state
                    coordinateSystemState = coordinateSystemState.copy(
                        outputLatitude = outputCoord.y.toString(),
                        outputLongitude = outputCoord.x.toString()
                    )
                    Log.i("Testing", "transformCoordinates: ${outputCoord.y} == ${outputCoord.x}")
                }else{
                    _uiEvent.send(CoordinateSystemEvent.Error("Please select both input and output coordinate systems"))
                }

            } catch (e: Exception) {
                _uiEvent.send(CoordinateSystemEvent.Error("Error transforming coordinates: ${e.message}"))
            }
        }
    }

    //26.91151111437163, 75.74486712374917

    fun getAllGetAllParameters(){
        viewModelScope.launch {
            when (val resource = getAllParametersUseCase()) {
                is Resource.Success -> {
                    resource.data?.collect {dataFlow ->
                        storedParameters.addAll(dataFlow)
                    }
                }
                is Resource.Error -> {
                    println("Error: ${resource.message}")
                }
            }
        }
    }

    fun insertParameters(parameters: List<Parameter>) {
        viewModelScope.launch {
            insertParametersUseCase(parameters)
        }
    }
}