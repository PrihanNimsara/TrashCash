package com.groupite.trashcash.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.groupite.trashcash.R;
import com.groupite.trashcash.models.Order;

public class OrderDetaillsActivity extends AppCompatActivity {


    TextView textViewKg, textViewRs, textViewType, textViewStatus;
    Button buttonMap;
    private  Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detaills);
        Intent i = getIntent();
        order = (Order) i.getSerializableExtra("order");
        customToolBar();
        init();

    }

    private void init() {
        textViewKg = findViewById(R.id.tv_kg);
        textViewRs = findViewById(R.id.tv_rs);
        textViewType = findViewById(R.id.tv_type);
        textViewStatus = findViewById(R.id.tv_status);
        buttonMap = findViewById(R.id.bt_map);

        setData();



    }
    private void singleApplyClick(Order order) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("sellerAddress", order.getSellerAddress());
        intent.putExtra("buyerAddress", order.getBuyerAddress());
        intent.putExtra("sellerPhone", order.getSellerPhone());
        intent.putExtra("buyerPhone", order.getBuyerPhone());
        intent.putExtra("orderID", order.getId());
        this.startActivity(intent);
    }

    private void setData() {
        textViewKg.setText(order.getWeight().toString().trim());
        textViewRs.setText(order.getPrice().toString().trim());
        textViewType.setText(order.getType().toString().trim());
        textViewStatus.setText(order.getStatus().toString().trim());

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleApplyClick(order);
            }
        });
    }

    private void customToolBar() {
        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}