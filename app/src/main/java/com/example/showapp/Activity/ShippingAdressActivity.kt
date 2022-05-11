package com.example.showapp.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.showapp.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_shipping_adress.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class ShippingAdressActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var mMap: GoogleMap? = null
    internal lateinit var mLastLocation: Location
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    var positionGps : MutableList<Double>? = ArrayList()
    private var takePos: Button?= null
    lateinit var mSharedPrefUser: SharedPreferences
    var currentLocation : Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    val REQUEST_CODE = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping_adress)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fetchLocation()
        takePos = findViewById(R.id.button1)
        //Log.i("type d'objet de google maps a ajouter article",type)

BottomSheetBehavior.from(sheet).apply {
    peekHeight= 150
    this.state = BottomSheetBehavior.STATE_COLLAPSED
    takePos!!.setOnClickListener {

        mSharedPrefUser = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        //mSharedPrefUser.edit().remove("lat").commit()
        //mSharedPrefUser.edit().remove("long").commit()
        Log.i("lat: ", mSharedPrefUser.getString("lat", null).toString())
        Log.i("long: ", mSharedPrefUser.getString("long", null).toString())
        val gcd = Geocoder(applicationContext, Locale.getDefault())
        val addresses = gcd.getFromLocation(positionGps!![0], positionGps!![1], 1)
        if (addresses.size > 0) {
            println("waaaaaaaaaaaaaaaaaaaa"+addresses[0].locality)
        } else {
            // do your stuff
        }
        mSharedPrefUser.edit().apply {
            putString("position", positionGps.toString())
            putString("cityName", addresses[0].locality)

        }.apply()
        Log.i("position !!!!", mSharedPrefUser.getString("position", null).toString())
//        val intent = Intent(applicationContext, UserActivity::class.java)
//        startActivity(intent)
        finish()
    }
}

        //val mapFragment = supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment
        //mapFragment.getMapAsync(this)

    }
    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }

        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null){
                currentLocation = location
                val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                positionGps!!.add(latLng.latitude)
                positionGps!!.add(latLng.longitude)
                Log.d("position selectionne : ", latLng.toString())

                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment?)
                supportMapFragment!!.getMapAsync(this@ShippingAdressActivity)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
        } else {
            buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }
        // change the location of the current location button in the map
        /*val locationButton = (myMap.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(Integer.parseInt("2"))
        val rlp =  locationButton.getLayoutParams() as RelativeLayout.LayoutParams
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 50, 150)*/

        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I Am Here!")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        googleMap.addMarker(markerOptions)
        mMap!!.setOnMyLocationButtonClickListener(GoogleMap.OnMyLocationButtonClickListener {
            mMap!!.clear()
            mMap!!.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Marked by You")
            )
            positionGps!!.clear()
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            Log.d("position selectionne : ", latLng.toString())
            positionGps!!.add(latLng.latitude)
            positionGps!!.add(latLng.longitude)
        })
        mMap!!.setOnMapClickListener(GoogleMap.OnMapClickListener { latLng ->
            mMap!!.clear()
            mMap!!.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Marked by You")
            )
            positionGps!!.clear()
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            Log.d("position selectionne : ", latLng.toString())
            positionGps!!.add(latLng.latitude)
            positionGps!!.add(latLng.longitude)

            Log.i("nouvelle position : ", positionGps.toString())
        })
    }

    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }

        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)

        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(11f))

        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnected(p0: Bundle?) {

        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    fun searchLocation(view: View) {
        val locationSearch: EditText = findViewById(R.id.et_search)
        var location: String
        location = locationSearch.text.toString().trim()
        var addressList: List<Address>? = null

        if (location == null || location == "") {
            Toast.makeText(this, "provide location", Toast.LENGTH_SHORT).show()
        } else {
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(location, 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val address = addressList!![0]
            val latLng = LatLng(address.latitude, address.longitude)
            mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}