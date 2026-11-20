package com.example.rickandmorty.network

import kotlinx.serialization.Serializable

@Serializable
data class CharactersResponse(
    val results: List<Character>
)
@Serializable
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val episode: List<String> = emptyList()
)

@Serializable
data class Episode(
    val id: Int,
    val name: String
)
