package uk.ac.warwick.cim.aiinstreet

import android.location.Location
import org.junit.Test

class DistanceTest {

    @Test
    fun testDistanceIsNear() {
        val dist = Distance()
        val testLocation = Location("newLocation")
        testLocation.latitude = 52.410826
        testLocation.longitude = -1.522640
        assert(dist.distanceTo(testLocation))
    }

    @Test
    fun testDistanceIsFar() {
        val dist = Distance()
        val testLocation = Location("testLocation")
        testLocation.latitude = 52.410826
        testLocation.longitude = -1.522640
        assert(!dist.distanceTo(testLocation))
    }

    @Test
    fun testLocationText() {
        val dist = Distance()
        val lat = 0.00
        val ln = 1.00
        assert(dist.locationText(lat, ln) == "set text")
    }

    @Test
    fun testLocationUrl() {
        val dist = Distance()
        val lat = 0.00
        val ln = 1.00
        assert(dist.locationAudio(lat, ln) == "http://blag.com")
    }
}