package uk.ac.warwick.cim.aiinstreet

import android.location.Location
import android.location.LocationManager
import junit.framework.TestCase.assertEquals
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
        val testLocation2 = Location(LocationManager.GPS_PROVIDER)
        testLocation2.latitude = 53.410
        testLocation2.longitude = -2.522

        val d = dist.distanceTo(testLocation2)
        assert(!d)
    }

    @Test
    fun testLocationsFindLocation() {
        val dist = Distance()
        val locations: MutableList<AudioLocations> = mutableListOf()
        locations.add(AudioLocations(52.456, -1.234, "exam", "test"))
        locations.add(AudioLocations(51.987, -1.296, "exam", "test"))

        val foundList = dist.findLocations(locations, 52.456, -1.234)
        assertEquals(foundList.size, 1)
        assertEquals(foundList[0].audioUrl, "exam")
    }

    @Test
    fun testLocationsNotFindLocation() {
        val dist = Distance()
        val locations: MutableList<AudioLocations> = mutableListOf()
        locations.add(AudioLocations(52.456, -1.234, "exam", "test"))
        locations.add(AudioLocations(51.987, -1.296, "exam", "test"))

        val foundList = dist.findLocations(locations, 52.45, -1.238)
        assertEquals(foundList.size, 0)
    }

    @Test
    fun testLocationsFindLocationText() {
        val dist = Distance()
        val locations: MutableList<AudioLocations> = mutableListOf()
        locations.add(AudioLocations(52.456, -1.234, "exam", "test"))
        locations.add(AudioLocations(51.987, -1.296, "exam", "test"))

        val foundList = dist.locationText(52.456, -1.234, locations)
        assertEquals(foundList, "test")
    }

    @Test
    fun testLocationsFindLocationUrl() {
        val dist = Distance()
        val locations: MutableList<AudioLocations> = mutableListOf()
        locations.add(AudioLocations(52.456, -1.234, "exam", "test"))
        locations.add(AudioLocations(51.987, -1.296, "exam", "test"))

        val foundList = dist.locationText(52.456, -1.234, locations)
        assertEquals(foundList, "exam")
    }
}