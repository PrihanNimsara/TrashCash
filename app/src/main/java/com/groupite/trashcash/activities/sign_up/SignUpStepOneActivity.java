package com.groupite.trashcash.activities.sign_up;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.groupite.trashcash.R;
import com.groupite.trashcash.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class SignUpStepOneActivity extends AppCompatActivity {

    TextInputLayout textInputLayoutFirstName;
    TextInputLayout textInputLayoutLastName;
    TextInputLayout textInputLayoutUserName;
    TextInputLayout textInputLayoutPassword;

    TextInputEditText textInputEditTextFirstName;
    TextInputEditText textInputEditTextLastName;
    TextInputEditText textInputEditTextUserName;
    TextInputEditText textInputEditTextPassword;

    Button buttonNext;

    String firstName = null;
    String lastName = null;
    String email = null;//
    String phone = null;//
    String address = null;//
    String userType = null;//
    String userName = null;
    String password = null;

    DatabaseReference root;
    DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step_one);

        root = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference = root.child("user");

        customToolBar();
        init();

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
        textInputLayoutFirstName = findViewById(R.id.til_first_name);
        textInputLayoutLastName = findViewById(R.id.til_last_name);
        textInputLayoutUserName = findViewById(R.id.til_user_name);
        textInputLayoutPassword = findViewById(R.id.til_password);

        textInputEditTextFirstName = findViewById(R.id.tiet_first_name);
        textInputEditTextLastName = findViewById(R.id.tiet_last_name);
        textInputEditTextUserName = findViewById(R.id.tiet_user_name);
        textInputEditTextPassword = findViewById(R.id.tiet_password);

        buttonNext = findViewById(R.id.bt_next);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isErrorAvailable()){
                    showError();
                }else {
                    isValidUserName(userName);

                }
            }
        });

        textInputEditTextFirstName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if(!s.toString().trim().isEmpty()){
                    if (s.toString().trim().length()<3){
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
                    if (s.toString().trim().length()<3){
                        textInputLayoutLastName.setError("Pleas enter valid Last Name");
                    }else {
                        textInputLayoutLastName.setError(null);
                    }
                }else {
                    textInputLayoutLastName.setError(null);
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
                        textInputLayoutUserName.setError(null);
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
                        textInputLayoutPassword.setError("invalid password");
                    }
                }else {
                    textInputLayoutPassword.setError(null);
                }
            }
        });
    }

    private Boolean isErrorAvailable() {
        firstName = textInputEditTextFirstName.getText().toString().trim();
        lastName = textInputEditTextLastName.getText().toString().trim();
        userName = textInputEditTextUserName.getText().toString().trim();
        password = textInputEditTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            textInputLayoutFirstName.setError(getString(R.string.can_not_be));
        }
        if (TextUtils.isEmpty(lastName)) {
            textInputLayoutLastName.setError(getString(R.string.can_not_be));
        }
        if (TextUtils.isEmpty(userName)) {
            textInputLayoutUserName.setError(getString(R.string.can_not_be));
        }
        if (TextUtils.isEmpty(password)) {
            textInputLayoutPassword.setError(getString(R.string.can_not_be));
        }


        boolean status = false;
        if ( !TextUtils.isEmpty(textInputLayoutFirstName.getError())){
            status = true;
        }else  if(!TextUtils.isEmpty(textInputLayoutLastName.getError())){
            status = true;
        }else  if(!TextUtils.isEmpty(textInputLayoutUserName.getError())){
            status = true;
        }else  if(!TextUtils.isEmpty(textInputLayoutPassword.getError())){
            status = true;
        }
        return  status;
    }

    private void goingToSignUpStepTwoActivity() {
        User user = new User(null, firstName, lastName, email, phone,address, userType, userName, password);
        Intent intent = new Intent(this, SignUpStepTwoActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    private void showError() {
        Toast.makeText(this, getString(R.string.some_thing), Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidPassword(String password ){
        Boolean status = false;
        if(password.length()>= 6 && password.length()<30){
            status = true;
        }
        return status;
    }

    private void isValidUserName(String username){
        Query checkUser = userDatabaseReference.orderByChild("userName").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    //user available
                    textInputLayoutUserName.setError("user name exist");
                } else {
                    //user not available
                    textInputLayoutUserName.setError(null);
                    goingToSignUpStepTwoActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                textInputLayoutUserName.setError("user name exist");
            }
        });
    }
}