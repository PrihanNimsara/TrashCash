package com.groupite.trashcash.helpers.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.groupite.trashcash.MapsActivity;
import com.groupite.trashcash.R;
import com.groupite.trashcash.helpers.UserType;
import com.groupite.trashcash.models.Order;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

import prihanofficial.com.kokis.logics.Kokis;

public class OrderAdapter extends ArrayAdapter<Order> implements View.OnClickListener{
    private ArrayList<Order> orderDataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView textViewBuyerName, textViewBuyerEmail, textViewBuyerPhone, textViewBuyerAddress;
        TextView textViewSellerName, textViewSellerEmail, textViewSellerPhone, textViewSellerAddress;
        TextView textViewWeight, textViewPrice, textViewType, textViewStatus;
        ExtendedFloatingActionButton buttonView;
        MaterialCardView materialCardViewOne, materialCardViewTwo;
    }

    public OrderAdapter(ArrayList<Order> data, Context context) {
        super(context, R.layout.row_item_order, data);
        this.orderDataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Order orderModel =(Order) object;

//        switch (v.getId())
//        {
//            case R.id.bt_apply:
//                Snackbar.make(v, "Release date " , Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
//        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Order orderDataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final OrderAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new OrderAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_order, parent, false);

            viewHolder.textViewBuyerName = convertView.findViewById(R.id.tv_buyer_name);
            viewHolder.textViewBuyerEmail = convertView.findViewById(R.id.tv_buyer_email);
            viewHolder.textViewBuyerPhone =  convertView.findViewById(R.id.tv_buyer_phone);
            viewHolder.textViewBuyerAddress =  convertView.findViewById(R.id.tv_buyer_address);

            viewHolder.textViewSellerName = convertView.findViewById(R.id.tv_seller_name);
            viewHolder.textViewSellerEmail = convertView.findViewById(R.id.tv_seller_email);
            viewHolder.textViewSellerPhone =  convertView.findViewById(R.id.tv_seller_phone);
            viewHolder.textViewSellerAddress =  convertView.findViewById(R.id.tv_seller_address);

            viewHolder.textViewWeight = convertView.findViewById(R.id.tv_total_weight);
            viewHolder.textViewPrice = convertView.findViewById(R.id.tv_total_price);
            viewHolder.textViewType =  convertView.findViewById(R.id.tv_ttype);
            viewHolder.textViewStatus =  convertView.findViewById(R.id.tv_status);

            viewHolder.buttonView = convertView.findViewById(R.id.bt_view);

            viewHolder.materialCardViewOne = convertView.findViewById(R.id.mcv_one);
            viewHolder.materialCardViewTwo = convertView.findViewById(R.id.mcv_two);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.textViewBuyerName.setText(orderDataModel.getBuyerName().toString().trim());
        viewHolder.textViewBuyerEmail.setText(orderDataModel.getBuyerEmail().toString().trim());
        viewHolder.textViewBuyerPhone.setText(orderDataModel.getBuyerPhone().toString().trim());
        viewHolder.textViewBuyerAddress.setText(orderDataModel.getBuyerAddress().toString().trim());

        viewHolder.textViewSellerName.setText(orderDataModel.getSellerName().toString().trim());
        viewHolder.textViewSellerEmail.setText(orderDataModel.getSellerEmail().toString().trim());
        viewHolder.textViewSellerPhone.setText(orderDataModel.getSellerPhone().toString().trim());
        viewHolder.textViewSellerAddress.setText(orderDataModel.getSellerAddress().toString().trim());

        viewHolder.textViewWeight.setText(orderDataModel.getWeight().toString().trim());
        viewHolder.textViewPrice.setText(orderDataModel.getPrice().toString().trim());
        viewHolder.textViewType.setText(orderDataModel.getType().toString().trim());
        viewHolder.textViewStatus.setText(orderDataModel.getStatus().toString().trim());

        viewHolder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               singleApplyClick(orderDataModel);
            }
        });

        String userType =  Kokis.getKokisString("user_type"," ");
        if(userType.equalsIgnoreCase(UserType.SELLER.toString())){
            viewHolder.materialCardViewTwo.setVisibility(View.GONE);
            viewHolder.materialCardViewOne.setVisibility(View.VISIBLE);
        }else {
            viewHolder.materialCardViewTwo.setVisibility(View.VISIBLE);
            viewHolder.materialCardViewOne.setVisibility(View.GONE);
        }

        return convertView;
    }


    private void singleApplyClick( Order order){
        Intent intent = new Intent(mContext, MapsActivity.class);
        intent.putExtra("sellerAddress",order.getSellerAddress());
        intent.putExtra("buyerAddress",order.getBuyerAddress());
        intent.putExtra("sellerPhone",order.getSellerPhone());
        intent.putExtra("buyerPhone",order.getBuyerPhone());
        intent.putExtra("orderID",order.getId());
        mContext.startActivity(intent);
    }
}
