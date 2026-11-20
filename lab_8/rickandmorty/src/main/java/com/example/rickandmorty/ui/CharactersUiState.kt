package com.example.rickandmorty.ui

import com.example.rickandmorty.network.Character

sealed interface CharactersUiState {
    data class Success(
        val characters: List<Character>,
        val isLoadingNextPage: Boolean = false
    ) : CharactersUiState
    object Error : CharactersUiState
    object Loading : CharactersUiState
}