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
import com.groupite.trashcash.models.Metal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import prihanofficial.com.kokis.logics.Kokis;

public class UpdateMetalDialog {

    EditText editTextPrice;

    Button buttonApply;
    Button buttonCancel;

    DatabaseReference root;
    DatabaseReference metalDatabaseReference;



    String price;

    String userId;
    String userType;

    public void showMetalDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.metal_dialog_layout);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        root = FirebaseDatabase.getInstance().getReference();
        metalDatabaseReference = root.child("metal");

        editTextPrice = dialog.findViewById(R.id.tiet_metal_price);


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

        userId = Kokis.getKokisString("user_id", null);
        userType = Kokis.getKokisString("user_type", null);

        if (!TextUtils.isEmpty(price) && !TextUtils.isEmpty(userId)) {
            Query checkMetal = metalDatabaseReference.orderByChild("userId").equalTo(userId);

            checkMetal.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot != null && snapshot.exists()) {
                        Metal metal = null;
                        for (DataSnapshot metalSnapShot : snapshot.getChildren()) {
                            metal = metalSnapShot.getValue(Metal.class);
                            break;
                        }
                        if (metal != null)
                            saveExistData(metal);
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
        String id = metalDatabaseReference.push().getKey();
        if (id != null) {
            Metal metal = new Metal(id, userId, userType, price);
            metalDatabaseReference.child(id).setValue(metal);
        }

    }

    private void saveExistData(Metal metal) {
        metal.setPriceForKg(price);

        metalDatabaseReference.child(metal.getId()).setValue(metal);
    }
}
