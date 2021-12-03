package com.example.tweetonmap

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Transformations.map
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), OnMapReadyCallback{

    var addressNew: Address? =null
    private var marker: Marker? = null
    private var mapReadyMarker: Marker? = null
    private var mGoogleMap: GoogleMap? = null
    private var markerOptions: MarkerOptions? = null
    var apiText: String? = null

    private var minLat: Double= -90.00
    private var maxLat: Double= 90.00

    private var minLon: Double= 0.00
    private var maxLon: Double= 180.00
    private var runnable: Runnable? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()


        val supportMapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this@MainActivity)

        et_SearchBar.setOnEditorActionListener { v, actionId, keyEvent ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val searchString = et_SearchBar.text
                if (searchString.isNullOrEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Please Enter Tweet You Want To Search",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    marker?.remove()
                    mapReadyMarker?.remove()


                    loadDataFromTweet(searchString.toString())

                }
            }
            false

        }


    }

    override fun onMapReady(p0: GoogleMap) {

        marker?.remove()

        mGoogleMap = p0

        val sydney = LatLng(-34.0, 151.0)
        markerOptions =MarkerOptions().position(sydney).title("Search Tweets On Map ")
        mGoogleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,5.0f))
        mapReadyMarker = mGoogleMap!!.addMarker(markerOptions)



    }

    private fun geoLocateRandom(title:String){

        var geocoder: Geocoder = Geocoder(this)
        var list: List<Address> = ArrayList()

        try{

            val r = Random()
            var randomLat: Double = minLat + (Math.random() * (maxLat - minLat + 1))
            Log.e("randomLat", randomLat.toString())

            var randomLon: Double = minLon + (Math.random() * (maxLon - minLon + 1))
            Log.e("randomLon", randomLon.toString())

            list = geocoder.getFromLocation(randomLat,randomLon,5)
            Log.e("list", list.toString())

            Log.e("ListSize", list.size.toString())

            var markerNew: Array<Marker?>? = arrayOfNulls(list.size)



            for(i in 0 until list.size){
                addressNew = list[i]

                markerOptions = MarkerOptions().position(LatLng(addressNew!!.latitude, addressNew!!.longitude))
                    .title(title)

                marker = mGoogleMap!!.addMarker(markerOptions)






            }





        }catch (e: Exception){

            Log.e("exception", e.printStackTrace().toString())
            Toast.makeText(applicationContext, e.message + e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }


   /* private fun moveCamera(latLng: LatLng, zoom: Float, title: String){

        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))

        markerOptions = MarkerOptions().position(latLng).title(title)
        marker = mGoogleMap!!.addMarker(markerOptions)


    }*/




    private fun loadDataFromTweet(searchString: String){
        val url ="https://api.twitter.com/2/tweets/search/recent?query=$searchString"

        val jsonObjectRequest= CustomJsonObjectRequestBasicAuth(Request.Method.GET,url,
            null, Response.Listener { response->

                apiText =response.getString("data")


                var jsonArray: JSONArray = JSONArray(apiText)

                Log.e("jsonArray", jsonArray.toString())


                for (i in 0 until jsonArray.length()){

                    var jsonObject: JSONObject =jsonArray.getJSONObject(i)

                    Log.e("jsonObject", jsonObject.toString())

                    apiText = jsonObject.getString("text")

                    Log.e("apiText", apiText.toString())

                    geoLocateRandom(apiText.toString())



                }

                Log.e("Response" , response.toString())


            }, Response.ErrorListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            })

        var requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)

    }

/*
    override fun onMarkerClick(p0: Marker): Boolean {

        mGoogleMap?.setOnInfoWindowClickListener {

            val intent = Intent(this, TweetDisplay::class.java)
            intent.putExtra("Tweet Details", apiText)
            startActivity(intent) }

        return false
    }*/

}