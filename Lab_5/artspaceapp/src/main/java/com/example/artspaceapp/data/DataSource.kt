package com.example.artspaceapp.data

import com.example.artspaceapp.R
import com.example.artspaceapp.model.Artwork

object DataSource {
    val Artworks = listOf(
        Artwork(R.drawable.mona_lisa, R.string.title_mona_lisa, R.string.artist_leonardo_da_vinci, R.string.year_1519),
        Artwork(R.drawable.starry_night, R.string.title_starry_night, R.string.artist_vincent_van_gogh, R.string.year_1889),
        Artwork(R.drawable.sunflowers, R.string.title_sunflowers, R.string.artist_vincent_van_gogh, R.string.year_1889),
        Artwork(R.drawable.the_birth_of_venus, R.string.title_the_birth_of_venus, R.string.artist_sandro_botticelli, R.string.year_1486),
    )
}