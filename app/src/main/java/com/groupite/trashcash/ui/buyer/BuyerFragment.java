package com.groupite.trashcash.ui.buyer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.groupite.trashcash.R;
import com.groupite.trashcash.helpers.UserType;
import com.groupite.trashcash.helpers.adapters.CustomAdapter;
import com.groupite.trashcash.models.BuyerModel;
import com.groupite.trashcash.models.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import prihanofficial.com.kokis.logics.Kokis;


public class BuyerFragment extends Fragment {

    DatabaseReference root;
    DatabaseReference userDatabaseReference,paperDatabaseReference, plasticDatabaseReference, metalDatabaseReference;
    Context context;

    ArrayList<BuyerModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        root = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference = root.child("user");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_buyer, container, false);

      String userType = Kokis.getKokisString("user_type", " ");
if( userType.equalsIgnoreCase(UserType.SELLER.toString())) {

    //
    listView = (ListView) root.findViewById(R.id.list);
    getUser();
//        adapter= new CustomAdapter(dataModels,context);
//
//        listView.setAdapter(adapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            BuyerModel dataModel = dataModels.get(position);

            Snackbar.make(view, dataModel.getUser().getUserName(), Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
        }
    });
    //
}else {
    new MaterialAlertDialogBuilder(context)
            .setTitle("Alert")
            .setMessage("you do not have permission to access this functionality...")
            .setPositiveButton("Ok", /* listener = */ null)
            .show();
}
        return root;
    }


    private void getUser(){
        Query checkUser = userDatabaseReference.orderByChild("userType").equalTo(UserType.BUYER.toString());
        dataModels= new ArrayList<>();
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    User user = null;
                    dataModels = new ArrayList<>();
                    dataModels.clear();
                    for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                        user   = userSnapShot.getValue(User.class);
                        BuyerModel buyerModel = new BuyerModel();
                        buyerModel.setUser(user);
                        dataModels.add(buyerModel);
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
        adapter= new CustomAdapter(dataModels,context);
        listView.setAdapter(adapter);
    }

}
