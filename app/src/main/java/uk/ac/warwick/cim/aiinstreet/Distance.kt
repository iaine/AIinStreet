package uk.ac.warwick.cim.aiinstreet

import android.location.Location
import kotlinx.serialization.json.Json
import java.io.File

class Distance {

    fun distanceTo (location: Location?): Boolean {
        var near = false


        //val locationObj = this.getLocations()

        val latitude = 52.410825
        val longitude = -1.522639
        val newLocation = Location("newLocation")
        newLocation.latitude = latitude
        newLocation.longitude = longitude
        if (location?.distanceTo(newLocation)!! < 2.5) {
            near = true
        }
        return near
    }

    fun locationAudio(lat: Double, long: Double):String {
        return "http://blag.com"
    }

    fun locationText (lat: Double, long: Double): String {
        return "set text"
    }

    private fun getLocations (): Locations {
        val filePath = "data.json" // Replace with your JSON file path
        val file = File(filePath)
        val locationJSON = file.readText()
        val locationObject = Json.decodeFromString<Locations>(locationJSON)
        return locationObject
    }
}