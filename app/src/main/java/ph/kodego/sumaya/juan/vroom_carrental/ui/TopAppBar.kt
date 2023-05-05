package ph.kodego.sumaya.juan.vroom_carrental.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ph.kodego.sumaya.juan.vroom_carrental.theme.DarkBlue
import ph.kodego.sumaya.juan.vroom_carrental.theme.Typography

@Composable
fun TopMenu(navController: NavController) {
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("btm_menu_item", Context.MODE_PRIVATE)

    TopAppBar(backgroundColor = DarkBlue) {
        IconButton(onClick = { navController.navigateUp() }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = when (sharedPreferences.getInt("selected_item_index", 0)) {
                1 -> "Favorites"
                2 -> "Bookings"
                3 -> "User Info"
                5 -> "Vroom Finder"
                else -> ""
            },
            style = Typography.body1,
            modifier = Modifier.padding(15.dp)
        )
    }
}

