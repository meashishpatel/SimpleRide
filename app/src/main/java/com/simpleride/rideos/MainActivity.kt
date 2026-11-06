package com.simpleride.rideos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simpleride.rideos.data.repository.VehicleRepository
import com.simpleride.rideos.data.source.SimulatedVehicleDataSource
import com.simpleride.rideos.presentation.screen.HomeScreen
import com.simpleride.rideos.presentation.viewmodel.VehicleViewModel
import com.simpleride.rideos.presentation.viewmodel.VehicleViewModelFactory
import com.simpleride.rideos.ui.theme.SimpleRideTheme

class MainActivity : ComponentActivity() {

    private val vehicleDataSource by lazy {
        SimulatedVehicleDataSource()
    }

    private val vehicleRepository by lazy {
        VehicleRepository(vehicleDataSource)
    }

    private val vehicleViewModel: VehicleViewModel by viewModels {
        VehicleViewModelFactory(vehicleRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SimpleRideTheme {
                val state = vehicleViewModel.uiState
                    .collectAsStateWithLifecycle()

                HomeScreen(
                    state = state.value
                )
            }
        }
    }
}