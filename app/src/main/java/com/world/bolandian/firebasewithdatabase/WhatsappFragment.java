package com.world.bolandian.firebasewithdatabase;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.world.bolandian.firebasewithdatabase.models.ChatMessage;
import com.world.bolandian.firebasewithdatabase.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class WhatsappFragment extends Fragment {
    private FirebaseDatabase mDatabase;
    private FirebaseUser user;
    private MediaPlayer mp;

    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.rvChat)
    RecyclerView rvChat;
    Unbinder unbinder;

    public WhatsappFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_whatsapp, container, false);
        unbinder = ButterKnife.bind(this, view);
        mp = MediaPlayer.create(getContext(),R.raw.whatapp_recive);
        mp.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        setupRecycler();
        return view;
    }

    private void setupRecycler() {
        MyChatAdapter adapter = new MyChatAdapter(getContext(),mDatabase.getReference("MyChat"));
        rvChat.setAdapter(adapter);
        rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChat.scrollToPosition(adapter.getItemCount());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnSend)
    public void onBtnSendClicked() {
        String text = etMessage.getText().toString();
        if(TextUtils.isEmpty(text))return;

        ChatMessage chat = new ChatMessage(new User(user),text);

        DatabaseReference chatTable = mDatabase.getReference("MyChat");
        DatabaseReference currentRow = chatTable.push();
        currentRow.setValue(chat);
        //the shorts way to do that
        //mDatabse .getReference("Chat").push().setValue(text);

        etMessage.setText(null);

    }

    private void readFromDB(){
        DatabaseReference chatRef = mDatabase.getReference("MyCoolChat");

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> rows = dataSnapshot.getChildren();

                for(DataSnapshot row: rows){
                    String text = row.getValue(String.class);
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readOnce(){
        mDatabase.getReference("MyCoolChat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot row : dataSnapshot.getChildren()){
                    Toast.makeText(getContext(), row.getValue(String.class), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readIncremental(){
        mDatabase.getReference("MyCoolChat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String text = dataSnapshot.getValue(String.class);
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Older viewHolder
    static class ChatAdapter extends FirebaseRecyclerAdapter<String,ChatAdapter.ChatViewHolder>{

        public ChatAdapter(Query ref) {
            super(String.class,R.layout.chat_item, ChatViewHolder.class, ref);
        }

        @Override
        protected void populateViewHolder(ChatViewHolder v, String text, int position) {
            v.tvChat.setText(text);
            //v.tvSenderTime.setText();
        }

        static class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView tvChat;

        public ChatViewHolder(View itemView) {
            super(itemView);
            tvChat = (TextView)itemView.findViewById(R.id.tvChat);

        }
      }
    }


    //Newer viewHolder
    public static class MyChatAdapter extends FirebaseRecyclerAdapter<ChatMessage,MyChatAdapter.MyChatViewHolder>{
        private Context context;

        public MyChatAdapter(Context context, Query ref) {
            super(ChatMessage.class, R.layout.chat_item, MyChatViewHolder.class, ref);
            this.context = context;
        }

        @Override
        protected void populateViewHolder(MyChatViewHolder viewHolder, ChatMessage model, int position) {
            viewHolder.tvMessage.setText(model.getMessage());
            viewHolder.tvSenderTime.setText(model.getSender() + "  "+ model.getTime());
            Glide.with(context).load(model.getProfileImage()).into(viewHolder.ivProfile);

        }

    public static class MyChatViewHolder extends RecyclerView.ViewHolder{
        CircularImageView ivProfile;
        TextView tvSenderTime,tvMessage;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            ivProfile = (CircularImageView)itemView.findViewById(R.id.ivProfile);
            tvSenderTime = (TextView)itemView.findViewById(R.id.tvTimeSender);
            tvMessage = (TextView)itemView.findViewById(R.id.tvChat);
          }
       }
    }
}
