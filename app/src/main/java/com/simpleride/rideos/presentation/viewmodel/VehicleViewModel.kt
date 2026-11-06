package com.simpleride.rideos.presentation.viewmodel

import com.simpleride.rideos.data.repository.VehicleRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simpleride.rideos.presentation.model.VehicleUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VehicleViewModel(
    private val repository: VehicleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleUiState())
    val uiState: StateFlow<VehicleUiState> = _uiState.asStateFlow()

    private val staleDataThreshold = 3000L

    init {
        observeVehicleData()
    }

    private fun observeVehicleData() {
        viewModelScope.launch {
            try {
                repository.observeVehicleData().collect { vehicleData ->

                    val currentTime = System.currentTimeMillis()

                    val isTimestampValid =
                        vehicleData.timestamp <= currentTime &&
                                currentTime - vehicleData.timestamp <= staleDataThreshold

                    val isSpeedValid =
                        vehicleData.speed.isFinite() &&
                                vehicleData.speed >= 0f

                    _uiState.value = VehicleUiState(
                        speed = vehicleData.speed.takeIf { isSpeedValid },
                        motorOn = vehicleData.motorOn,
                        killSwitchActive = vehicleData.killSwitchActive,
                        isDataAvailable = isTimestampValid,
                        lastUpdated = vehicleData.timestamp,
                        errorMessage = when {
                            !isTimestampValid -> "Vehicle data is stale or invalid"
                            !isSpeedValid -> "Invalid speed signal"
                            else -> null
                        }
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDataAvailable = false,
                    errorMessage = "Unable to receive vehicle data"
                )
            }
        }
    }
}