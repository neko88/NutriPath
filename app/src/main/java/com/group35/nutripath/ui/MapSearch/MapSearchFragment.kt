package com.group35.nutripath

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.group35.nutripath.ui.MapSearch.GroceryStore
import com.group35.nutripath.ui.MapSearch.GroceryStoreAdapter
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MapSearchFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var groceryStoreAdapter: GroceryStoreAdapter
    private val groceryStores = mutableListOf<GroceryStore>()
    private lateinit var currentLatLng: LatLng

    companion object {
        const val FINE_PERMISSION_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        //setting up RecyclerView for grocery stores
        val groceryListView = view.findViewById<RecyclerView>(R.id.grocery_list)
        groceryListView.layoutManager = LinearLayoutManager(requireContext())

        //updated onItemClick to move map
        groceryStoreAdapter = GroceryStoreAdapter(groceryStores) { latLng ->
            moveToStoreAndDrawRoute(latLng)
        }

        groceryListView.adapter = groceryStoreAdapter

        //creating map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getCurrLocation()
    }

    private fun getCurrLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(MarkerOptions().position(currentLatLng).title("My Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                    findNearbyGroceryStores(currentLatLng)
                }
            }
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrLocation()
            } else {
                showToast("Location permission is required to use this feature.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun findNearbyGroceryStores(currentLatLng: LatLng) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PlacesApiService::class.java)
        val locationString = "${currentLatLng.latitude},${currentLatLng.longitude}"
        val apiKey = getString(R.string.mapAPI)

        val call = service.getNearbyPlaces(
            location = locationString,
            radius = 2000, // 2km radius
            keyword = "grocery store",
            apiKey = apiKey
        )

        call.enqueue(object : retrofit2.Callback<NearbySearchResponse> {
            override fun onResponse(call: Call<NearbySearchResponse>, response: retrofit2.Response<NearbySearchResponse>) {
                if (response.isSuccessful) {
                    val nearbyPlaces = response.body()?.results ?: emptyList()
                    groceryStores.clear()

                    for (place in nearbyPlaces) {
                        val name = place.name
                        val latLng = LatLng(place.geometry.location.lat, place.geometry.location.lng)

                        groceryStores.add(GroceryStore(name, latLng))
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(name)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        )
                        Log.d("MapSearch", "Added grocery store: $name at $latLng")
                    }
                    groceryStoreAdapter.notifyDataSetChanged()
                    Log.d("MapSearch", "RecyclerView updated with ${groceryStores.size} grocery stores")
                } else {
                    Log.e("MapSearch", "Failed to retrieve grocery stores: ${response.errorBody()}")
                    showToast("Failed to retrieve grocery stores.")
                }
            }

            override fun onFailure(call: Call<NearbySearchResponse>, t: Throwable) {
                Log.e("MapSearch", "Error fetching grocery stores: ${t.message}")
                showToast("Error fetching grocery stores.")
            }
        })
    }

    //trying to implement the drawing direction features
    private fun moveToStoreAndDrawRoute(storeLatLng: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(storeLatLng, 15f))
        getDirectionsToStore(storeLatLng)
    }

    private fun getDirectionsToStore(destinationLatLng: LatLng) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PlacesApiService::class.java)
        val origin = "${currentLatLng.latitude},${currentLatLng.longitude}"
        val destination = "${destinationLatLng.latitude},${destinationLatLng.longitude}"
        val apiKey = getString(R.string.mapAPI)

        val call = service.getDirections(
            origin = origin,
            destination = destination,
            apiKey = apiKey
        )

        call.enqueue(object : retrofit2.Callback<DirectionsResponse> {
            override fun onResponse(call: Call<DirectionsResponse>, response: retrofit2.Response<DirectionsResponse>) {
                if (response.isSuccessful) {
                    val route = response.body()?.routes?.firstOrNull()
                    val polylinePoints = route?.overview_polyline?.points

                    if (polylinePoints != null) {
                        val decodedPath = PolyUtil.decode(polylinePoints)
                        mMap.addPolyline(PolylineOptions().addAll(decodedPath))
                    }
                } else {
                    Log.e("MapSearch", "Failed to retrieve directions: ${response.errorBody()}")
                    showToast("Failed to retrieve directions.")
                }
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                Log.e("MapSearch", "Error fetching directions: ${t.message}")
                showToast("Error fetching directions.")
            }
        })
    }
}


interface PlacesApiService {
    @GET("place/nearbysearch/json")
    fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("keyword") keyword: String,
        @Query("key") apiKey: String
    ): Call<NearbySearchResponse>

    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): Call<DirectionsResponse>
}

data class DirectionsResponse(val routes: List<Route>)
data class Route(val overview_polyline: Polyline)
data class Polyline(val points: String)

data class NearbySearchResponse(val results: List<PlaceResult>)
data class PlaceResult(val name: String, val geometry: Geometry)
data class Geometry(val location: LocationLatLng)
data class LocationLatLng(val lat: Double, val lng: Double)
