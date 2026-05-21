package se.id21afm.umu.cs.bouldr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import se.id21afm.umu.cs.bouldr.factories.PlannedRouteViewModelFactory
import se.id21afm.umu.cs.bouldr.ui.screens.HomeScreen
import se.id21afm.umu.cs.bouldr.ui.screens.NewRouteScreen
import se.id21afm.umu.cs.bouldr.ui.screens.PhotoScreen
import se.id21afm.umu.cs.bouldr.ui.screens.PlannedScreen
import se.id21afm.umu.cs.bouldr.ui.screens.RouteDetailScreen
import se.id21afm.umu.cs.bouldr.ui.screens.SettingsScreen
import se.id21afm.umu.cs.bouldr.ui.theme.BouldRTheme
import se.id21afm.umu.cs.bouldr.viewmodel.PlannedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BouldRTheme {
                AppNavigation()
            }
        }
    }
}

/**
 * Composable function that sets up the navigation graph for the application.
 * Defines all routes and their corresponding screens.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = (context.applicationContext as RouteApp).database

    // Helper function to avoid repetition of factory-boiler plate code
    @Composable
    fun plannedViewModel(backStackEntry: androidx.navigation.NavBackStackEntry): PlannedViewModel =
        viewModel(
            viewModelStoreOwner = backStackEntry,
            factory = PlannedRouteViewModelFactory(
                database.plannedRouteDao(),
                backStackEntry.savedStateHandle
            )
        )

    NavHost(navController = navController, startDestination = "home") {

        composable("home") { entry ->
            HomeScreen(navController, plannedViewModel(entry))
        }

        composable("planned") { entry ->
            PlannedScreen(navController, plannedViewModel(entry))
        }

        composable("new_route") { entry ->
            NewRouteScreen(navController, plannedViewModel(entry))
        }

        composable("settings") { entry ->
            SettingsScreen(navController, plannedViewModel(entry))
        }

        composable("photo") {
            PhotoScreen(navController)
        }

        composable(
            route = "route_detail/{routeId}",
            arguments = listOf(navArgument("routeId") { type = NavType.IntType })
        ) { entry ->
            val routeId = entry.arguments?.getInt("routeId") ?: return@composable
            RouteDetailScreen(navController, routeId, plannedViewModel(entry))
        }
    }
}