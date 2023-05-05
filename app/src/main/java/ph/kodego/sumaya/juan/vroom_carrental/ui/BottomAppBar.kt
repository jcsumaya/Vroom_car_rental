package ph.kodego.sumaya.juan.vroom_carrental.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ph.kodego.sumaya.juan.vroom_carrental.R
import ph.kodego.sumaya.juan.vroom_carrental.model.BottomMenuContent
import ph.kodego.sumaya.juan.vroom_carrental.model.getCarById
import ph.kodego.sumaya.juan.vroom_carrental.theme.DarkBlue
import ph.kodego.sumaya.juan.vroom_carrental.theme.Typography
import ph.kodego.sumaya.juan.vroom_carrental.theme.White

@Composable
fun getBottomMenuItems(): List<BottomMenuContent> {
    val context = LocalContext.current
    val carDetailsSharedPreferences = context.getSharedPreferences(
        "car_details", Context.MODE_PRIVATE)
    val carId = carDetailsSharedPreferences.getInt("carId", 0)
    val car = getCarById(carId)


    return listOf(
        BottomMenuContent("Home", R.drawable.ic_baseline_home_24,
            "home_screen"),
        BottomMenuContent("Favorites", R.drawable.ic_baseline_favorite_24,
            "favorites_screen"),
        BottomMenuContent("Bookings", R.drawable.ic_baseline_car_rental_24,
            "noCarReservation"),
        BottomMenuContent("Profile", R.drawable.ic_baseline_person_24,
            "profile_screen")
    )
}


@Composable
fun BottomMenu(
    items: List<BottomMenuContent>,
    initialSelectedItemIndex: Int = 0,
    modifier: Modifier,
    navController: NavController
) {
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("btm_menu_item", Context.MODE_PRIVATE)

    var selectedItemIndex by remember {
        mutableStateOf(sharedPreferences.getInt("selected_item_index",
                initialSelectedItemIndex
            )
        )
    }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        items.forEachIndexed { index, item ->
            if (destination.route == item.destination) {
                selectedItemIndex = index
                sharedPreferences.edit().putInt("selected_item_index", index).apply()
            }
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().background(DarkBlue)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(DarkBlue)
        ) {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier.clickable {
                        selectedItemIndex = index
                        sharedPreferences.edit().putInt("selected_item_index", index).apply()
                        navController.navigate(item.destination)
                    }
                ) {
                    BottomMenuItem(
                        item = item,
                        isSelected = index == selectedItemIndex,
                        activeTextColor = White,
                        inactiveTextColor = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun BottomMenuItem(
    item: BottomMenuContent,
    isSelected: Boolean = false,
    activeTextColor: Color = White,
    inactiveTextColor: Color = Color.Black,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(CircleShape)
            .padding(start = 20.dp, end = 20.dp, top = 5.dp)
    ) {
        Icon(
            painter = painterResource(id = item.iconId),
            contentDescription = item.title,
            tint = if(isSelected) activeTextColor else inactiveTextColor,
            modifier = Modifier
                .size(25.dp)
        )
        Text(
            text = item.title,
            style = Typography.body2,
            color = if(isSelected) activeTextColor else inactiveTextColor)
    }
}