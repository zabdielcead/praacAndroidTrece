package com.cead.androidtrece.fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentContainer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.cead.androidtrece.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.jar.Manifest


class BlankFragment : Fragment() , OnMapReadyCallback,  View.OnClickListener, GoogleMap.OnMapClickListener {



    private  val LOCATION_PERMISSION_REQUEST_CODE = 1
    var viewrootView: View ? = null
    var gmap: GoogleMap? = null
    var mapView: MapView? = null
    var address: List<Address> ? = null
    var geocoder: Geocoder? = null

    var markers: Marker? = null    // customizar el marcador del mapa

    var fab: FloatingActionButton? = null

    var currentLocation: Location ? = null
    var locationManager: LocationManager ? = null

    var cameraZoom : CameraPosition ?= null



    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        viewrootView = inflater.inflate(R.layout.fragment_blank, container, false)
        fab = viewrootView!!.findViewById(R.id.fabBtnDos)

        fab!!.setOnClickListener(this)
        return viewrootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView =  viewrootView!!.findViewById(R.id.mapDos)
        if(mapView != null) {
            mapView!!.onCreate(null)
            mapView!!.onResume()
            mapView!!.getMapAsync(this)
        }


    }

    override fun onResume() {
        super.onResume()
    }


    private fun isGPSEnabled(): Boolean{




        var  manager: LocationManager =  activity!!.getSystemService( Context.LOCATION_SERVICE ) as LocationManager

        var isGpsOn: Boolean = manager.isProviderEnabled( LocationManager.GPS_PROVIDER )
        if( isGpsOn){

            Toast.makeText(context, "prendido gps", Toast.LENGTH_LONG).show()
            return true
        }else{
            Toast.makeText(context, "apagado gps", Toast.LENGTH_LONG).show()
           // showAlertInfo()
            return false

        }


        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Settings.Secure.getInt(activity!!.contentResolver, Settings.Secure.LOCATION_MODE);
        }else {
            Settings.Secure.getInt(activity!!.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
        }
        */
    }

    private fun showAlertInfo(){
        AlertDialog.Builder(context!!)
            .setTitle("GPS SIGNAL")
            .setMessage("YOU DON´T HAVE GPS SIGNAL ENABLED. WOULD YOU LIKE TO ENABLE THE GPS SIGNAL NOW")
            .setPositiveButton("Save") { dialog, whichButton ->
                var intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("CANCEL", null)
            .show()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap){
        gmap = googleMap
        gmap!!.uiSettings.isZoomControlsEnabled = true
        gmap!!.setOnMapClickListener(this)

        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager


        setUpMap()

        /*
        gmap!!.setOnMyLocationButtonClickListener( object: GoogleMap.OnMyLocationButtonClickListener{
            override fun onMyLocationButtonClick(): Boolean {
                Toast.makeText(context,"boton", Toast.LENGTH_SHORT).show() // toast que se muestra en el mapa el boton gris
                return false
            }

        } )
        */
        // para desaparecer el boton gris que nos lleva a la localizacion
        //gmap!!.uiSettings.isMyLocationButtonEnabled = false
        gmap!!.isMyLocationEnabled = true

        // LocationManager.NETWORK_PROVIDER  Este proveedor determina la ubicación según la disponibilidad de la torre celular y los puntos de acceso WiFi
        // LocationManager.GPS_PROVIDER busca el gps satelital pero por el proveedor de la red
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000.toLong(), 0.toFloat(), object: LocationListener {  //el cero es el minimo de distacia para que se actualice
            override fun onLocationChanged(location: Location?) {
                //Toast.makeText(context,"onLocationChanged "+location!!.provider, Toast.LENGTH_SHORT).show()

                // gmap!!.addMarker(MarkerOptions().position(LatLng(location!!.latitude, location!!.longitude))).setDraggable(true)



            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Toast.makeText(context,"onStatusChanged", Toast.LENGTH_SHORT).show()
            }

            override fun onProviderEnabled(provider: String?) {
                Toast.makeText(context,"onProviderEnabled", Toast.LENGTH_SHORT).show()
            }

            override fun onProviderDisabled(provider: String?) {
                Toast.makeText(context,"onProviderDisabled", Toast.LENGTH_SHORT).show()
            }

        }) // es como el hilo que nos dira cuando se actualiza la ubicacion


    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
    }

    override fun onClick(v: View?) {
        if(!this.isGPSEnabled()){
            showAlertInfo()
        } else {
            if (ActivityCompat.checkSelfPermission(context!!,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    (context as Activity?)!!,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                return
            }
            var location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if(location == null){ // si no funciona locaation
                location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            currentLocation = location

            if(currentLocation != null){
                createOrUpdateMarkerByLocation(location)
                zoomTolocation(location)
            }
        }


    }

    private fun createOrUpdateMarkerByLocation(location : Location){
        if(markers == null){
            var i =  gmap!!.addMarker(MarkerOptions().position(LatLng(location!!.latitude, location!!.longitude)).draggable(true))
            markers = i
        }else{
            markers!!.position = LatLng(location!!.latitude, location.longitude)
        }
    }

    private fun zoomTolocation(location :Location?){

        var cdmx = LatLng(location!!.latitude, location!!.longitude)
        cameraZoom =  CameraPosition.builder()
            .target( cdmx )
            .zoom(18.toFloat())
            .bearing(0.toFloat())
            .tilt(30.toFloat())
            .build()

        gmap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraZoom))
    }

    override fun onMapClick(p0: LatLng?) {
        Toast.makeText(context,"toque el mapa", Toast.LENGTH_SHORT).show()
    }

}
