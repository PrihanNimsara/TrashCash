package com.groupite.trashcash.ui.slideshow;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.groupite.trashcash.R;
import com.groupite.trashcash.models.User;
import com.groupite.trashcash.helpers.MyDialogClickListener;
import com.groupite.trashcash.helpers.dialogs.UpdateProfileDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import prihanofficial.com.kokis.logics.Kokis;

public class SlideshowFragment extends Fragment {

    TextView textViewFirstName, textViewLastName, textViewEmail, textViewPhone, textViewAddress, textViewUserName, textViewPassword;
    Button buttonUpdate;

    Context context;

    DatabaseReference root;
    DatabaseReference userDatabaseReference;

    User updateUser = null;

    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);
        this.context = context;

        root = FirebaseDatabase.getInstance().getReference();
        userDatabaseReference = root.child("user");


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        init(root);

           buttonUpdate.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                 if( Kokis.kgetKokisBoolean("internet",false)) {
                     UpdateProfileDialog updateProfileDialog = new UpdateProfileDialog();
                     updateProfileDialog.showProfileDialog(context, updateUser, new MyDialogClickListener() {
                         @Override
                         public void onPositiveClicked(Boolean value) {
                             if (value) {
                                 loadData();
                             }
                         }
                     });
                 }else {
                     Toast.makeText(context,"Check your network connection",Toast.LENGTH_SHORT).show();
                 }
               }
           });

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();

        int x = 10;
    }

    private void init(View root) {
        textViewFirstName = root.findViewById(R.id.tv_first_name_title_data);

        textViewEmail = root.findViewById(R.id.tv_email_title_data);
        textViewPhone = root.findViewById(R.id.tv_phone_number_title_data);
        textViewAddress = root.findViewById(R.id.tv_address_title_data);
        textViewUserName = root.findViewById(R.id.tv_user_name_title_data);
        textViewPassword = root.findViewById(R.id.tv_password_title_data);

        buttonUpdate = root.findViewById(R.id.bt_update);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

   private void loadData(){
    String userId = Kokis.getKokisString("user_id", null);
    Query checkUser = userDatabaseReference.orderByChild("id").equalTo(userId);

    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            if (snapshot != null && snapshot.exists()) {
                User user = null;
                for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                    user = userSnapShot.getValue(User.class);
                    break;
                }
                if (user != null)
                    setValue(user);
            } else {
                // showError();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    });
}
    private void setValue(User user) {
        updateUser = user;

        textViewFirstName.setText(user.getFirstName().toString().trim().concat(" ").concat(user.getLastName().toString().trim()));
        textViewEmail.setText(user.getEmail().toString().trim());
        textViewPhone.setText(user.getPhone().toString().trim());
        textViewAddress.setText(user.getAddress().toString().trim());
        textViewUserName.setText(user.getUserName().toString().trim());
        textViewPassword.setText(user.getPassword().toString().trim());
    }
}