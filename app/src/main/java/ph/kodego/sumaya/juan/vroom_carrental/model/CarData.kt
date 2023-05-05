package ph.kodego.sumaya.juan.vroom_carrental.model

import ph.kodego.sumaya.juan.vroom_carrental.R

data class Car(val carId: Int,
               val type:String,
               val manufacturer: String,
               val carName: String,
               val transmission: String,
               val ratePerDay: Double,
               val availableLocations: List<String>,
               val imageId: Int)

val locations = getLocations().map { it.place }
    /*
    locations[0] = Manila
    locations[1] = Laguna
    locations[2] = Bulacan
    locations[3] = Cebu
    locations[4] = Davao
     */

val allCars = listOf(
    Car(1, "Sedan","Toyota","Camry","M/T",
        2000.0,
        listOf(locations[1], locations[4]),
        R.drawable.sedan_01),
    Car(2, "Sedan","Toyota","Corolla","A/T",
        2000.0,
        listOf(locations[3]),
        R.drawable.sedan_02),
    Car(3, "Sedan","Toyota","Vios","A/T",
        2000.0,
        listOf(locations[0], locations[1]),
        R.drawable.sedan_03),
    Car(4,"SUV","Toyota", "Fortuner","M/T",
        3000.0,
        listOf(locations[1],locations[2]),
        R.drawable.suv_01),
    Car(5,"SUV", "Mitsubishi", "Montero","A/T",
        3000.0,
        listOf(locations[0],locations[2]),
        R.drawable.suv_02),
    Car(6,"SUV", "Ford", "Everest","A/T",
        3500.0,
        listOf(locations[3],locations[4]),
        R.drawable.suv_03),
    Car(7,"SUV", "Ford", "Explorer","M/T",
        3500.0,
        listOf(locations[0],locations[2]),
        R.drawable.suv_04),
    Car(8,"Van", "Hyundai", "Grand Starex","A/T",
        4500.0,
        listOf(locations[2]),
        R.drawable.van_01),
    Car(9,"Van", "Toyota", "GL","M/T",
        5000.0,
        listOf(locations[1], locations[3]),
        R.drawable.van_02),
    Car(10,"Van", "Toyota", "Super Grandia","A/T",
        5000.0,
        listOf(locations[0], locations[2], locations[3],locations[4]),
        R.drawable.van_03),
    Car(11,"Wagon", "Toyota", "Innova","A/T",
        3000.0,
        listOf(locations[1],locations[2], locations[3],locations[4]),
        R.drawable.wagon_01),
    Car(12,"Coaster", "Hyundai", "County","M/T",
        7500.0,
        listOf(locations[0], locations[4]),
        R.drawable.coaster_01),
    Car(0, "","","","",
        0.0,
        listOf(),
        R.drawable.ic_launcher_foreground)
)

val allCarsMap = allCars.associateBy { it.carId }


val featuredCars = allCars.filter {
    it.carName in listOf(
        "Vios", "Fortuner", "Everest", "Innova", "County"
    )
}

fun getCarById(carId: Int): Car? {
    return allCarsMap[carId]
}
