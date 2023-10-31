package com.sharepay.ug.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sharepay.ug.Model.GroupList;
import com.sharepay.ug.R;

import java.util.ArrayList;
import java.util.List;

public class GroupRequestsAdapter extends RecyclerView.Adapter<GroupRequestsAdapter.GroupRequestViewHolder> {
    private List<GroupList> requests;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(GroupList item);
    }

    public GroupRequestsAdapter(List<GroupList> requests, OnItemClickListener listener) {
        this.requests = requests != null ? requests : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
        return new GroupRequestViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GroupRequestViewHolder holder, int position) {
        GroupList request = requests.get(position);
        holder.groupNameTextView.setText(request.getGroupName());
        holder.recentTransTextview.setText("Target Fee: Ugx"+ request.getTargetFee());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(request);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void setRequests(List<GroupList> requests) {
        this.requests = requests;
        notifyDataSetChanged();
    }

    static class GroupRequestViewHolder extends RecyclerView.ViewHolder {
        TextView groupNameTextView;
        TextView recentTransTextview;
        ImageButton accept;
        ImageButton decline;

        public GroupRequestViewHolder(View itemView) {
            super(itemView);
            groupNameTextView = itemView.findViewById(R.id.chatName);
            recentTransTextview = itemView.findViewById(R.id.chatMessage);
            accept = itemView.findViewById(R.id.accept);
            decline = itemView.findViewById(R.id.decline);
        }
    }
}