package com.groupite.trashcash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.groupite.trashcash.OrderCreateSuccess;
import com.groupite.trashcash.R;
import com.groupite.trashcash.helpers.MyDialogClickListener;
import com.groupite.trashcash.models.BuyerModel;
import com.groupite.trashcash.models.Metal;
import com.groupite.trashcash.models.Paper;
import com.groupite.trashcash.models.Plastic;
import com.groupite.trashcash.helpers.dialogs.OrderMetalDialog;
import com.groupite.trashcash.helpers.dialogs.OrderPaperDialog;
import com.groupite.trashcash.helpers.dialogs.OrderPlasticDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ConfirmOrderActivity extends AppCompatActivity  {

    Button buttonRequestPaper, buttonRequestPlastic, buttonRequestMetal;
    TextView textViewPaperPriceKg, textViewPlasticPriceKg, textViewMetalPriceKg;
    EditText editTextPaper, editTextPlastic, editTextMetal;

    Context context;

    String userId;
    BuyerModel buyerModel;
    String priceForKgPaper, priceForKgPlastic, priceForKgMetal;
    String paperWeight, plasticWeight, metalWeight;

    DatabaseReference root;
    DatabaseReference paperDatabaseReference, plasticDatabaseReference, metalDatabaseReference;

    CoordinatorLayout coordinatorLayoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        userId = getIntent().getStringExtra("userId");
        buyerModel =  (BuyerModel) getIntent().getExtras().getSerializable("buyerModel");

        context = this;
        customToolBar();
        init();
        root = FirebaseDatabase.getInstance().getReference();
        paperDatabaseReference = root.child("paper");
        plasticDatabaseReference = root.child("plastic");
        metalDatabaseReference = root.child("metal");


        buttonRequestPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paperWeight = editTextPaper.getText().toString().trim();


                if(priceForKgPaper!= null && !TextUtils.isEmpty(paperWeight)) {
                    OrderPaperDialog orderPaperDialog = new OrderPaperDialog();
                    orderPaperDialog.showOrderPaperDialog(context, paperWeight, priceForKgPaper,userId,buyerModel,new OrderCreateSuccess() {

                        @Override
                        public void successCallback() {
                            successMessage();
                        }
                    });
                }else {
                    errorMessage();
                }
            }
        });

        buttonRequestPlastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plasticWeight = editTextPlastic.getText().toString().trim();

                if(priceForKgPlastic != null && !TextUtils.isEmpty(plasticWeight)) {
                    OrderPlasticDialog orderPlasticDialog = new OrderPlasticDialog();
                    orderPlasticDialog.showOrderPlasticDialog(context, plasticWeight, priceForKgPlastic,userId,buyerModel,new OrderCreateSuccess() {

                        @Override
                        public void successCallback() {
                            successMessage();
                        }
                    });
                } else {
                    errorMessage();
                }
            }
        });

        buttonRequestMetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                metalWeight = editTextMetal.getText().toString().trim();

                if(priceForKgMetal != null && !TextUtils.isEmpty(metalWeight)) {
                    OrderMetalDialog orderMetalDialog = new OrderMetalDialog();
                    orderMetalDialog.showOrderMetalDialog(context, metalWeight,priceForKgMetal,userId,buyerModel,new OrderCreateSuccess() {

                        @Override
                        public void successCallback() {
                            successMessage();
                        }
                    });
                }else {
                    errorMessage();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPaperPrice();
    }

    private void getPaperPrice(){
        Query checkPaper = paperDatabaseReference.orderByChild("userId").equalTo(userId);
        Query checkPlastic = plasticDatabaseReference.orderByChild("userId").equalTo(userId);
        Query checkMetal = metalDatabaseReference.orderByChild("userId").equalTo(userId);

        checkPaper.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    Paper paper = null;
                    for (DataSnapshot paperSnapShot : snapshot.getChildren()) {
                        paper = paperSnapShot.getValue(Paper.class);
                        break;
                    }
                    if (paper != null) {
                        priceForKgPaper = paper.getPriceForKg().toString();

                        if (!priceForKgPaper.isEmpty()) {
                            textViewPaperPriceKg.setText(priceForKgPaper);
                        }else {
                            textViewPaperPriceKg.setText(getString(R.string.na));
                        }



                    }

                } else {
                    //saveNewData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        checkPlastic.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    Plastic plastic = null;
                    for (DataSnapshot plasticSnapShot : snapshot.getChildren()) {
                        plastic = plasticSnapShot.getValue(Plastic.class);
                        break;
                    }
                    if (plastic != null) {
                      priceForKgPlastic = plastic.getPriceForKg().toString();

                        if (!priceForKgPlastic.isEmpty()) {
                            textViewPlasticPriceKg.setText(priceForKgPlastic);
                        }else {
                            textViewPlasticPriceKg.setText(getString(R.string.na));
                        }


                    }

                } else {
                    //saveNewData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        checkMetal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null && snapshot.exists()) {
                    Metal metal = null;
                    for (DataSnapshot metalSnapShot : snapshot.getChildren()) {
                        metal = metalSnapShot.getValue(Metal.class);
                        break;
                    }
                    if (metal != null) {
                     priceForKgMetal = metal.getPriceForKg().toString();

                        if (!priceForKgMetal.isEmpty()) {
                            textViewMetalPriceKg.setText(priceForKgMetal);
                        }else {
                            textViewMetalPriceKg.setText(getString(R.string.na));
                        }


                    }

                } else {
                    //saveNewData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void init(){
        coordinatorLayoutRoot = findViewById(R.id.root);

        buttonRequestPaper = findViewById(R.id.bt_request_paper);
        buttonRequestPlastic = findViewById(R.id.bt_request_plastic);
        buttonRequestMetal = findViewById(R.id.bt_request_metal);

        textViewPaperPriceKg = findViewById(R.id.tv_paper_price_for_kg);
        textViewPlasticPriceKg = findViewById(R.id.tv_plastic_price_for_kg);
        textViewMetalPriceKg = findViewById(R.id.tv_metal_price_for_kg);

        editTextPaper = findViewById(R.id.tiet_paper_weight);
        editTextPlastic = findViewById(R.id.tiet_plastic_weight);
        editTextMetal = findViewById(R.id.tiet_metal_weight);
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

    private void errorMessage() {
        Toast.makeText(this,getString(R.string.some_thing),Toast.LENGTH_SHORT).show();
    }
    private  void  successMessage(){
        Toast.makeText(this,getString(R.string.success_create),Toast.LENGTH_SHORT).show();
    }


}