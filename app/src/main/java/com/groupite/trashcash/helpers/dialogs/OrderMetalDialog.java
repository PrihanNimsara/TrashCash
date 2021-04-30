package com.groupite.trashcash.helpers.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.groupite.trashcash.OrderCreateSuccess;
import com.groupite.trashcash.R;
import com.groupite.trashcash.helpers.RequestStatus;
import com.groupite.trashcash.helpers.WasteType;
import com.groupite.trashcash.models.BuyerModel;
import com.groupite.trashcash.models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import prihanofficial.com.kokis.logics.Kokis;

public class OrderMetalDialog {
    TextView textViewMetalPrice;
    Button buttonApply;
    Button buttonCancel;
    Double totalPrice = null;
    String totalWeightForDb = null;
    DatabaseReference root;
    DatabaseReference orderDatabaseReference;

    OrderCreateSuccess orderCreateSuccess;

    public void showOrderMetalDialog(Context context, String weight, String priceForKg,final String buyerId, final BuyerModel buyerModel,final OrderCreateSuccess interfaceSuccess) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.order_paper_layout);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        orderCreateSuccess = interfaceSuccess;


        textViewMetalPrice = dialog.findViewById(R.id.mtv_price);

        buttonApply = dialog.findViewById(R.id.bt_apply);
        buttonCancel = dialog.findViewById(R.id.bt_cancel);

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(buyerId,buyerModel);
                dialog.dismiss();
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        priceCalculation(weight,priceForKg);
        dialog.show();
    }

    private void saveData(final String buyerId, final BuyerModel buyerModel){
        root = FirebaseDatabase.getInstance().getReference();
        orderDatabaseReference = root.child("order");

        final String userId =  Kokis.getKokisString("user_id"," ");
        String queryParameter = buyerId+"_"+userId+"_"+ WasteType.METAL.toString() +"_"+ RequestStatus.ACTIVE.toString();

        Query checkUser = orderDatabaseReference.orderByChild("buyerId_sellerId_type_status").equalTo(queryParameter);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    // recode exist

                } else {
                    // new recode
                    saveNewData(buyerId,userId,totalPrice,buyerModel);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                //because it is no need to implement
            }
        });
    }

    private void priceCalculation(String totalWeight, String priceForOneKg){
        Double totalWeightInKg = Double.parseDouble(totalWeight);
        Double priceForOneKgInRs = Double.parseDouble(priceForOneKg);

        totalPrice = totalWeightInKg * priceForOneKgInRs;
        totalWeightForDb = totalWeight;
        textViewMetalPrice.setText(totalPrice.toString().trim());
    }

    private void saveNewData(String buyerId, String sellerId, Double price, BuyerModel buyerModel){
        String id = orderDatabaseReference.push().getKey();

        String sellerName = Kokis.getKokisString("first_name"," ");
        String sellerEmail = Kokis.getKokisString("email"," ");
        String sellerPhone = Kokis.getKokisString("phone"," ");
        String sellerAddress = Kokis.getKokisString("address"," ");

        if (id != null) {
            String queryColumn = buyerId+"_"+sellerId+"_"+ WasteType.METAL.toString() +"_"+ RequestStatus.ACTIVE.toString();
            Order order = new Order(id,
                    buyerId,buyerModel.getUser().getFirstName(), buyerModel.getUser().getEmail(),buyerModel.getUser().getPhone(),buyerModel.getUser().getAddress(),
                    sellerId,sellerName,sellerEmail,sellerPhone,sellerAddress,
                    Double.toString(price),totalWeightForDb,WasteType.METAL.toString() ,RequestStatus.ACTIVE.toString(),queryColumn);
            orderDatabaseReference.child(id).setValue(order);


            orderCreateSuccess.successCallback();
        }
    }
}
