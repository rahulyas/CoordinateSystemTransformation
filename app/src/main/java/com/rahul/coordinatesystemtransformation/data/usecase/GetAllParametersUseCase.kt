package com.rahul.coordinatesystemtransformation.data.usecase

import com.rahul.coordinatesystemtransformation.data.repository.ParameterRepository
import javax.inject.Inject

class GetAllParametersUseCase @Inject constructor(private val parameterRepository: ParameterRepository) {
    suspend operator fun invoke() = parameterRepository.getAllParameters()
}