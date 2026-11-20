package com.example.rickandmorty.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersApiService {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1,
        @Query("species") species: String? = null
    ): CharactersResponse

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): Character

    @GET("episode/{id}")
    suspend fun getEpisodeById(@Path("id") id: Int): Episode

    @GET("episode/{ids}")
    suspend fun getEpisodesByIds(@Path("ids") ids: String): List<Episode>
}