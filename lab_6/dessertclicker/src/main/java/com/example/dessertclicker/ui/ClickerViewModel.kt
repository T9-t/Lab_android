package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ClickerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ClickerUiState())
    val uiState: StateFlow<ClickerUiState> = _uiState.asStateFlow()
    val desserts = Datasource.dessertList

    private fun determineDessertToShow(dessertsSold: Int): Int{
        var dessertIndex = 0
        for ((i, element) in desserts.withIndex()) {
            if (dessertsSold >= element.startProductionAmount) {
                dessertIndex = i
            } else {
                break
            }
        }
        return dessertIndex
    }

    fun onDessertClicked() {
        _uiState.update { currentState ->
            val currentDessertPrice = desserts[_uiState.value.currentDessertIndex].price
            val revenue = _uiState.value.revenue + currentDessertPrice
            val dessertsSold = _uiState.value.dessertsSold + 1
            val nextDessertIndex = determineDessertToShow(dessertsSold)

            currentState.copy(
                revenue = revenue,
                dessertsSold = dessertsSold,
                currentDessertIndex = nextDessertIndex
            )
        }
    }
}