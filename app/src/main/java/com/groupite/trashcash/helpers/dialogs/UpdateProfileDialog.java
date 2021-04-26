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
import com.groupite.trashcash.models.User;
import com.groupite.trashcash.helpers.MyDialogClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import prihanofficial.com.kokis.logics.Kokis;

public class UpdateProfileDialog {
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextEmail;
    EditText editTextPhone;
    EditText editTextAddress;
    EditText editTextPassword;
    Button buttonApply;
    Button buttonCancel;

    DatabaseReference root;
    DatabaseReference userDatabaseReference;


    String firstName;
    String lastName;
    String email;
    String phone;
    String address;
    String password;


    String userId;

    public void showProfileDialog(Context context, User user, final MyDialogClickListener myDialogClickListener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.profile_dialog_layout);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        root = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference = root.child("user");

        editTextFirstName = dialog.findViewById(R.id.tiet_profile_first_name);
        editTextLastName = dialog.findViewById(R.id.tiet_profile_last_name);
        editTextEmail = dialog.findViewById(R.id.tiet_profile_email);
        editTextPhone = dialog.findViewById(R.id.tiet_profile_phone);
        editTextAddress = dialog.findViewById(R.id.tiet_address);
        editTextPassword = dialog.findViewById(R.id.tiet_profile_password);

        buttonApply = dialog.findViewById(R.id.bt_apply);
        buttonCancel = dialog.findViewById(R.id.bt_cancel);


        setData(user);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

                dialog.dismiss();

                myDialogClickListener.onPositiveClicked(true);
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogClickListener.onPositiveClicked(false);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setData(User user){
        editTextFirstName.setText(user.getFirstName().toString().trim());
        editTextLastName.setText(user.getLastName().toString().trim());
        editTextEmail.setText(user.getEmail().toString().trim());
        editTextPhone.setText(user.getPhone().toString().trim());
        editTextAddress.setText(user.getAddress().toString().trim());
        editTextPassword.setText(user.getPassword().toString().trim());
    }
    private void saveData() {
        firstName = editTextFirstName.getText().toString().trim();
        lastName = editTextLastName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        phone = editTextPhone.getText().toString().trim();
        address = editTextAddress.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        userId = Kokis.getKokisString("user_id", null);


        if ((!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)) && (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone))) {
            Query checkUser = userDatabaseReference.orderByChild("id").equalTo(userId);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot != null && snapshot.exists()) {
                        User user = null;
                        for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                            user = userSnapShot.getValue(User.class);
                            break;
                        }
                        if (user != null)
                            saveExistData(user);
                    } else {
                       // saveNewData();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }
    }


    private void saveExistData(User user) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setPassword(password);
        userDatabaseReference.child(user.getId()).setValue(user);
    }
}
