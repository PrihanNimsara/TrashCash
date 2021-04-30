package com.groupite.trashcash.helpers.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.groupite.trashcash.activities.MapsActivity;
import com.groupite.trashcash.R;
import com.groupite.trashcash.activities.OrderDetaillsActivity;
import com.groupite.trashcash.helpers.UserType;
import com.groupite.trashcash.models.Order;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

import prihanofficial.com.kokis.logics.Kokis;

public class OrderAdapter extends ArrayAdapter<Order> implements View.OnClickListener {
    private ArrayList<Order> orderDataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView textViewName, textViewCity, textViewEmail, textViewPhone;
        Button buttonView;

        TextView textViewWeight, textViewPrice, textViewType, textViewStatus;

        MaterialCardView materialCardViewOne, materialCardViewTwo;
    }

    public OrderAdapter(ArrayList<Order> data, Context context) {
        super(context, R.layout.row_item_order, data);
        this.orderDataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Order orderModel = (Order) object;

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

            viewHolder.textViewName = convertView.findViewById(R.id.tv_name);
            viewHolder.textViewCity = convertView.findViewById(R.id.tv_city);
            viewHolder.textViewEmail = convertView.findViewById(R.id.tv_email_addres);
            viewHolder.textViewPhone = convertView.findViewById(R.id.tv_phone_num);

            viewHolder.buttonView = convertView.findViewById(R.id.bt_view);


//            viewHolder.textViewWeight = convertView.findViewById(R.id.tv_total_weight);
//            viewHolder.textViewPrice = convertView.findViewById(R.id.tv_total_price);
//            viewHolder.textViewType =  convertView.findViewById(R.id.tv_ttype);
//            viewHolder.textViewStatus =  convertView.findViewById(R.id.tv_status);
//
//            viewHolder.buttonView = convertView.findViewById(R.id.bt_view);
//
//            viewHolder.materialCardViewOne = convertView.findViewById(R.id.mcv_one);
//            viewHolder.materialCardViewTwo = convertView.findViewById(R.id.mcv_two);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        String userType = Kokis.getKokisString("user_type", " ");
        if (userType.equalsIgnoreCase(UserType.CLIENT.toString())) {
            viewHolder.textViewName.setText(orderDataModel.getBuyerName().toString().trim());
            viewHolder.textViewCity.setText(orderDataModel.getBuyerAddress().toString().trim());
            viewHolder.textViewEmail.setText(orderDataModel.getBuyerEmail().toString().trim());
            viewHolder.textViewPhone.setText(orderDataModel.getBuyerPhone().toString().trim());
        } else if (userType.equalsIgnoreCase(UserType.AGENT.toString())) {
            viewHolder.textViewName.setText(orderDataModel.getSellerName().toString().trim());
            viewHolder.textViewCity.setText(orderDataModel.getSellerAddress().toString().trim());
            viewHolder.textViewEmail.setText(orderDataModel.getSellerEmail().toString().trim());
            viewHolder.textViewPhone.setText(orderDataModel.getSellerPhone().toString().trim());
        }

//
//        viewHolder.buttonView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               singleApplyClick(orderDataModel);
//            }
//        });

        viewHolder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleApplyClick(orderDataModel);
            }
        });


        return convertView;
    }


    private void singleApplyClick(Order order) {
        Intent intent = new Intent(mContext, OrderDetaillsActivity.class);
//        intent.putExtra("sellerAddress", order.getSellerAddress());
//        intent.putExtra("buyerAddress", order.getBuyerAddress());
//        intent.putExtra("sellerPhone", order.getSellerPhone());
//        intent.putExtra("buyerPhone", order.getBuyerPhone());
//        intent.putExtra("orderID", order.getId());
        intent.putExtra("order",order);
        mContext.startActivity(intent);
    }
}
