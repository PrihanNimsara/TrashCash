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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import prihanofficial.com.kokis.logics.Kokis;

public class UpdatePaperDialog {
    EditText editTextPrice;
    Button buttonApply;
    Button buttonCancel;

    DatabaseReference root;
    DatabaseReference paperDatabaseReference;

    String price;
    String userId;
    String userType;

    public void showPaperDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.paper_dialog_layout);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        root = FirebaseDatabase.getInstance().getReference();
        paperDatabaseReference = root.child("paper");


        editTextPrice = dialog.findViewById(R.id.tiet_paper_price);
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
            Query checkPaper = paperDatabaseReference.orderByChild("userId").equalTo(userId);

            checkPaper.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot != null && snapshot.exists()) {
                        Paper paper = null;
                        for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                            paper = userSnapShot.getValue(Paper.class);
                            break;
                        }
                        if (paper != null)
                            saveExistData(paper);
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
        String id = paperDatabaseReference.push().getKey();
        if (id != null) {
            Paper paper = new Paper(id, userId, userType, price);
            paperDatabaseReference.child(id).setValue(paper);
        }

    }

    private void saveExistData(Paper paper) {
        paper.setPriceForKg(price);
        paperDatabaseReference.child(paper.getId()).setValue(paper);
    }
}
