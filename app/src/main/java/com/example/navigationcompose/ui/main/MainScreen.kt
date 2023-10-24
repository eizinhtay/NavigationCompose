package com.example.navigationcompose.ui.main

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navigationcompose.R
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.*
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

val bottomNavigationItems = listOf(
    BottomNavigationScreens.Frankendroid,
    BottomNavigationScreens.Pumpkin,
    BottomNavigationScreens.Ghost,
    BottomNavigationScreens.ScaryBag
)
sealed class BottomNavigationScreens(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Frankendroid : BottomNavigationScreens("Frankendroid", R.string.frankendroid_route, Icons.Filled.AccountBox)
    object Pumpkin : BottomNavigationScreens("Pumpkin", R.string.pumpkin_screen_route, Icons.Filled.AccountBox)
    object Ghost : BottomNavigationScreens("Ghost", R.string.ghost_screen_route, Icons.Filled.AccountBox)
    object ScaryBag : BottomNavigationScreens("ScaryBag", R.string.scary_bag_screen_route, Icons.Filled.AccountBox)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val navController = rememberNavController()

    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Frankendroid,
        BottomNavigationScreens.Pumpkin,
        BottomNavigationScreens.Ghost,
        BottomNavigationScreens.ScaryBag
    )
    Scaffold(
        bottomBar = {
            SpookyAppBottomNavigation(navController, bottomNavigationItems)
        },
    ) {
        MainScreenNavigationConfigurations(navController)
    }
}


@Composable
private fun SpookyAppBottomNavigation(
    navController: NavHostController,
    items: List<BottomNavigationScreens>
) {
    BottomNavigation {
        val currentRoute = currentRoute(navController)
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon,"", modifier = Modifier) },
                label = { Text(stringResource(id = screen.resourceId)) },
                selected = currentRoute == screen.route,
                onClick = {
                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route)
                    }
                }
            )
        }
    }
}





@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val KEY_ROUTE="101"
    return navBackStackEntry?.arguments?.getString(KEY_ROUTE)
}


@Composable
private fun MainScreenNavigationConfigurations(
    navController: NavHostController
) {
    NavHost(navController,
        startDestination = BottomNavigationScreens.Frankendroid.route) {
        composable(BottomNavigationScreens.Frankendroid.route) {
            ScaryScreen(ScaryAnimation.Frankendroid)
        }
        composable(BottomNavigationScreens.Pumpkin.route) {
            ScaryScreen(ScaryAnimation.Pumpkin)
        }
        composable(BottomNavigationScreens.Ghost.route) {
            ScaryScreen(ScaryAnimation.Ghost)
        }
        composable(BottomNavigationScreens.ScaryBag.route) {
            ScaryScreen(ScaryAnimation.ScaryBag)
        }
    }
}

sealed class ScaryAnimation(val animId: Int){
    object Frankendroid: ScaryAnimation(R.raw.frankensteindroid)
    object Pumpkin: ScaryAnimation(R.raw.jackolantern)
    object Ghost: ScaryAnimation(R.raw.ghost)
    object ScaryBag: ScaryAnimation(R.raw.bag)
}


@Composable
fun ScaryScreen(
    scaryAnimation: ScaryAnimation
) {
    val context = LocalContext.current
    val customView = remember { LottieAnimationView(context) }
    // Adds view to Compose
    AndroidView({ customView },
        modifier = Modifier.background(Color.Black)
    ) { view ->
        // View's been inflated - add logic here if necessary
        with(view) {
            setAnimation(scaryAnimation.animId)
            playAnimation()
            repeatMode = LottieDrawable.REVERSE
        }
    }
}
