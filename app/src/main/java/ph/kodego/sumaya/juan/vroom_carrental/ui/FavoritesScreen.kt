package ph.kodego.sumaya.juan.vroom_carrental.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ph.kodego.sumaya.juan.vroom_carrental.R
import ph.kodego.sumaya.juan.vroom_carrental.model.Car
import ph.kodego.sumaya.juan.vroom_carrental.model.getCarById
import ph.kodego.sumaya.juan.vroom_carrental.theme.DarkBlue
import ph.kodego.sumaya.juan.vroom_carrental.theme.Typography
import ph.kodego.sumaya.juan.vroom_carrental.theme.White

@Composable
fun FavoritesScreen(navController: NavController) {
    Box(modifier = Modifier
        .background(White)
        .fillMaxSize(),
        contentAlignment = Alignment.TopStart)
    {
        Column {
            TopMenu(navController)
            SavedVehicles(navController)
        }
        BottomMenu(
            items = getBottomMenuItems(),
            modifier = Modifier
                .align(Alignment.BottomCenter),
            navController = navController
        )
    }
}

@Composable
fun SavedVehicles(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("fave_cars", Context.MODE_PRIVATE)

    val favoriteCars: Map<String, *> = sharedPreferences.all

    val isFavoriteCars = favoriteCars.filter { car ->
        car.key.startsWith("car_") && car.value is Boolean && car.value as Boolean
    }.mapNotNull { car ->
        car.key.removePrefix("car_").toIntOrNull()?.let { carId -> getCarById(carId) }
    }

    if (isFavoriteCars.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 50.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp, bottom = 50.dp)
        ) {
            items(isFavoriteCars.size) { car ->
                FavoriteCarList(car = isFavoriteCars[car], navController = navController)
            }
        }
    } else {
        NoSavedVehicles(navController)
    }
}

@Composable
fun FavoriteCarList(car: Car, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .background(Color.LightGray)
        ) {
            Image(
                painter = painterResource(car.imageId),
                contentDescription = "${car.carName} image",
                modifier = Modifier
                    .padding(20.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds,
            )
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = car.manufacturer,
                    style = Typography.h5
                )
                Text(
                    text = car.carName,
                    style = Typography.h5
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "View Details",
                    color = White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable (onClick = { navController.navigate(
                            "car_details_screen/${car.carId}")})
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkBlue)
                        .padding(vertical = 6.dp, horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
fun NoSavedVehicles(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.vroom_logo),
            contentDescription = "Car Icon",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "You have not saved any vehicles yet.",
            style = Typography.body1
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("all_cars_screen") },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = DarkBlue
            )
        ) {
            Text(text = "Check All Vehicles")
        }
    }
}


//@Preview
//@Composable
//fun noSavedVehiclesPreview() {
//    noSavedVehicles()
//}