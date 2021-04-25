package com.groupite.trashcash;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.groupite.trashcash.helpers.RequestStatus;
import com.groupite.trashcash.helpers.UserType;
import com.groupite.trashcash.models.Order;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

import prihanofficial.com.kokis.logics.Kokis;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap sellerMap;
    private GoogleMap buyerMapMap;

    private String sellerAddress = " ";
    private String buyerAddress = " ";
    private String sellerPhone = " ";
    private String buyerPhone = " ";
    private String orderId = " ";

    ExtendedFloatingActionButton buttonCall, buttonEnd;

    DatabaseReference root;
    DatabaseReference orderDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        root = FirebaseDatabase.getInstance().getReference();
        orderDatabaseReference = root.child("order");

        sellerAddress = getIntent().getStringExtra("sellerAddress");
        buyerAddress = getIntent().getStringExtra("buyerAddress");
        sellerPhone = getIntent().getStringExtra("sellerPhone");
        buyerPhone = getIntent().getStringExtra("buyerPhone");
        orderId = getIntent().getStringExtra("orderID");

        buttonCall = findViewById(R.id.bt_call);
        buttonEnd = findViewById(R.id.bt_end);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder();
            }
        });

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userType = Kokis.getKokisString("user_type", " ");

                if (userType.equalsIgnoreCase(UserType.BUYER.toString())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + buyerPhone));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + sellerPhone));
                    startActivity(intent);
                }
            }
        });
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        sellerMap = googleMap;
        buyerMapMap = googleMap;

        try {
            LatLng sellerLatLng = getLocationFromAddress(this, sellerAddress);
            LatLng buyerLatLng = getLocationFromAddress(this, buyerAddress);

            sellerMap.addMarker(new MarkerOptions().position(sellerLatLng).title("Seller Location").icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            buyerMapMap.addMarker(new MarkerOptions().position(buyerLatLng).title("Buyer Location"));
            float zoomLevel = 25.0f;

            sellerMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sellerLatLng, zoomLevel));
            buyerMapMap.moveCamera(CameraUpdateFactory.newLatLngZoom(buyerLatLng, zoomLevel));



//        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//            @Override
//            public void onMarkerDragStart(Marker arg0) {
//                // TODO Auto-generated method stub
//                Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public void onMarkerDragEnd(Marker arg0) {
//                // TODO Auto-generated method stub
//                Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
//
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
//            }
//
//            @Override
//            public void onMarkerDrag(Marker arg0) {
//                // TODO Auto-generated method stub
//                Log.i("System out", "onMarkerDrag...");
//            }
//        });
//        int x =10;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        Double lat = loc.getLatitude();
        Double lng = loc.getLongitude();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void cancelOrder() {
        Query checkUser = orderDatabaseReference.orderByChild("id").equalTo(orderId);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    Order order = null;
                    for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                        order  = userSnapShot.getValue(Order.class);
                        break;
                    }
                    if (order != null)
                        setInactive(order);
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void setInactive(Order order){
         order.setStatus(RequestStatus.INACTIVE.toString());
         orderDatabaseReference.child(orderId).setValue(order);
         Toast.makeText(this, "successfully updated  ", Toast.LENGTH_SHORT).show();
    }
}