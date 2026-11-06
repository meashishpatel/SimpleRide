package com.simpleride.rideos.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simpleride.rideos.data.model.VehicleData
import com.simpleride.rideos.data.repository.VehicleRepository
import com.simpleride.rideos.presentation.model.SignalStatus
import com.simpleride.rideos.presentation.model.VehicleStateReducer
import com.simpleride.rideos.presentation.model.VehicleUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Collects vehicle data and exposes it as a single [StateFlow] of [VehicleUiState].
 *
 * Edge cases handled here:
 *  - invalid / missing signals (validated by [VehicleStateReducer]);
 *  - stale data: if no sample arrives within [signalTimeoutMillis] the state switches
 *    to [SignalStatus.STALE] and all live values are cleared;
 *  - source failure: the error is shown and collection is retried after [retryDelayMillis].
 */
class VehicleViewModel(
    private val repository: VehicleRepository,
    private val clock: () -> Long = System::currentTimeMillis,
    private val signalTimeoutMillis: Long = DEFAULT_SIGNAL_TIMEOUT_MILLIS,
    private val retryDelayMillis: Long = DEFAULT_RETRY_DELAY_MILLIS
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleUiState())
    val uiState: StateFlow<VehicleUiState> = _uiState.asStateFlow()

    init {
        observeVehicleData()
    }

    private sealed interface Event {
        data class Sample(val data: VehicleData) : Event
        data object Timeout : Event
        data object SourceFailed : Event
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeVehicleData() {
        viewModelScope.launch {
            repository.observeVehicleData()
                // Each new sample restarts the watchdog; if it fires, the signal is stale.
                .transformLatest<VehicleData, Event> { data ->
                    emit(Event.Sample(data))
                    delay(signalTimeoutMillis)
                    emit(Event.Timeout)
                }
                .retryWhen { cause, _ ->
                    if (cause is CancellationException) return@retryWhen false
                    emit(Event.SourceFailed)
                    delay(retryDelayMillis)
                    true
                }
                .collect { event -> _uiState.update { reduce(it, event) } }
        }
    }

    private fun reduce(state: VehicleUiState, event: Event): VehicleUiState = when (event) {
        is Event.Sample -> VehicleStateReducer.onSample(
            previous = state,
            data = event.data,
            nowMillis = clock(),
            staleThresholdMillis = signalTimeoutMillis
        )

        Event.Timeout -> VehicleStateReducer.onSignalLost(
            state, SignalStatus.STALE, "Vehicle signal lost"
        )

        Event.SourceFailed -> VehicleStateReducer.onSignalLost(
            state, SignalStatus.ERROR, "Unable to read vehicle data. Retrying…"
        )
    }

    companion object {
        const val DEFAULT_SIGNAL_TIMEOUT_MILLIS = 3_000L
        const val DEFAULT_RETRY_DELAY_MILLIS = 2_000L
    }
}
