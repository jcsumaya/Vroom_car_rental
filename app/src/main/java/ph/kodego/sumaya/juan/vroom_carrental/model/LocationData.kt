package ph.kodego.sumaya.juan.vroom_carrental.model

import ph.kodego.sumaya.juan.vroom_carrental.R

data class LocationData(val place: String, val imageId: Int, val destination: String, val chipIndex: Int)

fun getLocations(): List<LocationData> {
    return listOf(
        LocationData("Manila", R.drawable.manila, "manila_cars", 0),
        LocationData("Laguna", R.drawable.laguna, "laguna_cars", 1),
        LocationData("Bulacan", R.drawable.bulacan, "bulacan_cars", 2),
        LocationData("Cebu", R.drawable.cebu, "cebu_cars", 3),
        LocationData("Davao", R.drawable.davao, "davao_cars", 4)
    )
}