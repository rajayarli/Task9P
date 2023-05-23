package com.example.task9p;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.example.lostandfound.data.DatabaseHelper;
import com.example.lostandfound.model.Item;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DisplayMap extends FragmentActivity implements OnMapReadyCallback {
    List<Item> itemList;  // List of items to display
    List<String> addresses; // List of addresses related to the items
    DatabaseHelper db; // Instance of DatabaseHelper to interact with SQLite database
    GoogleMap map; // GoogleMap instance to display the map
    List<LatLng> latLngList; // List of LatLng objects representing geographical positions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);

        try {
            itemList = new ArrayList<>();
            addresses = new ArrayList<>();
            latLngList = new ArrayList<>();
            db = new DatabaseHelper(DisplayMap.this);

            MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
            mapFragment.getMapAsync(this); // Get map instance asynchronously

            itemList = db.fetchAllItems(); // Fetch all items from database

            // If items are available, extract their locations and convert to LatLng objects
            if(itemList != null) {
                for(int i =0;i<itemList.size();i++) {
                    addresses.add(itemList.get(i).getLocation());
                }
                for(int i =0;i<itemList.size();i++) {
                    latLngList.add(getLocationFromAddress(addresses.get(i)));
                }
            } else {
                addresses = null;
            }
        } catch (Exception e){
            Log.d("reached oc",e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            map = googleMap;

            // Add markers on the map for each LatLng position
            for (int i = 0; i < latLngList.size(); i++) {
                map.addMarker(new MarkerOptions().position(latLngList.get(i)).title("Marker"));
                map.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
                map.moveCamera(CameraUpdateFactory.newLatLng(latLngList.get(i)));
            }
        } catch (Exception e){
            Log.d("reached omr",e.getMessage());
        }
    }

    // Method to get LatLng from address using Geocoder
    public LatLng getLocationFromAddress(String strAddress) {
        try {
            Geocoder coder = new Geocoder(this);
            List<Address> address;
            GeoPoint p1 = null;

            try {
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }
                Address location = address.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }catch (Exception e){
            Log.d("reached glfa",e.getMessage());
            return null;
        }
    }
}
