package uk.ac.warwick.cim.aiinstreet

import android.location.Location
import android.location.LocationManager
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DistanceTest {

    @Mock
    private lateinit var testLocation1: Location

    //@Mock

    @Test
    fun testDistanceIsNear() {
        val dist = Distance()
        testLocation1.latitude = 52.410826
        testLocation1.longitude = -1.522640
        assert(dist.distanceTo(testLocation1))
    }

    @Test
    fun testDistanceIsFar() {
        val dist = Distance()
        var testLocation2 = Location(LocationManager.GPS_PROVIDER)
        testLocation2.latitude = 53.410
        testLocation2.longitude = -2.522

        val d = dist.distanceTo(testLocation2)
        println(d)
        assert(!d)
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