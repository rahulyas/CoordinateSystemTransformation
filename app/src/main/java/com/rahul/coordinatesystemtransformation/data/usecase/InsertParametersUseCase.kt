package com.rahul.coordinatesystemtransformation.data.usecase

import com.rahul.coordinatesystemtransformation.data.model.Parameter
import com.rahul.coordinatesystemtransformation.data.repository.ParameterRepository
import javax.inject.Inject

class InsertParametersUseCase @Inject constructor(private val parameterRepository: ParameterRepository)  {
    suspend operator fun invoke(parameters: List<Parameter>) {
        parameterRepository.insertParameters(parameters)
    }
}