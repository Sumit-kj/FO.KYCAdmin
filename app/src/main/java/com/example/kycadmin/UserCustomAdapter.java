package com.example.kycadmin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserCustomAdapter extends RecyclerView.Adapter<UserCustomAdapter.MyViewHolder>
        implements Filterable {

    private ArrayList<User> dataSet;
    private ArrayList<User> dataSetFiltered;
    private ArrayList<User> dataSetOriginal;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewPhone;
        CheckBox checkBoxIsApproved;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.user_card_name);
            this.textViewPhone = (TextView) itemView.findViewById(R.id.user_card_phone);
            this.checkBoxIsApproved = (CheckBox) itemView.findViewById(R.id.user_card_is_approved);
        }
    }

    public UserCustomAdapter(ArrayList<User> data) {
        this.dataSet = data;
        this.dataSetOriginal = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View cardView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(cardView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView textViewName = holder.textViewName;
        TextView textViewPhone = holder.textViewPhone;
        final CheckBox checkboxIsApproved = holder.checkBoxIsApproved;
        textViewName.setText(dataSet.get(listPosition).getName());
        textViewPhone.setText(dataSet.get(listPosition).getPhone());
        checkboxIsApproved.setChecked(dataSet.get(listPosition).getIsApproved() == 1);
        checkboxIsApproved.setOnClickListener(new View.OnClickListener() {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            @Override
            public void onClick(View v) {
                if(checkboxIsApproved.isChecked()){
                    database.child("users").child(dataSet.get(listPosition).getUser_id()).child("isApproved").setValue(1);
                }
                else if(!checkboxIsApproved.isChecked()){
                    database.child("users").child(dataSet.get(listPosition).getUser_id()).child("isApproved").setValue(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataSetFiltered = dataSet;
                } else {
                    List<User> filteredList = new ArrayList<>();
                    for (User row : dataSetOriginal) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    dataSetFiltered = (ArrayList<User>) filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataSetFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                dataSet.clear();
                dataSetFiltered = (ArrayList<User>) filterResults.values;
                dataSet = dataSetFiltered;
                notifyDataSetChanged();
            }
        };
    }
}