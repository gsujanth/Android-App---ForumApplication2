package com.example.princ.inclass12;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

class ThreadsAdapter extends ArrayAdapter<MessageThread> {

    private Context ctx;
    private MessageThread messageThread;
    private DataUpdateAfterDelete dataUpdateAfterDelete;
    private FirebaseAuth mAuth;
    private final String TAG = "demoThreadAdapter";
    private ArrayList<MessageThread> messageThreadObjects;

    ThreadsAdapter(@NonNull Context context, int resource, @NonNull List<MessageThread> objects, ThreadsActivity threadsActivity) {
        super(context, resource, objects);
        this.ctx = context;
        this.dataUpdateAfterDelete=threadsActivity;
        this.messageThreadObjects = (ArrayList<MessageThread>) objects;
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        messageThread = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.threads_listview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.threadTitleTV = convertView.findViewById(R.id.threadTitleTV);
            viewHolder.deleteThreadButton = convertView.findViewById(R.id.deleteThreadButton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.threadTitleTV.setTextColor(Color.parseColor("#000000"));
        viewHolder.threadTitleTV.setText(messageThread.title);
        try {
            if (!messageThread.user_id.equals(mAuth.getCurrentUser().getUid())) {
                viewHolder.deleteThreadButton.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.deleteThreadButton.setVisibility(View.VISIBLE);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        viewHolder.deleteThreadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataUpdateAfterDelete.deleteThread(messageThreadObjects.get(position).thread_id);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView threadTitleTV;
        ImageButton deleteThreadButton;
    }

    public interface DataUpdateAfterDelete{
        void deleteThread(String thread_id);
    }

}

