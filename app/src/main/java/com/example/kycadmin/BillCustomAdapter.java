package com.example.kycadmin;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class BillCustomAdapter extends RecyclerView.Adapter<BillCustomAdapter.MyViewHolder>
        implements Filterable {

    private ArrayList<Bill> dataSet;
    private ArrayList<Bill> dataSetFiltered;
    private ArrayList<Bill> dataSetOriginal;
    private Context context;
    StorageReference storageReference;
    private int mExpandedPosition = -1;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewBillNumber;
        TextView textViewBillDate;
        View viewSeparator;
        ImageView imageViewBillImage;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.bill_card_enterprise);
            this.textViewBillNumber = (TextView) itemView.findViewById(R.id.bill_card_bill_number);
            this.textViewBillDate = (TextView) itemView.findViewById(R.id.bill_card_bill_date);
            this.viewSeparator = (View) itemView.findViewById(R.id.bill_card_separator);
            this.imageViewBillImage = (ImageView) itemView.findViewById(R.id.bill_card_bill_image);
        }
    }

    public BillCustomAdapter(Context context, ArrayList<Bill> data) {
        this.dataSet = data;
        this.dataSetOriginal = data;
        this.context = context;
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
        View viewSeparator = holder.viewSeparator;
        ImageView imageViewBillImage = holder.imageViewBillImage;
        textViewName.setText(dataSet.get(listPosition).getEnterprise_name());
        textViewBillNumber.setText(dataSet.get(listPosition).getBill());
        textViewBillDate.setText(dataSet.get(listPosition).getDate());
        displayInitialPictures(dataSet.get(listPosition).getEnterprise_id(), dataSet.get(listPosition).getBill(), imageViewBillImage);

        final boolean isExpanded = listPosition==mExpandedPosition;
        imageViewBillImage.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        viewSeparator.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:listPosition;
                notifyItemChanged(listPosition);
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


    public void displayInitialPictures(String id, String billNo, final ImageView imageView){
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference mImageRef = storageReference.child("bills/"+id+"/"+billNo);
        mImageRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println(uri);
                        Glide.with(context).load(uri).into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println();
            }
        });
    }
}
