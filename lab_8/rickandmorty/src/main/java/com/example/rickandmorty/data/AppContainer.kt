package com.example.rickandmorty.data

import com.example.rickandmorty.network.CharactersApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val charactersRepository: CharactersRepository
}
class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://rickandmortyapi.com/api/"

    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()
    private val retrofitService: CharactersApiService by lazy {
        retrofit.create(CharactersApiService::class.java)
    }
    override val charactersRepository: CharactersRepository by lazy {
        NetworkCharactersRepository(retrofitService)
    }
}