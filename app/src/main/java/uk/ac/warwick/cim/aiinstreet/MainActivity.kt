package uk.ac.warwick.cim.aiinstreet

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import uk.ac.warwick.cim.aiinstreet.ui.theme.AIinStreetTheme
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : ComponentActivity() {


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback

    private var requestingLocationUpdates = false

    private var locationText: String? = null

    private var locationUrl: String? = null

    private var audioPlayer = AudioPlayer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIinStreetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //@todo: write this to file and check if points exist.
                    //val url = ""
                    //getPoints(url);


                    val locationPermissionRequest = registerForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) { permissions ->
                        when {
                            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                                // Precise location access granted.
                            }
                            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                                // Only approximate location access granted.
                            } else -> {
                            // No location access granted.
                        }
                        }
                    }
                    locationPermissionRequest.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION))

                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        requestingLocationUpdates = true
                    }
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            location?: return@addOnSuccessListener
                            locationText = "- @lat: ${location.latitude}\n" +
                                    "- @lng: ${location.longitude}\n"
                        }

                    locationCallback = object : LocationCallback() {

                        override fun onLocationResult(locationResult: LocationResult) {

                            locationText = ""
                            for (location in locationResult.locations){
                                // Update UI with location data
                                // ...

                                /*
                                * if (long, lat)
                                * If location matches test
                                 */
                                locationText += "- @lat: ${location.latitude}\n" +
                                        "- @lng: ${location.longitude}\n"
                            }
                        }
                    }
                    AudioMessage(name = locationText, url = locationUrl)
                }
            }
        }
    }


    /**
     * AudioMessage is a changeable view object.
     *
     * By default, we have a standard message that shows.
     * When the URL is filled in, a button is shown to play the audio.
     */
    @Composable
    fun AudioMessage(name:String?, url:String?){
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text ="$name",
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(text="$url")
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (url != "") {
            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Button(
                    onClick = { audioPlayer.play(url) }, colors = ButtonDefaults.buttonColors(
                        Color.Red
                    ), shape = RoundedCornerShape(20.dp)
                ) { Text("Play") }
                Button(
                    onClick = { audioPlayer.pause() }, colors = ButtonDefaults.buttonColors(
                        Color.Red
                    )
                ) { Text("Pause") }
                Button(
                    onClick = { audioPlayer.stop() }, colors = ButtonDefaults.buttonColors(
                        Color.Red
                    ), shape = RoundedCornerShape(50.dp)
                ) { Text("Stop") }
            }
        }
    }

    @Preview
    @Composable
    fun PreviewAudioMessage(){
        AIinStreetTheme {
            AudioMessage(name = "Here lies static", url = "hhy")
        }
    }
    fun getPoints(url: String): String {
        val connection = URL(url).openConnection()
        val reader = BufferedReader(InputStreamReader(connection.getInputStream()))
        val jsonData = StringBuilder()

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            jsonData.append(line)
        }
        reader.close()

        //val jsonObject = JSONObject(jsonData)
        return jsonData.toString()
    }

    /**
     * If close to an audio way point, play audio.
     */
    fun playAudio(locationResult: LocationResult) {
        Distance().distanceTo(locationResult)
    }

    //start location updates
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestingLocationUpdates = true
            return
        }
            fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper())
        }



    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    // start receiving location update when activity  visible/foreground
    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }


}