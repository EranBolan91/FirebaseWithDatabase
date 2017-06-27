package com.world.bolandian.firebasewithdatabase.dialogs;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.world.bolandian.firebasewithdatabase.R;
import com.world.bolandian.firebasewithdatabase.models.ShoppingLists;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddListFragment extends DialogFragment {


    @BindView(R.id.etListName)
    EditText etListName;
    Unbinder unbinder;
    @BindView(R.id.btnAdd)
    BootstrapButton btnAdd;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public AddListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnAdd)
    public void onBtnAddClicked() {

        if (!(etListName.getText().toString().isEmpty())) {
            //start the prograssBar
            progressBar.setVisibility(View.VISIBLE);
            //1) get the user
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //2) get a refernce to the UserListTable
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserLists").child(user.getUid());

            //3)push returns the id of the new record
            //create a new Row
            DatabaseReference row = ref.push();
            // got the id of the new row
            String listUID = row.getKey();
            //get the list name from the editText
            String listName = etListName.getText().toString();
            //constructor the model
            ShoppingLists model = new ShoppingLists(user.getUid(), listUID, listName);
            //set te vake of the new row of the model
             //TODO: progressBar while adding to firebase with - addOn...
            row.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        dismiss();
                }
            });

        }else {
            etListName.setError("Cannot be empty");
        }
    }
}
