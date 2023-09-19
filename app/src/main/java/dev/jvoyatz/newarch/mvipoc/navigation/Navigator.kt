package dev.jvoyatz.newarch.mvipoc.navigation

import androidx.navigation.NavController

interface Navigator {
    fun navigate(destination: Destination)

    fun bind(navController: NavController)
    fun unbind()
}