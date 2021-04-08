package com.groupite.trashcash.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.groupite.trashcash.R;
import com.groupite.trashcash.helpers.UserType;
import com.groupite.trashcash.models.Metal;
import com.groupite.trashcash.models.Paper;
import com.groupite.trashcash.models.Plastic;
import com.groupite.trashcash.utills.dialogs.UpdateMetalDialog;
import com.groupite.trashcash.utills.dialogs.UpdatePaperDialog;
import com.groupite.trashcash.utills.dialogs.UpdatePlasticDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import prihanofficial.com.kokis.logics.Kokis;

public class HomeFragment extends Fragment {

    Button buttonPaperUpdate;
    Button buttonPlasticUpdate;
    Button buttonMetalUpdate;

    Context context;

    DatabaseReference root;
    DatabaseReference paperDatabaseReference, plasticDatabaseReference, metalDatabaseReference;


    TextView textViewPaperPrice, textViewPlasticPrice, textViewMetalPrice;
    TextView textViewPaperCom, textViewPlasticCom, textViewMetalCom;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        root = FirebaseDatabase.getInstance().getReference();
        paperDatabaseReference = root.child("paper");
        plasticDatabaseReference = root.child("plastic");
        metalDatabaseReference = root.child("metal");

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        init(root);

        buttonPaperUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePaperDialog updatePaperDialog = new UpdatePaperDialog();
                updatePaperDialog.showPaperDialog(context);
            }
        });

        buttonPlasticUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePlasticDialog updatePlasticDialog = new UpdatePlasticDialog();
                updatePlasticDialog.showPlasticDialog(context);
            }
        });

        buttonMetalUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateMetalDialog updateMetalDialog = new UpdateMetalDialog();
                updateMetalDialog.showMetalDialog(context);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        String userType = Kokis.getKokisString("user_type", " ");
        if( userType.equalsIgnoreCase(UserType.BUYER.toString())) {
            String userId = Kokis.getKokisString("user_id", null);

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
                            textViewPaperPrice.setText(paper.getPriceForKg());
                            textViewPaperCom.setText(paper.getCompensationPrice());
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
                            textViewPlasticPrice.setText(plastic.getPriceForKg());
                            textViewPlasticCom.setText(plastic.getCompensationPrice());
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
                            textViewMetalPrice.setText(metal.getPriceForKg());
                            textViewMetalCom.setText(metal.getCompensationPrice());
                        }

                    } else {
                        //saveNewData();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }else {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Alert")
                    .setMessage("you do not have permission to access this functionality...")
                    .setPositiveButton("Ok", /* listener = */ null)
                    .show();
        }

    }


    private void init(View root) {

        textViewPaperPrice = root.findViewById(R.id.tv_paper_price_result);
        textViewPaperCom = root.findViewById(R.id.tv_paper_com_result);

        textViewPlasticPrice = root.findViewById(R.id.tv_plastic_price_result);
        textViewPlasticCom = root.findViewById(R.id.tv_plastic_com_result);

        textViewMetalPrice = root.findViewById(R.id.tv_metal_price_result);
        textViewMetalCom = root.findViewById(R.id.tv_metal_com_result);

        buttonPaperUpdate = root.findViewById(R.id.bt_paper_update);
        buttonPlasticUpdate = root.findViewById(R.id.bt_plastic_update);
        buttonMetalUpdate = root.findViewById(R.id.bt_metal_update);
    }
}