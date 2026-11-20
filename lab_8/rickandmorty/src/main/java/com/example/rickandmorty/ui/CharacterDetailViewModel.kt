package com.example.rickandmorty.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rickandmorty.CharactersApplication
import com.example.rickandmorty.data.CharactersRepository
import kotlinx.coroutines.launch
import java.io.IOException

class CharacterDetailViewModel(
    private val charactersRepository: CharactersRepository
) : ViewModel() {

    var uiState: CharacterDetailUiState by mutableStateOf(CharacterDetailUiState.Loading)
        private set

    fun selectCharacter(characterId: Int) {
        viewModelScope.launch {
            uiState = try {
                CharacterDetailUiState.Success(
                    charactersRepository.getCharacter(characterId),
                )
            } catch (e: IOException) {
                CharacterDetailUiState.Error
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CharactersApplication)
                val charactersRepository = application.container.charactersRepository
                CharacterDetailViewModel(charactersRepository = charactersRepository)
            }
        }
    }
}