package uk.ac.warwick.cim.aiinstreet

import android.location.Location
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ActivityTest {


    @Test
    fun location_nearPoint() {
        val latitude = 0.00
        val longitude = 25.45
        val newLocation = Location("newLocation")
        newLocation.latitude = latitude
        newLocation.longitude = longitude

        val latitude1 = 0.00
        val longitude1 = 25.45
        val newLocation1 = Location("newLocation1")
        newLocation1.latitude = latitude1
        newLocation1.longitude = longitude1

    }
}