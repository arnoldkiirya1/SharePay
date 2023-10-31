package com.sharepay.ug.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sharepay.ug.Model.GroupList;
import com.sharepay.ug.R;

import java.util.ArrayList;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder> {
    private List<GroupList> groups;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(GroupList item);
    }

    public GroupsAdapter(List<GroupList> groups, OnItemClickListener listener) {
        this.groups = groups != null ? groups : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupsAdapter.GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_item, parent, false);
        return new GroupsAdapter.GroupsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GroupsAdapter.GroupsViewHolder holder, int position) {
        GroupList group = groups.get(position);
        holder.groupNameTextView.setText(group.getGroupName());
        holder.recent_transTextview.setText(group.getRecentTransaction().substring(0, 28) + "...");
        holder.timeTextview.setText(group.getdateUpdated());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(group);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void setGroups(List<GroupList> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    static class GroupsViewHolder extends RecyclerView.ViewHolder {
        TextView groupNameTextView;
        TextView recent_transTextview;
        TextView timeTextview;

        public GroupsViewHolder(View itemView) {
            super(itemView);
            groupNameTextView = itemView.findViewById(R.id.chatName);
            recent_transTextview = itemView.findViewById(R.id.chatMessage);
            timeTextview = itemView.findViewById(R.id.timestamp);
        }
    }
}