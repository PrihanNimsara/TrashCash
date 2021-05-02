package com.groupite.trashcash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.groupite.trashcash.R;
import com.groupite.trashcash.activities.sign_up.SignUpStepOneActivity;
import com.groupite.trashcash.helpers.NetworkChangeReceiver;
import com.groupite.trashcash.models.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import prihanofficial.com.kokis.logics.Kokis;

public class LoginActivity extends AppCompatActivity {

  static   ConstraintLayout rootConstraint;

    EditText editTextUserName;
    EditText editTextPassword;
    TextView textViewNewUserSignUp;

    Button buttonLogin;

    String userName;
    String password;

    DatabaseReference root;
    DatabaseReference userDatabaseReference;


    private BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        root = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference = root.child("user");



        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        textViewNewUserSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              openSignUpActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void init(){

        rootConstraint = findViewById(R.id.cl_root);

        editTextUserName = findViewById(R.id.tiet_user_name);
        editTextPassword = findViewById(R.id.tiet_password);
        buttonLogin = findViewById(R.id.bt_login);
        textViewNewUserSignUp = findViewById(R.id.tv_new_user_signup);

        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();
    }

    private void login(){
        userName = editTextUserName.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)){
            Query checkUser = userDatabaseReference.orderByChild("userName").equalTo(userName);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot != null && snapshot.exists()) {
                        User user = null;
                        for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                        user   = userSnapShot.getValue(User.class);
                            break;
                        }
                        if (user != null)
                       loginValidUsername(user);
                    } else {
                      showError();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }else {
            Toast.makeText(this,"Please enter username and password",Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(){
        Toast.makeText(this,"Please enter valid user name",Toast.LENGTH_SHORT).show();
    }

    private void loginValidUsername(User user){
      if (password.equals(user.getPassword())){
          Toast.makeText(this,"Login successfully",Toast.LENGTH_SHORT).show();
          Kokis.setKokisBoolean("isLogged",true);
          Kokis.setKokisString("first_name",user.getFirstName());
          Kokis.setKokisString("last_name",user.getLastName());
          Kokis.setKokisString("email",user.getEmail());
          Kokis.setKokisString("phone",user.getPhone());
          Kokis.setKokisString("address",user.getAddress());

          Kokis.setKokisString("user_id",user.getId());
          Kokis.setKokisString("user_type",user.getUserType());

          Intent intent = new Intent(this, MainActivity.class);
          startActivity(intent);
          finish();

      }else {
          Toast.makeText(this,"Please enter valid password",Toast.LENGTH_SHORT).show();
      }
    }

    private void openSignUpActivity(){
        Intent intent = new Intent(this, SignUpStepOneActivity.class);
        startActivity(intent);
    }





    public static void dialog(boolean value){

        if(value){
            Snackbar.make(rootConstraint,"We are back !!!", BaseTransientBottomBar.LENGTH_SHORT).show();
        }else {
            Snackbar.make(rootConstraint,"Could not Connect to internet", BaseTransientBottomBar.LENGTH_INDEFINITE).show();
        }
    }


    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
}