package com.wadachirebandi.kiddietrackingadmin.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.wadachirebandi.kiddietrackingadmin.R
import com.wadachirebandi.kiddietrackingadmin.daos.LocationDao
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocClient: FusedLocationProviderClient

    private var liveLocation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupLocClient()
    }

    suspend fun getLiveLocation() {
        LocationDao().locationCollection.get().addOnSuccessListener {
            liveLocation = it["live_location"] as Boolean
            if (liveLocation) {
                setupLocClient()
                getCurrentLocation()
            }
        }
        delay(5000)
        getLiveLocation()
    }

    private fun setupLocClient() {
        fusedLocClient =
            LocationServices.getFusedLocationProviderClient(this)
    }

    private fun requestLocPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION
        )
    }

    companion object {
        private const val REQUEST_LOCATION = 1
        private const val TAG = "MapsActivity"
    }

    private fun getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocClient.lastLocation.addOnCompleteListener {
                val location = it.result
                Log.i(TAG, location.toString())
                LocationDao().locationCollection2.set(location)
            }
        } else {
            requestLocPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.size == 1 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                getCurrentLocation()
            } else {
                Log.e(TAG, "Location permission has been denied")
            }
        }
    }
}