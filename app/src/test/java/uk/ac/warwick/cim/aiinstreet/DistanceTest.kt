package uk.ac.warwick.cim.aiinstreet

import android.location.Location
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DistanceTest {

    @Mock
    private lateinit var testLocation1: Location

    @Mock
    private lateinit var testLocation2: Location
    @Test
    fun testDistanceIsNear() {
        val dist = Distance()
        Mockito.`when`(testLocation1.latitude).thenReturn(52.410826)
        Mockito.`when`(testLocation1.longitude).thenReturn(-1.522640)
        assert(dist.distanceTo(testLocation1))
    }

    @Test
    fun testDistanceIsFar() {
        val dist = Distance()
        Mockito.`when`(testLocation2.latitude).thenReturn(53.410979)
        Mockito.`when`(testLocation2.longitude).thenReturn(-2.522789)

        assert(dist.distanceTo(testLocation2))
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