package com.example.mycity.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mycity.R

enum class CategoryType(
    @StringRes val title: Int,
    @DrawableRes val image: Int
) {
    Restaurants(
        title = R.string.cat_restaurants,
        image = R.drawable.ic_category_restaurants
    ),
    Parks(
        title = R.string.cat_parks,
        image = R.drawable.ic_category_parks
    ),
    CoffeeShops(
        title = R.string.cat_coffee,
        image = R.drawable.ic_category_coffee
    ),
    Cinemas(
        title = R.string.cat_cinemas,
        image = R.drawable.ic_category_cinemas
    ),
    ShoppingMalls(
        title = R.string.cat_malls,
        image = R.drawable.ic_category_malls
    )
}

data class Place(
    val category: CategoryType,
    @StringRes val title: Int,
    @StringRes val description: Int,
    @StringRes val address: Int,
    @DrawableRes val image: Int
)