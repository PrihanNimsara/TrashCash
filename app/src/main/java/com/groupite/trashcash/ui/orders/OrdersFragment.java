package com.groupite.trashcash.ui.orders;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.groupite.trashcash.R;
import com.groupite.trashcash.helpers.UserType;
import com.groupite.trashcash.helpers.adapters.OrderAdapter;
import com.groupite.trashcash.models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import prihanofficial.com.kokis.logics.Kokis;

public class OrdersFragment extends Fragment {

    DatabaseReference root;
    DatabaseReference orderDatabaseReference;
    Context context;

    ArrayList<Order> orderArrayList;
    ListView listView;
    private static OrderAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        root = FirebaseDatabase.getInstance().getReference();
        orderDatabaseReference = root.child("order");
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        listView = (ListView) root.findViewById(R.id.list);

        String userType = Kokis.getKokisString("user_type", " ");
        String userId =  Kokis.getKokisString("user_id"," ");
        if(userType.equalsIgnoreCase(UserType.AGENT.toString())){
            getDataBuyer(userId);
        }else {
            getDataSeller(userId);
        }
        return root;
    }


    private void getDataSeller(String userId){
        Query checkOrders = orderDatabaseReference.orderByChild("sellerId").equalTo(userId.toString());
        orderArrayList= new ArrayList<>();
        checkOrders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    Order order = null;
                    orderArrayList = new ArrayList<>();
                    orderArrayList.clear();
                    for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                        order   = userSnapShot.getValue(Order.class);
//                        Order orderModel = new Order();
//                        orderModel.set(user);
                        orderArrayList.add(order);
                    }

                    setDataToList();
                } else {
                    //showError();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getDataBuyer(String userId){
        Query checkOrders = orderDatabaseReference.orderByChild("buyerId").equalTo(userId.toString());
        orderArrayList= new ArrayList<>();
        checkOrders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    Order order = null;
                    orderArrayList = new ArrayList<>();
                    orderArrayList.clear();
                    for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                        order   = userSnapShot.getValue(Order.class);
//                        Order orderModel = new Order();
//                        orderModel.set(user);
                        orderArrayList.add(order);
                    }

                    setDataToList();
                } else {
                    //showError();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setDataToList(){
        adapter= new OrderAdapter(orderArrayList,context);
        listView.setAdapter(adapter);
    }


}