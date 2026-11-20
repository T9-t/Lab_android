package com.example.rickandmorty.data

import com.example.rickandmorty.network.Episode
import com.example.rickandmorty.network.Character
import com.example.rickandmorty.network.CharactersApiService

interface CharactersRepository {
    suspend fun getCharactersList(page: Int = 1, species: String? = null): List<Character>
    suspend fun getCharacter(id: Int): Pair<Character, List<Episode>>
}

class NetworkCharactersRepository(
    private val charactersApiService: CharactersApiService
) : CharactersRepository {

    override suspend fun getCharactersList(page: Int, species: String?): List<Character> =
        charactersApiService.getCharacters(page = page, species = species).results

    override suspend fun getCharacter(id: Int): Pair<Character, List<Episode>> {
        val character = charactersApiService.getCharacterById(id)
        val episodeIds = character.episode.mapNotNull { episodeURL ->
            episodeURL.substringAfterLast("/").toIntOrNull()
        }
        val episodes = when {
            episodeIds.isEmpty() -> emptyList()
            episodeIds.size == 1 -> listOf(charactersApiService.getEpisodeById(episodeIds.first()))
            else -> charactersApiService.getEpisodesByIds(episodeIds.joinToString(","))
        }
        return Pair(character, episodes)
    }
}