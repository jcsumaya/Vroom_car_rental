package ph.kodego.sumaya.juan.vroom_carrental.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.navigation.NavController
import ph.kodego.sumaya.juan.vroom_carrental.R
import ph.kodego.sumaya.juan.vroom_carrental.model.getCarById
import ph.kodego.sumaya.juan.vroom_carrental.theme.DarkBlue
import ph.kodego.sumaya.juan.vroom_carrental.theme.LightBlue
import ph.kodego.sumaya.juan.vroom_carrental.theme.Typography
import ph.kodego.sumaya.juan.vroom_carrental.theme.White
import java.time.LocalDate

@Composable
fun BookingsScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(
        "booking_details", Context.MODE_PRIVATE)

    val pickupDate = sharedPreferences.getString("start_date", "")
        ?.runCatching { LocalDate.parse(this) }
        ?.getOrElse { LocalDate.now() }

    val returnDate = sharedPreferences.getString("end_date", "")
        ?.runCatching { LocalDate.parse(this) }
        ?.getOrElse { LocalDate.now().plusMonths(1) }

    val carDetailsSharedPreferences = context.getSharedPreferences(
        "car_details", Context.MODE_PRIVATE)
    var carId = carDetailsSharedPreferences.getInt("carId", 0)
    val car = getCarById(carId)

    Box(modifier = Modifier
        .background(White)
        .fillMaxSize())
    {
        TopMenu(navController)

        WithCarReservation(
            car!!.manufacturer,
            car.carName,
            car.ratePerDay,
            pickupDate,
            returnDate,
            (returnDate!!.dayOfYear - pickupDate!!.dayOfYear)
                .toDouble() * car.ratePerDay,
            navController)

        BottomMenu(items = getBottomMenuItems(),
            modifier = Modifier
                .align(Alignment.BottomCenter),
            navController = navController)
    }
}

@Composable
fun WithCarReservation(
    manufacturer: String,
    carName: String,
    ratePerDay: Double,
    pickupDate: LocalDate?,
    returnDate: LocalDate?,
    totalAmount: Double,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, 80.dp, end = 16.dp)
    ) {
        Text(
            text = "$manufacturer $carName",
            style = Typography.h4
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Rate per day: $ratePerDay",
            style = Typography.body1
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Pickup date: $pickupDate",
            style = Typography.body1
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Return date: $returnDate",
            style = Typography.body1
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Total amount to pay: $totalAmount",
            style = Typography.body1
        )
        Spacer(modifier = Modifier.height(32.dp))
        val context = LocalContext.current
        Button(
            onClick = {
                context.getSharedPreferences("car_details", Context.MODE_PRIVATE)
                .edit { putInt("carId", 0) }
                navController.navigate("home_screen") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = LightBlue,
                contentColor = White
            )
        ) {
            Text(
                text = "Reserve Now",
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Composable
fun NoCarReservation(navController: NavController) {
    Box(modifier = Modifier
        .background(White)
        .fillMaxSize())
    {
        TopMenu(navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Image(
                painter = painterResource(id = R.drawable.vroom_logo),
                contentDescription = "Car Icon",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No ongoing bookings found",
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

        BottomMenu(items = getBottomMenuItems(),
            modifier = Modifier
                .align(Alignment.BottomCenter),
            navController = navController)
    }
}