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

class CharactersViewModel(
    private val charactersRepository: CharactersRepository
) : ViewModel() {
    var uiState: CharactersUiState by mutableStateOf(CharactersUiState.Loading)
        private set
    var selectedSpecies by mutableStateOf<String?>(null)
        private set

    private var currentPage = 1
    private var isLastPage = false

    init {
        getCharacters()
    }

    fun selectSpecies(species: String?) {
        if (selectedSpecies == species) return
        selectedSpecies = species
        currentPage = 1
        isLastPage = false
        uiState = CharactersUiState.Loading
        getCharacters()
    }

    fun loadNextPage() {
        val currentState = uiState
        if (currentState !is CharactersUiState.Success || currentState.isLoadingNextPage || isLastPage) return

        uiState = currentState.copy(isLoadingNextPage = true)
        getCharacters()
    }

    private fun getCharacters() {
        viewModelScope.launch {
            val currentList = (uiState as? CharactersUiState.Success)?.characters ?: emptyList()

            uiState = try {
                val newCharacters = charactersRepository.getCharactersList(
                    page = currentPage,
                    species = selectedSpecies
                )
                isLastPage = newCharacters.size < 20
                currentPage++
                CharactersUiState.Success(
                    characters = currentList + newCharacters,
                    isLoadingNextPage = false
                )
            } catch (e: IOException) {
                CharactersUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CharactersApplication)
                val charactersRepository = application.container.charactersRepository
                CharactersViewModel(charactersRepository = charactersRepository)
            }
        }
    }
}