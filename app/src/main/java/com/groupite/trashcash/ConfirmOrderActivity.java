package com.groupite.trashcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.groupite.trashcash.models.BuyerModel;
import com.groupite.trashcash.models.Metal;
import com.groupite.trashcash.models.Paper;
import com.groupite.trashcash.models.Plastic;
import com.groupite.trashcash.utills.dialogs.OrderMetalDialog;
import com.groupite.trashcash.utills.dialogs.OrderPaperDialog;
import com.groupite.trashcash.utills.dialogs.OrderPlasticDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ConfirmOrderActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        userId = getIntent().getStringExtra("userId");
        buyerModel =  (BuyerModel) getIntent().getExtras().getSerializable("buyerModel");

        context = this;
        init();
        root = FirebaseDatabase.getInstance().getReference();
        paperDatabaseReference = root.child("paper");
        plasticDatabaseReference = root.child("plastic");
        metalDatabaseReference = root.child("metal");


        buttonRequestPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paperWeight = editTextPaper.getText().toString().trim();

                if(!TextUtils.isEmpty(paperWeight)) {
                    OrderPaperDialog orderPaperDialog = new OrderPaperDialog();
                    orderPaperDialog.showOrderPaperDialog(context, paperWeight, priceForKgPaper,userId,buyerModel);
                }
            }
        });

        buttonRequestPlastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plasticWeight = editTextPlastic.getText().toString().trim();

                if(!TextUtils.isEmpty(plasticWeight)) {
                    OrderPlasticDialog orderPlasticDialog = new OrderPlasticDialog();
                    orderPlasticDialog.showOrderPlasticDialog(context, plasticWeight, priceForKgPlastic,userId,buyerModel);
                }
            }
        });

        buttonRequestMetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                metalWeight = editTextMetal.getText().toString().trim();

                if(!TextUtils.isEmpty(metalWeight)) {
                    OrderMetalDialog orderMetalDialog = new OrderMetalDialog();
                    orderMetalDialog.showOrderMetalDialog(context, metalWeight,priceForKgMetal,userId,buyerModel);
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
                        textViewPaperPriceKg.setText(priceForKgPaper);
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
                      textViewPlasticPriceKg.setText(priceForKgPlastic);
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
                     textViewMetalPriceKg.setText(priceForKgMetal);
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
}