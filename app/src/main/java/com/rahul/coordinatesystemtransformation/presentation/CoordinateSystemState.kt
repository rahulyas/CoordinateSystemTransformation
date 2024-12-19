package com.rahul.coordinatesystemtransformation.presentation

data class CoordinateSystemState(
    var selectedInputSystem: String = "",
    var selectedOutputSystem: String = "",
    var inputLatitude: String = "",
    var inputLongitude: String = "",
    var outputLatitude: String = "",
    var outputLongitude: String = ""
)

sealed interface CoordinateSystemAction{
    data class OnSelectedInputSystem(val selectedInputSystem: String) : CoordinateSystemAction
    data class OnSelectedOutputSystem(val selectedOutputSystem: String) : CoordinateSystemAction
    data class OnChangeInputLatitude(val inputLatitude: String) : CoordinateSystemAction
    data class OnChangeInputLongitude(val inputLongitude: String) : CoordinateSystemAction
}

sealed interface CoordinateSystemEvent {
    data class Success(val message: String) : CoordinateSystemEvent
    data class Error(val message: String) : CoordinateSystemEvent
}