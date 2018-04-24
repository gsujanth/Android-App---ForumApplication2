package com.example.princ.inclass12;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ThreadsActivity extends AppCompatActivity implements ThreadsAdapter.DataUpdateAfterDelete {

    private final String TAG = "demoThreads";

    TextView userNameTV,currentThreadsTV;
    EditText addThreadET;
    ImageButton logOutButton,addThreadButton;
    ListView threadsListView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    ThreadsAdapter threadsAdapter;
    List<MessageThread> threadsList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads);
        setTitle("Message Threads");

        userNameTV = findViewById(R.id.userNameTV);
        currentThreadsTV=findViewById(R.id.currentThreadsTV);
        addThreadET=findViewById(R.id.addThreadET);
        logOutButton = findViewById(R.id.logOutButton);
        addThreadButton=findViewById(R.id.addThreadButton);
        threadsListView=findViewById(R.id.threadsListView);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        userNameTV.setTextColor(Color.parseColor("#000000"));
        currentThreadsTV.setTextColor(Color.parseColor("#000000"));

        if(user!=null) {
            userNameTV.setText(user.getDisplayName());
            getThreads();
        }else{
            Toast.makeText(this, "No user currently logged in", Toast.LENGTH_SHORT).show();
        }

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                user=null;
                mDatabase=null;
                mAuth=null;
                Intent intent = new Intent(ThreadsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=addThreadET.getText().toString();
                String user_name=user.getDisplayName();
                if(!title.isEmpty()) {
                    addThread(title,user_name);
                }else{
                    Toast.makeText(ThreadsActivity.this, "Enter Thread Title", Toast.LENGTH_SHORT).show();
                }
            }
        });

        threadsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ThreadsActivity.this, ChatActivity.class);
                intent.putExtra("messageThreadDetails",threadsList.get(i));
                startActivity(intent);
                finish();
            }
        });
    }

    public void addThread(String title,String user_name){
        if(user!=null) {
            mDatabase.child("message_threads").push().setValue(new MessageThread(title, user.getUid(),user_name));
            addThreadET.setText("");
        }else{
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteThread(String thread_id) {
       mDatabase.child("message_threads").child(thread_id).removeValue();
    }

    public void getThreads(){
        mDatabase.child("message_threads").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                threadsList.clear();
                for (DataSnapshot messageThreadSnapshot: dataSnapshot.getChildren()) {
                    MessageThread messageThread = messageThreadSnapshot.getValue(MessageThread.class);
                    if(messageThread!=null) {
                        messageThread.thread_id = messageThreadSnapshot.getKey();
                        Log.d(TAG, "onDataChange: "+messageThread.toString());
                    }
                    threadsList.add(messageThread);
                }
                threadsAdapter = new ThreadsAdapter(ThreadsActivity.this, R.layout.threads_listview, threadsList,ThreadsActivity.this);
                threadsListView.setAdapter(threadsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ThreadsActivity.this, "ThreadsActivity: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
