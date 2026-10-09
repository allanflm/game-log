package com.allan.gamelog.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.allan.gamelog.R
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

// Cada rota é um objeto: o compilador garante que não existe rota com nome errado.
@Serializable data object HomeRoute
@Serializable data object SearchRoute
@Serializable data object AddRoute
@Serializable data object ActivityRoute
@Serializable data object ProfileRoute

// As 5 abas da barra inferior, na ordem em que aparecem.
enum class BottomBarDestination(
    val route: Any,
    val routeClass: KClass<*>,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    HOME(HomeRoute, HomeRoute::class, R.string.nav_home, Icons.Filled.Home),
    SEARCH(SearchRoute, SearchRoute::class, R.string.nav_search, Icons.Filled.Search),
    ADD(AddRoute, AddRoute::class, R.string.nav_add, Icons.Filled.Add),
    ACTIVITY(ActivityRoute, ActivityRoute::class, R.string.nav_activity, Icons.Filled.Notifications),
    PROFILE(ProfileRoute, ProfileRoute::class, R.string.nav_profile, Icons.Filled.Person),
}
