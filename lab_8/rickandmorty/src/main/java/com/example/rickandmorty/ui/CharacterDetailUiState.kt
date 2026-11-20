package com.example.rickandmorty.ui

import com.example.rickandmorty.network.Character
import com.example.rickandmorty.network.Episode

sealed interface CharacterDetailUiState {
    data class Success(val character: Pair<Character, List<Episode>>) : CharacterDetailUiState
    object Error : CharacterDetailUiState
    object Loading : CharacterDetailUiState
}