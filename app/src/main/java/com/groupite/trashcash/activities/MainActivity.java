package com.groupite.trashcash.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.groupite.trashcash.R;
import com.groupite.trashcash.activities.LoginActivity;
import com.groupite.trashcash.helpers.UserType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import prihanofficial.com.kokis.logics.Kokis;

public class MainActivity extends AppCompatActivity {

    TextView textViewName;
    TextView textViewEmail;
    TextView textViewUserType;

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_price, R.id.nav_orders, R.id.nav_profile,R.id.nav_agents,R.id.nav_calendar,R.id.nav_news,R.id.nav_faq)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);




//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            return;
//                        }
//
//                        // Get new Instance ID token and send to server
//                        String token = task.getResult().getToken();
////                        sendTokenToServer(token);
//                        int x=10;
//                    }
//                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String name = Kokis.getKokisString("first_name"," ").concat(" ").concat(Kokis.getKokisString("last_name"," "));
        String email = Kokis.getKokisString("email"," ");
        if(!TextUtils.isEmpty(name))
            textViewName.setText(name);
        if(!TextUtils.isEmpty(email))
            textViewEmail.setText(email);
    }

    private void init() {
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        textViewName = headerView.findViewById(R.id.tv_name);
        textViewEmail = headerView.findViewById(R.id.tv_email);
        textViewUserType = headerView.findViewById(R.id.tv_user_type);

        String userType =  Kokis.getKokisString("user_type"," ");
        textViewUserType.setText(userType.toString().trim());


        if(userType.equalsIgnoreCase(UserType.CLIENT.toString())){
            navigationView.getMenu().findItem(R.id.nav_agents).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_price).setVisible(false);
        }else {
            navigationView.getMenu().findItem(R.id.nav_agents).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_price).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
        new MaterialAlertDialogBuilder(this)
                .setTitle("Logging out")
                .setMessage("Are you sure?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       actionLogout();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }

    private void actionLogout(){
        Kokis.setKokisBoolean("isLogged",false);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finishAffinity();
        startActivity(intent);
    }

}