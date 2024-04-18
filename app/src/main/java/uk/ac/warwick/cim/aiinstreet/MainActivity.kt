package uk.ac.warwick.cim.aiinstreet

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.util.Log
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
import com.google.android.gms.location.Priority
import uk.ac.warwick.cim.aiinstreet.ui.theme.AIinStreetTheme
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URL


class MainActivity : ComponentActivity() {


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // globally declare LocationRequest
    //private var locationRequest = LocationRequest()
    /*private var locationRequest = LocationRequest.create().apply {
        // Sets the desired interval for active location updates. This interval is inexact. You
        // may not receive updates at all if no location sources are available, or you may
        // receive them less frequently than requested. You may also receive updates more
        // frequently than requested if other applications are requesting location at a more
        // frequent interval.
        //
        // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
        // targetSdkVersion) may receive updates less frequently than this interval when the app
        // is no longer in the foreground.
        interval = TimeUnit.SECONDS.toMillis(5)

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates more frequently than this value.
        fastestInterval = TimeUnit.SECONDS.toMillis(5)

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        maxWaitTime = TimeUnit.MINUTES.toMillis(2)

        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }*/
    private var locationRequest = LocationRequest.Builder(5000)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .setIntervalMillis(5000)
        .setMinUpdateIntervalMillis(2000)
        .setMaxUpdates(1)
        .build()

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback

    private var requestingLocationUpdates = false

    private var locationText: String? = null

    private var locationUrl: String? = null

    private var audioPlayer = AudioPlayer()

    private lateinit var baseUrl:String

    private val handler = Handler()

    private lateinit var runnable: Runnable

    private lateinit var vibrator: Vibrator

    private val locationPermissionRequest = registerForActivityResult(
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
                    vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

                    val appDir = File(this.getExternalFilesDir(null), "")
                    if (!appDir.exists()) { appDir.mkdirs() }
                    baseUrl = appDir.toString()

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
                        ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                            1)

                        requestingLocationUpdates = true
                    }

                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            location?: return@addOnSuccessListener
                            Log.i("LOCAT", location.toString())
                            locationText = "- @lat: ${location.latitude}\n" +
                                    "- @lng: ${location.longitude}\n"
                        }

                    locationCallback = object : LocationCallback() {

                        override fun onLocationResult(locationResult: LocationResult) {

                            locationText = ""
                            for (location in locationResult.locations){
                                Log.i("LOCATI", location.toString())
                                /*
                                * if (long, lat)
                                * If location matches test
                                 */
                                locationText += "- @lat: ${location.latitude}\n" +
                                        "- @lng: ${location.longitude}\n"
                                playAudio(location)
                            }
                        }
                    }

                    runnable = Runnable {
                        this.startLocationUpdates()
                        handler.postDelayed(runnable,5000)
                    }
                    //write inside onCreate method
                    handler.postDelayed(runnable,5000)
                    //changemessafge based on url state?
                    AudioMessage(name = locationText, url = locationUrl)
                }
            }
        }
    }

    /*@Composable
    fun NoAudioMessage(){
        Column {
            Text(
                text = "Lost Signal",
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }

    @Preview
    @Composable
    fun PreviewNoAudioMessage() {
        NoAudioMessage()
    }*/


    /**
     * AudioMessage is a changeable view object.
     *
     * By default, we have a standard message that shows.
     * When the URL is filled in, a button is shown to play the audio.
     */
    @Composable
    fun AudioMessage(name:String?, url:String?){

        Haptic().vibrate(vibrator, 500)

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text ="$name",
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            //if (url != null) {

                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    Button(
                        onClick = { audioPlayer.play("$baseUrl/test.wav") }, colors = ButtonDefaults.buttonColors(
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
           // }
        }

    }

    @Preview
    @Composable
    fun PreviewAudioMessage(){
        AIinStreetTheme {
            AudioMessage(name = "Here lies static", url = "")
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
     * If close to an audio way point, reset the UI variables
     */

    fun playAudio(locationResult: Location) {
        val dist = Distance()
        val audio = Distance().distanceTo(locationResult!!)

        val allLocations = dist.getLocations()
        if (audio) {
            val lat = locationResult!!
            locationText = dist.locationText (lat.latitude, lat.longitude, allLocations)
            locationUrl = dist.locationAudio(lat.latitude, lat.longitude, allLocations)
        }

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
            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1)

            requestingLocationUpdates = true
            return
        }
        Log.i("LOCAT", "In loop")
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
                Looper.getMainLooper())
        Log.i("LOCAT", "Out loop")
        handler.postDelayed(runnable, 5000)
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