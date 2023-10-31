package com.sharepay.ug.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sharepay.ug.MainActivity;
import com.sharepay.ug.Model.Messages;
import com.sharepay.ug.R;

import java.util.List;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private static int VIEW_TYPE ;
    private List<Messages> messages;
    private int userId; // Current user's ID

    public MessageAdapter(Context context, List<Messages> messages, int userId) {
        this.messages = messages;
        this.userId = userId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 1) {
            view = inflater.inflate(R.layout.incoming_chat_item, parent, false);
        } else {
            view = inflater.inflate(R.layout.incoming_chat_item, parent, false);
        }

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Messages message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages message = messages.get(position);
        Log.d("UserID", "message.getUserId(): " + message.getUserId());
        Log.d("UserID", "MainActivity.userPhoneNumber: " + MainActivity.userPhoneNumber);
        if(Objects.equals(message.getUserId(), MainActivity.userPhoneNumber)){
            return 1;
        } else{
            return 2;
        }

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView;
        private TextView messageTextView;
        private TextView userIdTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize view elements
            dateTextView = itemView.findViewById(R.id.textView19);
            messageTextView = itemView.findViewById(R.id.textView16);
            userIdTextView = itemView.findViewById(R.id.textView18);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Messages message) {
            // Bind data to view elements based on message direction (incoming or outgoing)
            dateTextView.setText(message.getTimestamp()); // Replace with the actual date field in your Messages class
            messageTextView.setText("Has deposited Ugx " + message.getAmountDeposited() + " to group wallet, charge fee: 0.0 "); // Replace with the actual message field in your Messages class
            userIdTextView.setText(message.getUserName()); // Replace with the actual user ID field in your Messages class
        }
    }

   
}
