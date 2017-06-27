package com.world.bolandian.firebasewithdatabase;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.world.bolandian.firebasewithdatabase.dialogs.AddListFragment;
import com.world.bolandian.firebasewithdatabase.dialogs.UserShareLists;
import com.world.bolandian.firebasewithdatabase.models.ShoppingLists;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment {


    @BindView(R.id.fabAddList)
    FloatingActionButton fabAddList;
    @BindView(R.id.rvShoppingLists)
    RecyclerView rvShoppingLists;
    Unbinder unbinder;
    private FragmentManager fm;

    public ShoppingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        fm = getChildFragmentManager();
        //1)query the data for the view
        //1.1)get the user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)return view; // no user -> no user lists
        //1.2) get the ref to the user-table
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserLists").child(user.getUid());

        //2)init a shoppingList Adapter
        ShoppingListAdapter adapter = new ShoppingListAdapter(ref,fm);
        //3)set the layoutManager and adapter of the recyclerView
        rvShoppingLists.setAdapter(adapter);
        rvShoppingLists.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fabAddList)
    public void onFabAddListsClicked() {
        /*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference  ref =  FirebaseDatabase.getInstance().getReference("UserShoppingLists").child(user.getUid());
        ref.push().setValue(new ShoppingLists());  */
        AddListFragment dialog = new AddListFragment();
        dialog.show(getChildFragmentManager(),"addListDialog");
    }

    public static class ShoppingListAdapter extends FirebaseRecyclerAdapter<ShoppingLists,ShoppingListAdapter.ShoppingListViewHolder>{
                private FragmentManager fragmentManager;
        public ShoppingListAdapter(Query query,FragmentManager fragmentManager) {
            super(ShoppingLists.class, R.layout.shopping_list_name_item, ShoppingListViewHolder.class, query);
             this.fragmentManager = fragmentManager;
        }

        @Override
        protected void populateViewHolder(final ShoppingListViewHolder viewHolder, final ShoppingLists model, int position) {
            viewHolder.tvListName.setText(model.getName());
            viewHolder.fabShareList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserShareLists dialog = new UserShareLists();
                    dialog.show(fragmentManager,"UserShowLists");
                }
            });
        }

        public static class ShoppingListViewHolder extends RecyclerView.ViewHolder{
        private TextView tvListName;
        private FloatingActionButton fabShareList;

        public ShoppingListViewHolder(View itemView) {
            super(itemView);
            tvListName = (TextView)itemView.findViewById(R.id.tvListname);
            fabShareList = (FloatingActionButton)itemView.findViewById(R.id.fabListShare);
        }
      }
    }
}
