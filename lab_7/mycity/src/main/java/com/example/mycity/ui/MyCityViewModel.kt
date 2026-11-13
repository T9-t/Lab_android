package com.example.mycity.ui

import androidx.lifecycle.ViewModel
import com.example.mycity.data.DataSource
import com.example.mycity.model.CategoryType
import com.example.mycity.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyCityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MyCityUiState())
    val uiState: StateFlow<MyCityUiState> = _uiState.asStateFlow()

    fun selectCategory(category: CategoryType) {
        val places = DataSource.getPlacesByCategory(category)
        _uiState.update {
            it.copy(
                currentCategory = category,
                currentPlacesList = places,
                selectedPlace = places.firstOrNull() ?: it.selectedPlace
            )
        }
    }
    fun selectPlace(place: Place) {
        _uiState.update {
            it.copy(selectedPlace = place)
        }
    }
}