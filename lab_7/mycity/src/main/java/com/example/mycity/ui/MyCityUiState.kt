package com.example.mycity.ui

import com.example.mycity.data.DataSource
import com.example.mycity.model.CategoryType
import com.example.mycity.model.Place

data class MyCityUiState(
    val currentCategory: CategoryType = CategoryType.Parks,
    val currentPlacesList: List<Place> = DataSource.getPlacesByCategory(CategoryType.Parks),
    val selectedPlace: Place = currentPlacesList.first()
)