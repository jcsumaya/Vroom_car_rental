package ph.kodego.sumaya.juan.vroom_carrental.ui

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ph.kodego.sumaya.juan.vroom_carrental.R
import ph.kodego.sumaya.juan.vroom_carrental.model.Car
import ph.kodego.sumaya.juan.vroom_carrental.model.LocationData
import ph.kodego.sumaya.juan.vroom_carrental.model.featuredCars
import ph.kodego.sumaya.juan.vroom_carrental.model.getLocations
import ph.kodego.sumaya.juan.vroom_carrental.theme.DarkBlue
import ph.kodego.sumaya.juan.vroom_carrental.theme.LightBlue
import ph.kodego.sumaya.juan.vroom_carrental.theme.Typography
import ph.kodego.sumaya.juan.vroom_carrental.theme.White

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
    ) {
        Column {
            GreetingSection()
            Divider(color = Color.Black)
            BrowseByLocationSection(getLocations(), navController)
            VroomFinderSection(navController)
            FeaturedCarsSection(featuredCars, navController)
        }
        BottomMenu(items = getBottomMenuItems(),
        modifier = Modifier
            .align(Alignment.BottomCenter),
        navController = navController)
    }
}

@Composable
fun GreetingSection() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE)
    var name = sharedPreferences.getString("name", "")

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Let's Vroom, $name!",
                style = Typography.h3)
            Text(
                text = "Book your self-drive car today.",
                style = MaterialTheme.typography.body1)
        }
        Image(
            painter = painterResource(id = R.drawable.vroom_logo),
            contentDescription = "Vroom Logo",
            modifier = Modifier
                .size(80.dp))
    }
}

@Composable
fun BrowseByLocationSection(location: List<LocationData>, navController: NavController){
    Text(
        text = "Browse by City",
        style = Typography.h4,
        modifier = Modifier
            .padding(start = 15.dp, top = 25.dp))

    LazyRow{
        items(location.size) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                    .clickable(onClick = {navController.navigate(location[it].destination)})
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = location[it].imageId),
                        contentDescription = location[it].place,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.FillBounds
                    )
                    Text(
                        text = location[it].place,
                        color = Color.Black,
                        style = Typography.body1
                        )
                }
            }
        }
    }
}

@Composable
fun VroomFinderSection(navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(DarkBlue)
            .padding(horizontal = 15.dp, vertical = 5.dp)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = "Vroom Finder",
                style = MaterialTheme.typography.h4,
                color = White
            )
            Text(
                text = "Check all available cars for rent",
                style = MaterialTheme.typography.body2,
                color = White
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_24),
            contentDescription = "Next button",
            tint = Color.White,
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .clickable(onClick = {navController.navigate("all_cars_screen")})
        )
    }
}

@Composable
fun FeaturedCarsSection(featuredCars: List<Car>, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Featured Cars",
            style = MaterialTheme.typography.h2,
            modifier = Modifier
                .padding(start = 15.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 100.dp),
            modifier = Modifier
                .fillMaxHeight()
        ){
            items(featuredCars.size) {
                FeaturedItem(featuredCar = featuredCars[it], navController)
            }
        }
    }
}

@Composable
fun FeaturedItem(
    featuredCar: Car,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(color = White)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Image(
            painter = painterResource(featuredCar.imageId),
            contentDescription = "${featuredCar.carName} image",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Text(
                text = "${featuredCar.manufacturer} ${featuredCar.carName}",
                style = MaterialTheme.typography.h4,
                color = Color.White,
                lineHeight = 25.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
            )
            Text(
                text = "Details",
                color = White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable (onClick = { navController.navigate(
                        "car_details_screen/${featuredCar.carId}")})
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(10.dp))
                    .background(LightBlue)
                    .padding(vertical = 6.dp, horizontal = 15.dp)
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview(){
//    HomeScreen()
//}