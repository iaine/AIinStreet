package uk.ac.warwick.cim.aiinstreet

import android.location.Location
import android.location.LocationManager
import kotlinx.serialization.json.Json
import java.io.File

class Distance {

    fun distanceTo (location: Location): Boolean {
        var near = false


        //val locationObj = this.getLocations()

        val latitude = 52.410825
        val longitude = -1.522639
        val newLocation = Location(LocationManager.GPS_PROVIDER)
        newLocation.latitude = latitude
        newLocation.longitude = longitude
        val results = FloatArray(1)

        Location.distanceBetween(location.latitude, location.longitude, newLocation.latitude, newLocation.longitude, results)
        if (results[0] < 2.5) {
            near = true
        }
        return near
    }

    fun locationAudio(lat: Double, long: Double, locations:MutableList<AudioLocations>):String {
        var url = ""
        val filtered = this.findLocations(locations, lat, long)
        if (filtered.size == 1) {
            url = filtered[0].audioUrl
        }
        return url
    }

    fun locationText (lat: Double, long: Double, locations:MutableList<AudioLocations>): String {
        var text = ""
        val filtered = this.findLocations(locations, lat, long)
        if (filtered.size == 1) {
            text = filtered[0].audioText
        }
        return text
    }

    private fun getLocations (): AudioLocations {
        val filePath = "data.json" // Replace with your JSON file path
        val file = File(filePath)
        val locationJSON = file.readText()
        return Json.decodeFromString<AudioLocations>(locationJSON)
    }

    fun findLocations (aLocation: MutableList<AudioLocations>, lat: Double, long: Double): List<AudioLocations> {
        return aLocation.filter{it.lat == lat && it.long == long}
    }
}