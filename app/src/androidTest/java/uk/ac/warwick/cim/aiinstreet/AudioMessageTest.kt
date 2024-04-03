package uk.ac.warwick.cim.aiinstreet

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import uk.ac.warwick.cim.aiinstreet.ui.theme.AIinStreetTheme

class AudioMessageTest {

    @get:Rule
    val composeTestRule =  createAndroidComposeRule<MainActivity>()
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity

    @Test
    fun addTextToMessageTest() {
        // Start the app
        composeTestRule.setContent {
            AIinStreetTheme {
                MainActivity().AudioMessage(name = "test", url = "htt" )
            }
        }

        composeTestRule.onNodeWithText("test").assertIsDisplayed()
    }

    @Test
    fun addUrlToMessageForButtonTest() {
        // Start the app
        composeTestRule.setContent {
            MainActivity().AudioMessage(name = "test", url = "htt://example" )
        }

        composeTestRule.onNodeWithText("test").assertExists()

    }

    @Test
    fun clickButtonOnUrl() {
        // Start the app
        composeTestRule.setContent {
            MainActivity().AudioMessage(name = "test", url = "htt://example" )
        }

        composeTestRule.onNodeWithText("test").performClick()

    }
}