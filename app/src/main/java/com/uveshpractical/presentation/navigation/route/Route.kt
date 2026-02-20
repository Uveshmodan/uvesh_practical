package com.uveshpractical.presentation.navigation.route

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    object ListScreen: Route()

    @Serializable
    data class DetailsScreen(val id: Int): Route()
}