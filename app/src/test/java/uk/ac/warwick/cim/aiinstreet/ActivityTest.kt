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
        val latitude: Double = 0.00;
        val longitude: Double = 25.45;
        val newLocation = Location("newlocation")
        newLocation.setLatitude(latitude)
        newLocation.setLongitude(longitude)

        val latitude1: Double = 0.00;
        val longitude1: Double = 25.45;
        val newLocation1 = Location("newlocation")
        newLocation1.setLatitude(latitude1)
        newLocation1.setLongitude(longitude1)

    }
}