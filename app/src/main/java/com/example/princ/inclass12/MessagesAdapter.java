package com.example.princ.inclass12;

import android.content.Context;
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

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

class MessagesAdapter extends ArrayAdapter<Message> {

    private Context ctx;
    Message message;
    private final String TAG = "demoMessageAdapter";
    private MessagesAdapter.DataUpdateAfterMessageDelete dataUpdateAfterMessageDelete;
    private FirebaseAuth mAuth;
    private ArrayList<Message> messageObjects;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
    private PrettyTime p = new PrettyTime();
    private Date convertedDate;

    MessagesAdapter(@NonNull Context context, int resource, @NonNull List<Message> objects, ChatActivity chatActivity) {
        super(context, resource, objects);
        this.ctx = context;
        this.messageObjects = (ArrayList<Message>) objects;
        this.dataUpdateAfterMessageDelete = chatActivity;
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        message = getItem(position);
        MessagesAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.messages_listview, parent, false);
            viewHolder = new MessagesAdapter.ViewHolder();
            viewHolder.messageTV = convertView.findViewById(R.id.messageTV);
            viewHolder.messageSenderTV = convertView.findViewById(R.id.messageSenderTV);
            viewHolder.messageTimeTV = convertView.findViewById(R.id.messageTimeTV);
            viewHolder.deleteMessageButton = convertView.findViewById(R.id.deleteMessageButton);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MessagesAdapter.ViewHolder) convertView.getTag();
        }

        if (!message.user_id.equals(mAuth.getCurrentUser().getUid())) {
            viewHolder.deleteMessageButton.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.deleteMessageButton.setVisibility(View.VISIBLE);
        }
        viewHolder.messageSenderTV.setText(message.user_name);
        viewHolder.messageTV.setText(message.message);

        try {
            convertedDate = dateFormat.parse(message.created_time);
            p.setReference(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.messageTimeTV.setText(p.format(convertedDate));

        viewHolder.deleteMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataUpdateAfterMessageDelete.deleteMessage(messageObjects.get(position).message_id);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView messageTV, messageSenderTV, messageTimeTV;
        ImageButton deleteMessageButton;
    }

    public interface DataUpdateAfterMessageDelete {
        void deleteMessage(String message_id);
    }
}