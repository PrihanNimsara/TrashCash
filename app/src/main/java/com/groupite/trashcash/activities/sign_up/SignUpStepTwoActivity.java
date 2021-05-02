package com.groupite.trashcash.activities.sign_up;


import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.groupite.trashcash.R;
import com.groupite.trashcash.helpers.UserType;
import com.groupite.trashcash.models.Order;
import com.groupite.trashcash.models.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class SignUpStepTwoActivity extends AppCompatActivity {


    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPhone;
    TextInputLayout textInputLayoutAddress;
    RadioGroup radioGroupUserType;


    TextInputEditText textInputEditTextEmail;
    TextInputEditText textInputEditTextPhone;
    TextInputEditText textInputEditTextAddress;


    Button buttonSignUp;

    String firstName = null;
    String lastName = null;
    String email = null;
    String phone = null;
    String address = null;
    String userType = null;
    String userName = null;
    String password = null;

    DatabaseReference root;
    DatabaseReference userDatabaseReference;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step_two);

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");

        root = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference = root.child("user");


        customToolBar();
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void customToolBar(){
        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        textInputLayoutEmail = findViewById(R.id.til_email);
        textInputLayoutPhone = findViewById(R.id.til_phone);
        textInputLayoutAddress = findViewById(R.id.til_address);
        radioGroupUserType = findViewById(R.id.rg_user_type);

        textInputEditTextEmail = findViewById(R.id.tiet_email);
        textInputEditTextPhone = findViewById(R.id.tiet_phone);
        textInputEditTextAddress = findViewById(R.id.tiet_address);

        buttonSignUp = findViewById(R.id.bt_signup);

        userType = UserType.CLIENT.toString();


        radioGroupUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.r_user_type_seller:
                        userType = UserType.CLIENT.toString();
                        break;
                    case R.id.r_user_type_buyer:
                        userType = UserType.AGENT.toString();
                        break;
                }
            }
        });


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        textInputEditTextEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    if (isValidEmailAddress(s.toString().trim())){
                        textInputLayoutEmail.setError(null);
                    }else {
                        textInputLayoutEmail.setError("invalid email address");
                    }
                }else {
                    textInputLayoutEmail.setError(null);
                }
            }
        });

        textInputEditTextPhone.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    if (isValidMobile(s.toString().trim())){
                        textInputLayoutPhone.setError(null);
                    }else {
                        textInputLayoutPhone.setError("invalid phone number");
                    }
                }else {
                    textInputLayoutPhone.setError(null);
                }
            }
        });

        textInputEditTextAddress.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    if (s.toString().trim().length() < 3){
                        textInputLayoutAddress.setError("Pleas enter valid address");
                    }else {
                        textInputLayoutAddress.setError(null);
                    }
                }else {
                    textInputLayoutAddress.setError(null);
                }
            }
        });

    }

    private void showDialog(){
        new MaterialAlertDialogBuilder(SignUpStepTwoActivity.this)
                .setTitle("Sign Up")
                .setMessage("Are you sure you want to continue ? ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isErrorAvailable()){
                            showError1();
                        }else {
                            saveData();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }


    private Boolean isErrorAvailable() {

        email = textInputEditTextEmail.getText().toString().trim();
        phone = textInputEditTextPhone.getText().toString().trim();
        address = textInputEditTextAddress.getText().toString().trim();

       if (TextUtils.isEmpty(email)) {
            textInputLayoutEmail.setError(getString(R.string.can_not_be));
        }
       if(TextUtils.isEmpty(phone)){
            textInputLayoutPhone.setError(getString(R.string.can_not_be));
        }
       if(TextUtils.isEmpty(address)){
            textInputLayoutAddress.setError(getString(R.string.can_not_be));
        }



       Boolean status = false;
        if (!TextUtils.isEmpty(textInputLayoutEmail.getError())){
            status = true;
        }else  if(!TextUtils.isEmpty(textInputLayoutPhone.getError())){
            status = true;
        }else  if(!TextUtils.isEmpty(textInputLayoutAddress.getError())){
            status = true;
        }
        return  status;
    }


    private void showError1() {
        Toast.makeText(this, getString(R.string.some_thing), Toast.LENGTH_SHORT).show();
    }

    private void saveData() {
        Query checkUser = userDatabaseReference.orderByChild("userName").equalTo(userName);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    showError();
                } else {
                    callSaveSignUpRequest();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void callSaveSignUpRequest() {
        String id = userDatabaseReference.push().getKey();
        if (id != null) {


            user.setId(id);
            user.setEmail(email);
            user.setPhone(phone);
            user.setAddress(address);
            user.setUserType(userType);

            userDatabaseReference.child(id).setValue(user);
//            Toast.makeText(this, "successfully sign up  ", Toast.LENGTH_SHORT).show();
//
//            finish();

            Intent intent = new Intent(this, SignUpSuccessActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void showError() {
        Toast.makeText(this, " sign up failed, because username exist", Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidMobile(String phone) {
        Boolean status = false;
        if(phone.length() == 10){
            status = true;
        }
        return status;
    }

    private Boolean isValidEmailAddress(String email){
        Boolean status = false;
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            status = true;
        }
        return  status;
    }


}