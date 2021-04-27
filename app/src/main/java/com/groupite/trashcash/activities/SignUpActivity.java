package com.groupite.trashcash.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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


public class SignUpActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutFirstName;
    TextInputLayout textInputLayoutLastName;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPhone;
    TextInputLayout textInputLayoutAddress;
    RadioGroup radioGroupUserType;
    TextInputLayout textInputLayoutUserName;
    TextInputLayout textInputLayoutPassword;

    TextInputEditText textInputEditTextFirstName;
    TextInputEditText textInputEditTextLastName;
    TextInputEditText textInputEditTextEmail;
    TextInputEditText textInputEditTextPhone;
    TextInputEditText textInputEditTextAddress;
    TextInputEditText textInputEditTextUserName;
    TextInputEditText textInputEditTextPassword;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        customToolBar();
        init();

        root = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference = root.child("user");


        radioGroupUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.r_user_type_seller:
                        userType = UserType.SELLER.toString();
                        break;
                    case R.id.r_user_type_buyer:
                        userType = UserType.BUYER.toString();
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

        textInputEditTextFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputLayoutFirstName.setError(null);
            }
        });

        textInputEditTextLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputLayoutLastName.setError(null);
            }
        });

        textInputEditTextEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputLayoutEmail.setError(null);
            }
        });

        textInputEditTextPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputLayoutPhone.setError(null);
            }
        });

        textInputEditTextAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputLayoutAddress.setError(null);
            }
        });

        textInputEditTextUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputLayoutUserName.setError(null);
            }
        });

        textInputEditTextPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputLayoutPassword.setError(null);
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void init() {
        textInputLayoutFirstName = findViewById(R.id.til_first_name);
        textInputLayoutLastName = findViewById(R.id.til_last_name);
        textInputLayoutEmail = findViewById(R.id.til_email);
        textInputLayoutPhone = findViewById(R.id.til_phone);
        textInputLayoutAddress = findViewById(R.id.til_address);
        radioGroupUserType = findViewById(R.id.rg_user_type);
        textInputLayoutUserName = findViewById(R.id.til_user_name);
        textInputLayoutPassword = findViewById(R.id.til_password);

        textInputEditTextFirstName = findViewById(R.id.tiet_first_name);
        textInputEditTextLastName = findViewById(R.id.tiet_last_name);
        textInputEditTextEmail = findViewById(R.id.tiet_email);
        textInputEditTextPhone = findViewById(R.id.tiet_phone);
        textInputEditTextAddress = findViewById(R.id.tiet_address);
        textInputEditTextUserName = findViewById(R.id.tiet_user_name);
        textInputEditTextPassword = findViewById(R.id.tiet_password);

        buttonSignUp = findViewById(R.id.bt_signup);

        userType = UserType.SELLER.toString();



        textInputEditTextFirstName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    if (count<3){
                        textInputLayoutFirstName.setError("Pleas enter valid First Name");
                    }else {
                        textInputLayoutFirstName.setError(null);
                    }
                }else {
                    textInputLayoutFirstName.setError(null);
                }


            }
        });


        textInputEditTextLastName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    if (count<3){
                        textInputLayoutLastName.setError("Pleas enter valid Last Name");
                    }else {
                        textInputLayoutLastName.setError(null);
                    }
                }else {
                    textInputLayoutLastName.setError(null);
                }
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


        textInputEditTextUserName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    if (s.toString().trim().length()<3){
                        textInputLayoutUserName.setError("Pleas enter valid username");
                    }else {
                        isValidUserName(s.toString().trim());
                    }
                }else {
                    textInputLayoutUserName.setError(null);
                }


            }
        });

        textInputEditTextPassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    if (isValidPassword(s.toString().trim())){
                        textInputLayoutPassword.setError(null);
                    }else {
                        textInputLayoutPassword.setError("invalid email password");
                    }
                }else {
                    textInputLayoutPassword.setError(null);
                }
            }
        });



    }

    private void saveSignUpDetails() {
        firstName = textInputEditTextFirstName.getText().toString().trim();
        lastName = textInputEditTextLastName.getText().toString().trim();
        email = textInputEditTextEmail.getText().toString().trim();
        phone = textInputEditTextPhone.getText().toString().trim();
        address = textInputEditTextAddress.getText().toString().trim();

        userName = textInputEditTextUserName.getText().toString().trim();
        password = textInputEditTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            textInputLayoutFirstName.setError("You need to enter a first name");
        } else if (TextUtils.isEmpty(lastName)) {
            textInputLayoutLastName.setError("You need to enter a last name");
        } else if (TextUtils.isEmpty(email)) {
            textInputLayoutEmail.setError("You need to enter an email name");
        }else if(TextUtils.isEmpty(phone)){
            textInputLayoutPhone.setError("You need to enter a phone number");
        } else if(TextUtils.isEmpty(address)){
            textInputLayoutAddress.setError("You need to enter an address");
        } else if (TextUtils.isEmpty(userName)) {
            textInputLayoutUserName.setError("You need to enter a username name");
        } else if (TextUtils.isEmpty(password)) {
            textInputLayoutPassword.setError("You need to enter a password name");
        } else {
            saveData();
        }
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
            User user = new User(id, firstName, lastName, email, phone,address, userType, userName, password);
            userDatabaseReference.child(id).setValue(user);
            Toast.makeText(this, "successfully sign up  ", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    private void showError() {
        Toast.makeText(this, " sign up failed, because username exist", Toast.LENGTH_SHORT).show();
    }

    private void showDialog(){
        new MaterialAlertDialogBuilder(SignUpActivity.this)
                .setTitle("Sign Up")
                .setMessage("Are you sure you want to continue ? ")
                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveSignUpDetails();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void customToolBar(){
        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private Boolean isValidMobile(String phone) {
        Boolean status = false;
        if(phone.length() == 10){
            status = true;
        }
        return status;
    }

    private Boolean isValidPassword(String password ){
        Boolean status = false;
        if(password.length()>= 6 && password.length()<30){
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

    private void isValidUserName(String username){
        Query checkUser = userDatabaseReference.orderByChild("userName").equalTo(username);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    //user available
                    textInputLayoutUserName.setError("1111");

                    Log.d("fuck","fuck1");
                } else {
                    //user not available
                    textInputLayoutUserName.setError(null);
                    Log.d("fuck","fuck2");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                textInputLayoutUserName.setError("1111");
                Log.d("fuck","fuck3");
            }
        });


    }
}