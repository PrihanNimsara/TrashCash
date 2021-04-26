package com.groupite.trashcash.helpers.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.groupite.trashcash.R;
import com.groupite.trashcash.models.Paper;
import com.groupite.trashcash.models.Plastic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import prihanofficial.com.kokis.logics.Kokis;

public class UpdatePlasticDialog {

    EditText editTextPrice;
    EditText editTextCom;
    Button buttonApply;
    Button buttonCancel;

    DatabaseReference root;
    DatabaseReference plasticDatabaseReference;



    String price;
    String com;
    String userId;
    String userType;

    public void showPlasticDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.plastic_dialog_layout);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        root = FirebaseDatabase.getInstance().getReference();
        plasticDatabaseReference = root.child("plastic");


        editTextPrice = dialog.findViewById(R.id.tiet_plastic_price);
        buttonApply = dialog.findViewById(R.id.bt_apply);
        buttonCancel = dialog.findViewById(R.id.bt_cancel);

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                dialog.dismiss();
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void saveData() {

        price = editTextPrice.getText().toString();
        com = editTextCom.getText().toString();
        userId = Kokis.getKokisString("user_id", null);
        userType = Kokis.getKokisString("user_type", null);

        if (!TextUtils.isEmpty(price) && (!TextUtils.isEmpty(com) && !TextUtils.isEmpty(userId))) {
            Query checkPaper = plasticDatabaseReference.orderByChild("userId").equalTo(userId);

            checkPaper.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot != null && snapshot.exists()) {
                        Plastic plastic = null;
                        for (DataSnapshot plasticSnapShot : snapshot.getChildren()) {
                            plastic = plasticSnapShot.getValue(Plastic.class);
                            break;
                        }
                        if (plastic != null)
                            saveExistData(plastic);
                    } else {
                        saveNewData();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }
    }


    private void saveNewData() {
        String id = plasticDatabaseReference.push().getKey();
        if (id != null) {
            Paper paper = new Paper(id, userId, userType, price);
            plasticDatabaseReference.child(id).setValue(paper);
        }

    }

    private void saveExistData(Plastic plastic) {
        plastic.setPriceForKg(price);

        plasticDatabaseReference.child(plastic.getId()).setValue(plastic);
    }
}
