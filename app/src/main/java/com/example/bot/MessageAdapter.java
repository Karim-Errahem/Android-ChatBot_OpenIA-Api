package com.example.bot;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatview = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new MyViewHolder(chatview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message = messageList.get(position);

        // Check who sent the message and show/hide views accordingly
        if (message.getSendby().equals(Message.send_by_me)) {
            // Hide the left chat bubble, show the right one
            holder.LeftChatView.setVisibility(View.GONE);  // Accessing LeftChatView through holder
            holder.rightChatView.setVisibility(View.VISIBLE);  // Accessing rightChatView through holder
            holder.RightTextView.setText(message.getMessage());  // Set message text on the right side
        } else {
            // Hide the right chat bubble, show the left one
            holder.rightChatView.setVisibility(View.GONE);  // Accessing rightChatView through holder
            holder.LeftChatView.setVisibility(View.VISIBLE);  // Accessing LeftChatView through holder
            holder.LeftTextView.setText(message.getMessage());  // Set message text on the left side
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // ViewHolder class to hold the views for each message
    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout LeftChatView, rightChatView;
        TextView LeftTextView, RightTextView;

        // Constructor to initialize views
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            LeftChatView = itemView.findViewById(R.id.left_chat_view);  // Find the left chat bubble layout
            rightChatView = itemView.findViewById(R.id.right_chat_view);  // Find the right chat bubble layout
            LeftTextView = itemView.findViewById(R.id.left_chat_text_view);  // Find the left text view
            RightTextView = itemView.findViewById(R.id.right_chat_text_view);  // Find the right text view
        }
    }
}
