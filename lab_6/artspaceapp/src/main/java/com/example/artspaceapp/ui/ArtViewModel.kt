package com.example.artspaceapp.ui

import androidx.lifecycle.ViewModel
import com.example.artspaceapp.data.DataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ArtViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ArtSpaceUiState())
    val uiState: StateFlow<ArtSpaceUiState> = _uiState.asStateFlow()
    private val artCount = DataSource.Artworks.size - 1

    private fun updateArtSpaceState(updatedScore: Int){
        _uiState.update { currentState ->
            currentState.copy(
                dataIndex = updatedScore
            )
        }
    }
    fun nextArt() {
        val index = _uiState.value.dataIndex
        updateArtSpaceState(if (index != artCount) index.plus(1) else 0)
    }
    fun previousArt() {
        val index = _uiState.value.dataIndex
        updateArtSpaceState(if (index != 0) index.minus(1) else artCount)
    }
}