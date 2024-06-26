package uk.ac.warwick.cim.aiinstreet

import android.location.Location
import android.location.LocationManager

class Distance {

    private var allLocations: List<AudioLocations>
    init {
        allLocations = this.getLocations()
    }

    fun distanceTo (location: Location): Boolean {
        var near = false

        val newLocation = Location(LocationManager.GPS_PROVIDER)
        newLocation.latitude = location.latitude
        newLocation.longitude = location.longitude

        val results = FloatArray(1)

        for (locations in this.allLocations) {
            Location.distanceBetween(
                locations.lat,
                locations.long,
                newLocation.latitude,
                newLocation.longitude,
                results
            )

            if (results[0] < 5) near = true
        }

        return near
    }

    fun locationAudio(lat: Double, long: Double):String {
        var url = ""
        val filtered = this.findLocations(lat, long)
        if (filtered.size == 1) {
            url = filtered[0].audioUrl
        }
        return url
    }

    fun locationText (lat: Double, long: Double): String {
        var text = ""
        val filtered = this.findLocations(lat, long)
        if (filtered.size == 1) {
            text = filtered[0].audioText
        }
        return text
    }

    fun getLocations (): MutableList<AudioLocations> {

        val locations: MutableList<AudioLocations> = mutableListOf()
        locations.add(AudioLocations(52.41033238626, -1.5210952754423, "example", "Barras Lane"))
        locations.add(AudioLocations(52.41108173823, -1.5231900806341, "example", "Holyhead Surgery"))
        locations.add(AudioLocations(52.41186052723, -1.525158822005, "example", "Rail Station"))
        locations.add(AudioLocations(52.41143126082, -1.5297539855765, "example", "All0tments"))
        locations.add(AudioLocations(51.75487933031, -1.2549123452325, "example", "Weston"))
        locations.add(AudioLocations(51.74459827237, -1.2294136893110, "example", "Magdalen Road"))
        return locations
    }

    fun findLocations (lat: Double, long: Double): List<AudioLocations> {
        return this.allLocations.filter{it.lat == lat && it.long == long}
    }
}
