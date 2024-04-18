package uk.ac.warwick.cim.aiinstreet

import android.location.Location
import android.location.LocationManager

class Distance {

    //@todo refactor this so distance only has the points to save passing object around

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

    fun locationAudio(lat: Double, long: Double, locations: MutableList<AudioLocations>):String {
        var url = ""
        val filtered = this.findLocations(locations, lat, long)
        if (filtered.size == 1) {
            url = filtered[0].audioUrl
        }
        return url
    }

    fun locationText (lat: Double, long: Double, locations: MutableList<AudioLocations>): String {
        var text = ""
        val filtered = this.findLocations(locations, lat, long)
        if (filtered.size == 1) {
            text = filtered[0].audioText
        }
        return text
    }

    fun getLocations (): MutableList<AudioLocations> {
        val locations: MutableList<AudioLocations> = mutableListOf()
        locations.add(AudioLocations(52.41033238626815, -1.5210952754423754, "example", "Barras Lane"))
        locations.add(AudioLocations(52.41108173823987, -1.5231900806341336, "example", "Holyhead Surgery"))
        locations.add(AudioLocations(52.41186052723431, -1.525158822005133, "example", "Rail Station"))
        locations.add(AudioLocations(52.41143126082055, -1.5297539855765452, "example", "Rail Station"))
        return locations
    }

    fun findLocations (aLocation: MutableList<AudioLocations>, lat: Double, long: Double): List<AudioLocations> {
        return aLocation.filter{it.lat == lat && it.long == long}
    }
}