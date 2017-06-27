package com.world.bolandian.firebasewithdatabase.dialogs;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.world.bolandian.firebasewithdatabase.R;
import com.world.bolandian.firebasewithdatabase.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserShareLists extends DialogFragment {


    @BindView(R.id.rvShareUsers)
    RecyclerView rvShareUsers;
    Unbinder unbinder;
    DatabaseReference ref;

    public UserShareLists() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_share_lists, container, false);
        unbinder = ButterKnife.bind(this, view);
        //1)query the data for the view
        //1.1)get the user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)return view; // no user -> no user lists
        //1.2) get the ref to the user-table
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        //2)init a shoppingList Adapter
        UserShareListsAdapter adapter = new UserShareListsAdapter(ref,getContext());
        //3)set the layoutManager and adapter of the recyclerView
        rvShareUsers.setAdapter(adapter);
        rvShareUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static class UserShareListsAdapter extends FirebaseRecyclerAdapter<User,UserShareViewHolder> {
        private Context context;

        public UserShareListsAdapter(Query query,Context context) {
            super(User.class, R.layout.user_share_list_item, UserShareViewHolder.class, query);
            this.context = context;
        }

        @Override
        protected void populateViewHolder(UserShareViewHolder viewHolder,final User model, final int position) {

            viewHolder.userName.setText(model.getDisplayName());
            viewHolder.cimImageDisplay.setImageURI(Uri.parse(model.getProfileImage()));
            Glide.with(context).load(model.getProfileImage()).into(viewHolder.cimImageDisplay);

            viewHolder.btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Shared!", Toast.LENGTH_SHORT).show();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserLists").child(model.getUid());

                    Log.v(String.valueOf(ref),"ref");
                }
            });
        }
    }

    public static class UserShareViewHolder extends RecyclerView.ViewHolder{
        private CircularImageView cimImageDisplay;
        private TextView userName;
        private Button btnShare;

        public UserShareViewHolder(View itemView) {
            super(itemView);
            cimImageDisplay = (CircularImageView)itemView.findViewById(R.id.civImageDisplay);
            userName = (TextView)itemView.findViewById(R.id.tvDisplayName);
            btnShare = (Button)itemView.findViewById(R.id.btnShare);
        }
    }
}
