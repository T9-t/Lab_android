package com.example.mycity.data

import com.example.mycity.R
import com.example.mycity.model.CategoryType
import com.example.mycity.model.Place

object DataSource {
    val allPlaces = listOf(
        Place(
            category = CategoryType.Restaurants,
            title = R.string.restaurants_1_title,
            description = R.string.restaurants_1_desc,
            address = R.string.restaurants_1_address,
            image = R.drawable.ic_restaurants_1
        ),
        Place(
            category = CategoryType.Restaurants,
            title = R.string.restaurants_2_title,
            description = R.string.restaurants_2_desc,
            address = R.string.restaurants_2_address,
            image = R.drawable.ic_restaurants_2
        ),
        Place(
            category = CategoryType.Restaurants,
            title = R.string.restaurants_3_title,
            description = R.string.restaurants_3_desc,
            address = R.string.restaurants_3_address,
            image = R.drawable.ic_restaurants_3
        ),
        Place(
            category = CategoryType.Restaurants,
            title = R.string.restaurants_4_title,
            description = R.string.restaurants_4_desc,
            address = R.string.restaurants_4_address,
            image = R.drawable.ic_restaurants_4
        ),
        Place(
            category = CategoryType.Parks,
            title = R.string.park_1_title,
            description = R.string.park_1_desc,
            address = R.string.park_1_address,
            image = R.drawable.ic_park_1
        ),
        Place(
            category = CategoryType.Parks,
            title = R.string.park_2_title,
            description = R.string.park_2_desc,
            address = R.string.park_2_address,
            image = R.drawable.ic_park_2
        ),
        Place(
            category = CategoryType.Parks,
            title = R.string.park_3_title,
            description = R.string.park_3_desc,
            address = R.string.park_3_address,
            image = R.drawable.ic_park_3
        ),
        Place(
            category = CategoryType.CoffeeShops,
            title = R.string.coffee_1_title,
            description = R.string.coffee_1_desc,
            address = R.string.coffee_1_address,
            image = R.drawable.ic_coffee_1
        ),
        Place(
            category = CategoryType.CoffeeShops,
            title = R.string.coffee_2_title,
            description = R.string.coffee_2_desc,
            address = R.string.coffee_2_address,
            image = R.drawable.ic_coffee_2
        ),
        Place(
            category = CategoryType.CoffeeShops,
            title = R.string.coffee_3_title,
            description = R.string.coffee_3_desc,
            address = R.string.coffee_3_address,
            image = R.drawable.ic_coffee_3
        ),
        Place(
            category = CategoryType.Cinemas,
            title = R.string.cinemas_1_title,
            description = R.string.cinemas_1_desc,
            address = R.string.cinemas_1_address,
            image = R.drawable.ic_cinemas_1
        ),
        Place(
            category = CategoryType.Cinemas,
            title = R.string.cinemas_2_title,
            description = R.string.cinemas_2_desc,
            address = R.string.cinemas_2_address,
            image = R.drawable.ic_cinemas_2
        ),
        Place(
            category = CategoryType.Cinemas,
            title = R.string.cinemas_3_title,
            description = R.string.cinemas_3_desc,
            address = R.string.cinemas_3_address,
            image = R.drawable.ic_cinemas_3
        ),
        Place(
            category = CategoryType.Cinemas,
            title = R.string.cinemas_4_title,
            description = R.string.cinemas_4_desc,
            address = R.string.cinemas_4_address,
            image = R.drawable.ic_cinemas_4
        ),
        Place(
            category = CategoryType.ShoppingMalls,
            title = R.string.malls_1_title,
            description = R.string.malls_1_desc,
            address = R.string.malls_1_address,
            image = R.drawable.ic_malls_1
        ),
        Place(
            category = CategoryType.ShoppingMalls,
            title = R.string.malls_2_title,
            description = R.string.malls_2_desc,
            address = R.string.malls_2_address,
            image = R.drawable.ic_malls_2
        ),
        Place(
            category = CategoryType.ShoppingMalls,
            title = R.string.malls_3_title,
            description = R.string.malls_3_desc,
            address = R.string.malls_3_address,
            image = R.drawable.ic_malls_3
        ),
        Place(
            category = CategoryType.ShoppingMalls,
            title = R.string.malls_4_title,
            description = R.string.malls_4_desc,
            address = R.string.malls_4_address,
            image = R.drawable.ic_malls_4
        )
    )
    fun getPlacesByCategory(category: CategoryType): List<Place> {
        return allPlaces.filter { it.category == category }
    }
}