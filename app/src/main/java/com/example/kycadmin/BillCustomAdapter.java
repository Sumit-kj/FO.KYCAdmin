package com.example.kycadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BillCustomAdapter extends RecyclerView.Adapter<BillCustomAdapter.MyViewHolder>
        implements Filterable {

    private ArrayList<Bill> dataSet;
    private ArrayList<Bill> dataSetFiltered;
    private ArrayList<Bill> dataSetOriginal;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewBillNumber;
        TextView textViewBillDate;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.bill_card_enterprise);
            this.textViewBillNumber = (TextView) itemView.findViewById(R.id.bill_card_bill_number);
            this.textViewBillDate = (TextView) itemView.findViewById(R.id.bill_card_bill_date);
        }
    }

    public BillCustomAdapter(Context context, ArrayList<Bill> data) {
        this.dataSet = data;
        this.dataSetOriginal = data;
    }

    @Override
    public BillCustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View cardView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_card_layout, parent, false);
        BillCustomAdapter.MyViewHolder myViewHolder = new BillCustomAdapter.MyViewHolder(cardView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final BillCustomAdapter.MyViewHolder holder, final int listPosition) {
        TextView textViewName = holder.textViewName;
        TextView textViewBillNumber = holder.textViewBillNumber;
        TextView textViewBillDate = holder.textViewBillDate;
        textViewName.setText(dataSet.get(listPosition).getEnterprise_name());
        textViewBillNumber.setText(dataSet.get(listPosition).getBill());
        textViewBillDate.setText(dataSet.get(listPosition).getDate());
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
                    dataSetFiltered = dataSetOriginal;
                } else {
                    List<Bill> filteredList = new ArrayList<>();
                    for (Bill row : dataSetOriginal) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBill().toLowerCase().contains(charString.toLowerCase()) || row.getEnterprise_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    dataSetFiltered = (ArrayList<Bill>) filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataSetFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                dataSet.clear();
                dataSetFiltered = (ArrayList<Bill>) filterResults.values;
                dataSet = dataSetFiltered;
                notifyDataSetChanged();
            }
        };
    }
}
