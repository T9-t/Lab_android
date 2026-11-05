package com.example.artspaceapp.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Artwork(
    @DrawableRes val imageResourceId: Int,
    @StringRes val titleId: Int,
    @StringRes val artistId: Int,
    @StringRes val yearId: Int
)