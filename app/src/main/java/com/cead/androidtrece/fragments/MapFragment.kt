package com.cead.androidtrece.fragments


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getSystemService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.cead.androidtrece.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, View.OnClickListener {



    var viewrootView: View ? = null
    var gmap: GoogleMap ? = null
    var mapView: MapView ? = null
    var address: List<Address> ? = null
    var geocoder: Geocoder ? = null

    var markers: MarkerOptions ? = null    // customizar el marcador del mapa

    var fab: FloatingActionButton ? = null


    override fun onCreateView(  inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
         viewrootView = inflater.inflate(R.layout.fragment_map, container, false)
        fab = viewrootView!!.findViewById(R.id.fabBtn)
        fab!!.setOnClickListener(this)
        return viewrootView
    }

    override fun onClick(v: View?) {
        checkIFGPSIsEnabled()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = viewrootView!!.findViewById(R.id.map)

        if(mapView != null){
            mapView!!.onCreate(null)
            mapView!!.onResume()
            mapView!!.getMapAsync(this)
        }

    }

    override fun onResume() {
        super.onResume()

    }

    private fun checkIFGPSIsEnabled(){
        var  manager: LocationManager =  activity!!.getSystemService( Context.LOCATION_SERVICE ) as LocationManager

        var isGpsOn: Boolean = manager.isProviderEnabled( LocationManager.GPS_PROVIDER )
        if( isGpsOn){

            Toast.makeText(context, "prendido gps", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context, "apagado gps", Toast.LENGTH_LONG).show()
            showAlertInfo()

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

    override fun onMapReady(googleMap: GoogleMap?) {
        gmap = googleMap
        val cdmx = LatLng(19.4284706, -99.1276627)
        var zoom: CameraUpdate = CameraUpdateFactory.zoomTo(15.toFloat())

        //customizar el marcador
        markers = MarkerOptions()
        markers!!.position(cdmx)
        markers!!.title("Mi marcador")
        markers!!.draggable(true)
        markers!!.snippet("Esto es una caja de texto donde modificara los datos")
        markers!!.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.star_on))

        //
        gmap!!.addMarker(markers)


        //gmap!!.addMarker(MarkerOptions().position(cdmx).title("Marker in CDMX").draggable(true)) // añadir el marcador a sydney - draggable es para que se pueda mover el punto
        gmap!!.moveCamera(CameraUpdateFactory.newLatLng(cdmx))

        gmap!!.animateCamera(zoom)
        gmap!!.setOnMarkerDragListener(this)

        geocoder = Geocoder(context, Locale.getDefault()) //nos da la informacion depende de la latitud y la longitud

    }

    override fun onMarkerDragEnd(marker: Marker?) { // cuando termine de arrastrar el marcador nos llega el marker customizado
        var latitude = marker!!.position.latitude
        var long = marker!!.position.longitude

        address =  geocoder!!.getFromLocation(latitude, long, 1)  // si una localizacion tiene mas de una direccion solo obtenemos 1 la que tenga mas info

        var addresTx = address!!.get(0).getAddressLine(0)
        var city = address!!.get(0).locality
        var state = address!!.get(0).adminArea
        var country = address!!.get(0).countryName
        var postalCode = address!!.get(0).postalCode


        marker!!.snippet = "city: "+ city + "state: "+ state
        /*
        Toast.makeText(context,  "address: "+ addresTx + "\n"+
                                      "city: "+ city + "\n"+
                                      "state: "+ state + "\n"+
                                      "country: "+ country + "\n"+
                                      "postalCode: "+ postalCode + "\n"

                        , Toast.LENGTH_SHORT).show()
        */
        marker!!.showInfoWindow()
    }

    override fun onMarkerDragStart(marker: Marker?) {
        marker!!.hideInfoWindow()
    }

    override fun onMarkerDrag(marker: Marker?) {

    }

}
