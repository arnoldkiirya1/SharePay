package com.sharepay.ug.Adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sharepay.ug.Model.GroupOption;
import com.sharepay.ug.R;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.GroupMemberViewHolder> {
    private List<GroupOption> groupMembers;

    public GroupMemberAdapter(List<GroupOption> groupMembers) {
        this.groupMembers = groupMembers != null ? groupMembers : new ArrayList<>();
    }

    @NonNull
    @Override
    public GroupMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new GroupMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMemberViewHolder holder, int position) {
        GroupOption groupMember = groupMembers.get(position);
        holder.userNameTextView.setText(groupMember.getUserName());
        holder.collectionShareTextView.setText("Ugx"+groupMember.getCollectionShare());
        holder.userIdTextView.setText(groupMember.getUserId());
    }

    @Override
    public int getItemCount() {
        return groupMembers.size();
    }

    public void setGroupMembers(List<GroupOption> groupMembers) {
        this.groupMembers = groupMembers;
        notifyDataSetChanged();
    }

    static class GroupMemberViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView collectionShareTextView;
        TextView userIdTextView;

        public GroupMemberViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.textView11);
            collectionShareTextView = itemView.findViewById(R.id.chatMessage3);
            userIdTextView = itemView.findViewById(R.id.chatMessage2);
        }
    }
}