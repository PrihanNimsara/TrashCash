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

import com.groupite.trashcash.ConfirmOrderActivity;
import com.groupite.trashcash.R;
import com.groupite.trashcash.models.BuyerModel;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<BuyerModel> implements View.OnClickListener{

private ArrayList<BuyerModel> dataSet;
        Context mContext;

// View lookup cache
private static class ViewHolder {
    TextView textViewFirstName, textViewEmail, textViewPhone, textViewAddress;
    Button buttonApply;
}

    public CustomAdapter(ArrayList<BuyerModel> data, Context context) {
        super(context, R.layout.row_item_buyers, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        BuyerModel dataModel=(BuyerModel)object;

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
        final BuyerModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item_buyers, parent, false);
            viewHolder.textViewFirstName = convertView.findViewById(R.id.tv_first_name);
            viewHolder.textViewEmail = convertView.findViewById(R.id.tv_email);
            viewHolder.textViewPhone =  convertView.findViewById(R.id.tv_phone);
            viewHolder.textViewAddress =  convertView.findViewById(R.id.tv_address);
            viewHolder.buttonApply = convertView.findViewById(R.id.bt_apply);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.textViewFirstName.setText(dataModel.getUser().getFirstName());
        viewHolder.textViewEmail.setText(dataModel.getUser().getEmail());
        viewHolder.textViewPhone.setText(dataModel.getUser().getPhone());
        viewHolder.textViewAddress.setText(dataModel.getUser().getAddress());

        viewHolder.buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               singleApplyClick(dataModel);
            }
        });

        return convertView;
    }


    private void singleApplyClick( BuyerModel dataModel){
        Intent intent = new Intent(mContext, ConfirmOrderActivity.class);
        intent.putExtra("userId",dataModel.getUser().getId());
        intent.putExtra("buyerModel",(Serializable)dataModel);
        mContext.startActivity(intent);
    }
}
