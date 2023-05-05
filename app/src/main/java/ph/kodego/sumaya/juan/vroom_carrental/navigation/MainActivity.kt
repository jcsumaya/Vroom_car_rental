package ph.kodego.sumaya.juan.vroom_carrental.navigation

import ProfileScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ph.kodego.sumaya.juan.vroom_carrental.model.getCarById
import ph.kodego.sumaya.juan.vroom_carrental.ui.*
import ph.kodego.sumaya.juan.vroom_carrental.theme.VroomCarRentalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VroomCarRentalTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home_screen",
                ) {
                    composable("home_screen") {
                        HomeScreen(navController)
                    }
                    composable("favorites_screen") {
                        FavoritesScreen(navController)
                    }
                    composable("bookings_screen/{carId}") {
                        val context = LocalContext.current
                        val sharedPreferences = context.getSharedPreferences("btm_menu_item", MODE_PRIVATE)
                        sharedPreferences.edit().putInt("selected_item_index", 2).apply()
                        BookingsScreen(navController)
                    }
                    composable("noCarReservation") {
                        NoCarReservation(navController)
                    }
                    composable("profile_screen") {
                        ProfileScreen(navController)
                    }
                    composable("all_cars_screen") {
                        val context = LocalContext.current
                        val sharedPreferences = context.getSharedPreferences("btm_menu_item", MODE_PRIVATE)
                        sharedPreferences.edit().putInt("selected_item_index", 5).apply()
                        AllCarsScreen(navController, chipIndex = -1)
                    }
                    composable("manila_cars") {
                        AllCarsScreen(navController, chipIndex = 0)
                    }
                    composable("laguna_cars") {
                        AllCarsScreen(navController, chipIndex = 1)
                    }
                    composable("bulacan_cars") {
                        AllCarsScreen(navController, chipIndex = 2)
                    }
                    composable("cebu_cars") {
                        AllCarsScreen(navController, chipIndex = 3)
                    }
                    composable("davao_cars") {
                        AllCarsScreen(navController, chipIndex = 4)
                    }
                    composable("car_details_screen/{carId}") { backStackEntry ->
                        val carId = (backStackEntry.arguments?.getString("carId")?.toInt())
                        val car = getCarById(carId!!)
                        CarDetailsScreen(navController, car!!.carId)
                    }
                }
            }
        }
    }
}

