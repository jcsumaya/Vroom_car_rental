package ph.kodego.sumaya.juan.vroom_carrental.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavController
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import ph.kodego.sumaya.juan.vroom_carrental.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch
import ph.kodego.sumaya.juan.vroom_carrental.model.Car
import ph.kodego.sumaya.juan.vroom_carrental.model.getCarById
import ph.kodego.sumaya.juan.vroom_carrental.theme.*

@Composable
fun CarDetailsScreen(navController: NavController, carId: Int) {
    Box {
        CarDetails(carId, navController)
        BottomMenu(
            items = getBottomMenuItems(),
            modifier = Modifier
                .align(Alignment.BottomCenter),
        navController = navController)
    }
}

@Composable
fun CarDetails(carId: Int, navController: NavController) {
    val car = getCarById(carId)
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val favoriteCarsSharedPreferences = context.getSharedPreferences("fave_cars", Context.MODE_PRIVATE)
    val carDetailsSharedPreferences = context.getSharedPreferences("car_details", Context.MODE_PRIVATE)

    var carIdentifier by remember{ mutableStateOf(carDetailsSharedPreferences.getInt("carId", 0)) }

    if (car != null) {
        carIdentifier = carId

        carDetailsSharedPreferences.edit {
            putInt("carId", car.carId)
        }
    }

    var isFavorite by rememberSaveable(key = "car_$carId") {
        mutableStateOf(favoriteCarsSharedPreferences.getBoolean("car_$carId", false))
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .padding(bottom = 50.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${car?.manufacturer} ${car?.carName}",
                        style = Typography.body1)
                },
                backgroundColor = DarkBlue,
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                CarImage(car = car)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    LaunchedEffect(key1 = isFavorite) {
                        favoriteCarsSharedPreferences.edit {
                            putBoolean("car_$carId", isFavorite)
                        }
                    }
                    Text(
                        text = "${car?.manufacturer} ${car?.carName}",
                        style = MaterialTheme.typography.h4
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = if (!isFavorite) Gray else DarkBlue,
                        modifier = Modifier
                            .clickable {
                                isFavorite = !isFavorite
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        if (isFavorite)
                                            "Added to Favorites"
                                        else
                                            "Removed from Favorites",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                            .size(30.dp)
                    )
                }
                Text(
                    text = "${car?.type} • ${car?.transmission}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                CarDetailsRow(
                    image = painterResource(id = R.drawable.ic_baseline_location_on_24),
                    text = car?.availableLocations!!.joinToString(", ") {it}
                )
                CarDetailsRow(
                    image = painterResource(id = R.drawable.peso),
                    text = "${car.ratePerDay}/ day"
                )

                BookingSection(carId, navController)
                val sharedPreferences = context.getSharedPreferences(
                    "booking_details", Context.MODE_PRIVATE)
            }
        }
    )
}

@Composable
fun CarImage(car: Car?) {
    val image = painterResource(id = car!!.imageId)
    Image(
        painter = image,
        contentDescription = car.carName,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun CarDetailsRow(image: Painter, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = Typography.body1,
            fontSize = 16.sp
        )
    }
}

@Composable
fun BookingSection(carId: Int, navController: NavController) {
    val car = getCarById(carId)
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(
        "booking_details", Context.MODE_PRIVATE)

    var startDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var endDate by remember {
        mutableStateOf(LocalDate.now().plusMonths(1))
    }

    val formattedStartDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(startDate)
        }
    }
    val formattedEndDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(endDate)
        }
    }

    val startDateDialogState = rememberMaterialDialogState()
    val endDateDialogState = rememberMaterialDialogState()

    Column {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Pick up Date: ",
                style = Typography.h5)
            Spacer(
                modifier = Modifier
                    .padding(10.dp))
            Button(
                onClick = { startDateDialogState.show() },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = LightBlue,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Spacer(
                        modifier = Modifier
                            .width(8.dp))
                    Text(
                        text = formattedStartDate,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Return Date: ",
                style = Typography.h5)
            Spacer(
                modifier = Modifier
                .padding(10.dp))
            Button(
                onClick = { endDateDialogState.show() },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = LightBlue,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Spacer(
                        modifier = Modifier
                            .width(8.dp))
                    Text(
                        text = formattedEndDate,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }


    MaterialDialog(
        dialogState = startDateDialogState,
        buttons = {
            positiveButton(
                text = "Select")
            negativeButton(text = "Back")
        },
    ) {
        datepicker(
            initialDate = startDate,
            allowedDateValidator = {
                it.isBefore(endDate.plusDays(1)) && it.isAfter(LocalDate.now())
            }
        ) {
            startDate = it
        }
    }
    MaterialDialog(
        dialogState = endDateDialogState,
        buttons = {
            positiveButton(
                text = "Select")
            negativeButton(text = "Back")
        },
    ) {
        datepicker(
            initialDate = endDate,
            allowedDateValidator = {
                it.isEqual(startDate)
                        || it.isAfter(startDate)
                        && it.isBefore(LocalDate.now().plusMonths(1))
            }
        ) {
            endDate = it
        }
    }
    Button(
        onClick = {
            sharedPreferences.edit {
                putString("start_date_text", startDate.toString())
                putInt("start_date", startDate.dayOfYear)
                putString("end_date_text", endDate.toString())
                putInt("end_date", endDate.dayOfYear)
            }
            navController.navigate("bookings_screen/${car?.carId}")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        colors = ButtonDefaults
            .buttonColors(
                backgroundColor = DarkBlue,
                contentColor = White
            )) {
        Text(text = "Proceed to Booking")
    }
}

//@Preview
//@Composable
//fun CarDetailsScreenPreview() {
//    CarDetailsScreen()
//}