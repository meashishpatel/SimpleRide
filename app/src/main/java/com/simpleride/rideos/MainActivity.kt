package com.simpleride.rideos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simpleride.rideos.data.repository.VehicleRepository
import com.simpleride.rideos.data.source.SimulatedVehicleDataSource
import com.simpleride.rideos.presentation.screen.HomeScreen
import com.simpleride.rideos.presentation.viewmodel.VehicleViewModel
import com.simpleride.rideos.presentation.viewmodel.VehicleViewModelFactory
import com.simpleride.rideos.ui.theme.SimpleRideTheme

class MainActivity : ComponentActivity() {

    // Manual wiring: data source -> repository -> ViewModel. A DI framework is not needed at this size.
    private val vehicleViewModel: VehicleViewModel by viewModels {
        VehicleViewModelFactory(VehicleRepository(SimulatedVehicleDataSource()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SimpleRideTheme {
                val state by vehicleViewModel.uiState.collectAsStateWithLifecycle()
                HomeScreen(state = state)
            }
        }
    }
}
