package ph.kodego.sumaya.juan.vroom_carrental.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ph.kodego.sumaya.juan.vroom_carrental.model.Car
import ph.kodego.sumaya.juan.vroom_carrental.model.allCars
import ph.kodego.sumaya.juan.vroom_carrental.model.locations
import ph.kodego.sumaya.juan.vroom_carrental.theme.DarkBlue
import ph.kodego.sumaya.juan.vroom_carrental.theme.LightBlue
import ph.kodego.sumaya.juan.vroom_carrental.theme.White

@Composable
fun AllCarsScreen(navController: NavController, chipIndex: Int) {
    Box(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
    ) {
        Column {
            TopMenu(navController)
            AvailableLocationSection(chips = locations, chipIndex, navController)
            SearchCars(chips = locations, navController = navController)
        }
        BottomMenu(
            items = getBottomMenuItems(),
            modifier = Modifier
                .align(Alignment.BottomCenter),
        navController = navController)
    }
}

@Composable
fun AvailableLocationSection(
    chips: List<String>,
    chipIndex: Int,
    navController: NavController
) {
    var selectedChipIndex by remember {
        mutableStateOf(chipIndex)
    }

    val filteredCars = if (selectedChipIndex >= 0) {
        allCars.filter { it.availableLocations.contains(chips[selectedChipIndex]) } }
    else {
        allCars
    }

    var cars by remember { mutableStateOf(listOf<Car>()) }

    val onUpdateCars: (List<Car>) -> Unit = { updatedCars ->
        cars = filteredCars
    }

    @Composable
    fun updateCarListByChip(navController: NavController) {
        if (selectedChipIndex >= 0) {
            UpdateCarList(cars = cars,
                onUpdateCars = onUpdateCars,
                chips = chips,
                selectedChipIndex = selectedChipIndex,
                navController = navController
            )
        }
    }

    Text(
        text = "Available locations",
        style = MaterialTheme.typography.h6,
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp)
    )
    LazyRow{
        items(chips.size) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                    .clickable {
                        selectedChipIndex = if (selectedChipIndex == it) {
                            -1
                        } else {
                            it
                        }
                    }
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedChipIndex == it) DarkBlue
                        else LightBlue
                    )
                    .padding(15.dp)
            ){
                Text(text = locations[it], color = Color.Black)
            }
        }
    }
    updateCarListByChip(navController)
}

@Composable
fun SearchCars(
    modifier: Modifier = Modifier,
    hint: String = "Search",
    chips: List<String>,
    navController: NavController,
    onQueryChanged: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }

    var cars by remember { mutableStateOf(allCars) }

    val filteredCars = if (query.isBlank()) {
        allCars
    } else {
        allCars.filter { it.carName.contains(query, ignoreCase = true) ||
                it.manufacturer.contains(query, ignoreCase = true)}
    }

    val onUpdateCars: (List<Car>) -> Unit = { updatedCars ->
        cars = filteredCars
    }

    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier,
        value = query,
        maxLines = 1,
        onValueChange = { query = it; onQueryChanged(it) },
        placeholder = { Text(text = hint) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { query = "" }
                ) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear Search Icon")
                }
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onQueryChanged(query)
                // Close keyboard
                focusManager.clearFocus()
            }
        )
    )
    UpdateCarList(cars = cars,
        chips = chips,
        selectedChipIndex = -1,
        navController,
        onUpdateCars = onUpdateCars
    )
}

@Composable
fun CarListItem(car: Car, navController: NavController) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(45.dp))
            .fillMaxWidth()
            .clickable { navController.navigate("car_details_screen/${car.carId}") },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .background(Color.LightGray)
        ) {
            Column(
                modifier = Modifier.padding(30.dp)
            ) {
                Text(
                    text = car.manufacturer,
                    style = MaterialTheme.typography.h5,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = car.carName,
                    style = MaterialTheme.typography.h5,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${car.type} • ${car.transmission}",
                    style = MaterialTheme.typography.body1,
                    color = Color.DarkGray
                )
            }
            Image(
                painter = painterResource(car.imageId),
                contentDescription = "${car.carName} image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentScale = ContentScale.FillBounds,
            )
        }
    }
}

@Composable
fun CarListTitle(chips: List<String>, selectedChipIndex: Int) {
    val location = if (selectedChipIndex >= 0 && selectedChipIndex < chips.size) {
        chips[selectedChipIndex]
    } else {
        "All Locations"
    }
    Text(
        text = "Vroom Cars in $location",
        style = MaterialTheme.typography.h5
    )
}

@Composable
fun UpdateCarList(
    cars: List<Car>,
    chips: List<String>,
    selectedChipIndex: Int,
    navController: NavController,
    onUpdateCars: (List<Car>) -> Unit) {
        Column(
            modifier = Modifier
            .padding(16.dp)
        ) {
            CarListTitle(chips, selectedChipIndex)
            Spacer(modifier = Modifier
                .height(16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                modifier = Modifier.padding(bottom = 50.dp)
            ) {
                items(cars.size) { index ->
                    CarListItem(cars[index], navController)
                }
            }
        }
        onUpdateCars(cars)
}


//@Preview(showBackground = true)
//@Composable
//fun AllCarsScreenPreview() {
//    AllCarsScreen(navController)
//}