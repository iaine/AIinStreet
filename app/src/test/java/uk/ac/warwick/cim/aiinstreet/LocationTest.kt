package uk.ac.warwick.cim.aiinstreet

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LocationTest {

    @Test
    fun test_AudioLocation_geographical() {
        val newLocation = AudioLocations(52.456, -1.234, "exam", "test")
        assertEquals(newLocation.lat, 52.456)
        assertEquals(newLocation.long, -1.234)
    }

    @Test
    fun test_AudioLocation_text() {
        val newLocation = AudioLocations(52.456, -1.234, "exam", "test")
        assertEquals(newLocation.audioText, "test")
    }

    @Test
    fun test_AudioLocation_url() {
        val newLocation = AudioLocations(52.456, -1.234, "exam", "test")
        assertEquals(newLocation.audioUrl, "exam")
    }
    
}