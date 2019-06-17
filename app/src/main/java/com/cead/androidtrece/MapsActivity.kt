package com.cead.androidtrece

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions




/*

        1. CREAMOS UNA ACTIVIDAD TIPO MAPA
        2. LUEGO EN EL ARCHIVO google_maps_api.xml ABRIMOS LA URL DE LA LINEA 7  https://console.developers.google.com/flows/enableapi?apiid=maps_android_backend&keyType=CLIENT_SIDE_ANDROID&r=35:F4:E6:54:C1:4D:2F:BD:82:1D:BA:53:EB:72:4E:66:32:7F:D5:69%3Bcom.cead.androidtrece
        3. CREAMOS LA CLAVE API, COPIAMOS EL KEY EN <string name="google_maps_key" translatable="false" templateMergeStrategy="preserve">YOUR_KEY_HERE</string> EN YOUR_KEY_HERE
 */

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)            /*object: OnMapReadyCallback {
            override fun onMapReady(p0: GoogleMap?) {

            }

        })*/
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // nos permite acotar el zoom y trabaja en un area especifica, alejarnos ni acercarnos
        mMap.setMinZoomPreference(10.toFloat())
        mMap.setMaxZoomPreference(15.toFloat())


        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        val cdmx = LatLng(19.4284706, -99.1276627)
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(cdmx).title("Marker in CDMX").draggable(true)) // añadir el marcador a sydney - draggable es para que se pueda mover el punto
        var camera:CameraPosition = CameraPosition.builder()
                                    .target(cdmx)
                                    .zoom(10.toFloat()) // 1 vista del mundo, 5 masa de tierra, 10 ciudad, 15 calles, 20 edificios  limite 21
                                    .bearing(90.toFloat())  // orientacion de la camara hacia el este limit 0 a 365
                                    .tilt(90.toFloat()) // tilt de la camara efecto 3D hasta 90
                                    .build()
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(cdmx)) // camara apunte a sydney
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera))

        mMap.setOnMapClickListener(object: GoogleMap.OnMapClickListener{
            override fun onMapClick(latlng: LatLng?) {
                Toast.makeText(this@MapsActivity, "click on: \n"+ "Lat: "+ latlng!!.latitude +" long: "+latlng!!.longitude, Toast.LENGTH_SHORT).show()
            }

        })

        mMap.setOnMapLongClickListener (object : GoogleMap.OnMapLongClickListener{
            override fun onMapLongClick(latlng: LatLng?) {
                Toast.makeText(this@MapsActivity, "long click on: \n"+ "Lat: "+ latlng!!.latitude +" long: "+latlng!!.longitude, Toast.LENGTH_SHORT).show()
            }
        })

        mMap.setOnMarkerDragListener( object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragEnd(p0: Marker?) {

            }

            override fun onMarkerDragStart(p0: Marker?) {

            }

            override fun onMarkerDrag(marker: Marker?) {

                Toast.makeText(this@MapsActivity, "onMarkerDrag: \n"+ "Lat: "+ marker!!.position.latitude +" long: "+marker!!.position.longitude, Toast.LENGTH_SHORT).show()


            }

        })
    }
}
