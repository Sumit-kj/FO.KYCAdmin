package com.example.kycadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.widget.ListPopupWindow.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

public class DealerCustomAdapter extends RecyclerView.Adapter<DealerCustomAdapter.MyViewHolder>
        implements Filterable {

    private ArrayList<Dealer> dataSet;
    private ArrayList<Dealer> dataSetFiltered;
    private ArrayList<Dealer> dataSetOriginal;
    private int mExpandedPosition = -1;
    private Context context;
    private int mSelectedPosition;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageButton imageVIewOptions;
        TextView textViewPhone;
        TextView textViewEnterprise;
        TextView textViewAddr;
        TextView textViewAddr1;
        TextView textViewAddr2;
        TextView textViewAddr3;
        TextView textViewPAN;
        TextView textViewPANNumber;
        TextView textViewGSTIN;
        TextView textViewGSTINNUmber;
        TextView textViewAadhar;
        TextView textViewAadharNUmber;
        TextView textViewAddedBy;
        TextView textViewAddedByUser;
        TextView textViewAddedByUserSpace;
        TextView textViewAddedByUserPhone;
        View viewSeparator1;
        View viewSeparator2;
        ImageView imageViewMap;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.dealer_card_name);
            this.imageVIewOptions = (ImageButton) itemView.findViewById(R.id.dealer_card_options);
            this.textViewPhone = (TextView) itemView.findViewById(R.id.dealer_card_phone);
            this.textViewEnterprise = (TextView) itemView.findViewById(R.id.dealer_card_enterprise);
            this.textViewAddr = (TextView) itemView.findViewById(R.id.dealer_card_address);
            this.textViewAddr1 = (TextView) itemView.findViewById(R.id.dealer_card_address1);
            this.textViewAddr2 = (TextView) itemView.findViewById(R.id.dealer_card_address2);
            this.textViewAddr3 = (TextView) itemView.findViewById(R.id.dealer_card_address3);
            this.textViewPAN = (TextView) itemView.findViewById(R.id.dealer_card_pan);
            this.textViewPANNumber = (TextView) itemView.findViewById(R.id.dealer_card_pan_number);
            this.textViewGSTIN = (TextView) itemView.findViewById(R.id.dealer_card_gstin);
            this.textViewGSTINNUmber = (TextView) itemView.findViewById(R.id.dealer_card_gstin_number);
            this.textViewAadhar = (TextView) itemView.findViewById(R.id.dealer_card_aadhar);
            this.textViewAadharNUmber = (TextView) itemView.findViewById(R.id.dealer_card_aadhar_number);
            this.textViewAddedBy = (TextView) itemView.findViewById(R.id.dealer_card_addedby);
            this.textViewAddedByUser = (TextView) itemView.findViewById(R.id.dealer_card_addedby_user);
            this.textViewAddedByUserSpace = (TextView) itemView.findViewById(R.id.dealer_card_addedby_user_space);
            this.textViewAddedByUserPhone = (TextView) itemView.findViewById(R.id.dealer_card_addedby_user_mobile);
            this.viewSeparator1 = (View) itemView.findViewById(R.id.dealer_card_separator_1);
            this.viewSeparator2 = (View) itemView.findViewById(R.id.dealer_card_separator_2);
            this.imageViewMap = (ImageView) itemView.findViewById(R.id.dealer_card_map);
        }
    }

    public DealerCustomAdapter(Context context, ArrayList<Dealer> data) {
        this.dataSet = data;
        this.dataSetOriginal = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dealer_card_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(cardView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TextView textViewName = holder.textViewName;
        ImageButton imageButtonOptions = holder.imageVIewOptions;
        imageButtonOptions.setTag(position);
        TextView textViewPhone = holder.textViewPhone;
        TextView textViewEnterprise = holder.textViewEnterprise;
        TextView textViewAddr = holder.textViewAddr;
        TextView textViewAddr1 = holder.textViewAddr1;
        TextView textViewAddr2 = holder.textViewAddr2;
        TextView textViewAddr3 = holder.textViewAddr3;
        TextView textViewPAN = holder.textViewPAN;
        TextView textViewPANNumber = holder.textViewPANNumber;
        TextView textViewGSTIN = holder.textViewGSTIN;
        TextView textViewGSTINNUmber = holder.textViewGSTINNUmber;
        TextView textViewAadhar = holder.textViewAadhar;
        TextView textViewAadharNUmber = holder.textViewAadharNUmber;
        TextView textViewAddedBy = holder.textViewAddedBy;
        final TextView textViewAddedByUser = holder.textViewAddedByUser;
        final TextView textViewAddedByUserSpace = holder.textViewAddedByUserSpace;
        final TextView textViewAddedByUserPhone = holder.textViewAddedByUserPhone;
        View viewSeparator1 = holder.viewSeparator1;
        View viewSeparator2 = holder.viewSeparator2;
        ImageView imageViewMap = holder.imageViewMap;

        mSelectedPosition = position;

        final String mSelectedId = dataSet.get(position).getId();
        final String mSelectedPhone = dataSet.get(position).getContact();
        final DealerCustomAdapter adapter = new DealerCustomAdapter(context, dataSet);

        textViewName.setText(dataSet.get(position).getName());
        imageButtonOptions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_dealer_more_options, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final AlertDialog.Builder builder;
                        switch (item.getItemId()) {
                            case R.id.dealer_more_option_edit:
                                Intent intent = new Intent(context, EditDealerProfileActivity.class);
                                intent.putExtra(EditDealerProfileActivity.mIntentId, mSelectedId);
                                context.startActivity(intent);
                                break;
                            case R.id.dealer_more_option_edit_phone:
                                builder = new AlertDialog.Builder(context);
                                builder.setTitle("Edit Phone Number:");
                                final EditText inputPhone = new EditText(context);
                                inputPhone.setInputType(InputType.TYPE_CLASS_PHONE);
                                inputPhone.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                inputPhone.setText(dataSet.get(mSelectedPosition).getContact());
                                inputPhone.setSelection(dataSet.get(mSelectedPosition).getContact().length());
                                builder.setView(inputPhone);
                                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.setNeutralButton("Verify", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.setCancelable(true);
                                final AlertDialog dialog = builder.create();
                                inputPhone.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        if(inputPhone.getText().toString() == mSelectedPhone || inputPhone.getText().toString().length() != 10)
                                            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(false);
                                        else
                                            ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(true);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                    }
                                });
                                dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                                    @Override
                                    public void onShow(DialogInterface dialogInterface) {

                                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                                        button.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {
                                                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                                database.child("dealers").orderByChild("contact").equalTo(mSelectedPhone)
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot dealerSnapshot: dataSnapshot.getChildren()) {
                                                                    dealerSnapshot.getRef().child("contact").setValue(inputPhone.getText().toString());
                                                                    dataSet.get(mSelectedPosition).setContact(inputPhone.getText().toString());
                                                                    new DealerCustomAdapter(context, dataSet).notifyDataSetChanged();
//                                                                    notifyDataSetChanged();
//                                                                    notifyItemChanged(mSelectedPosition);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                            }
                                                        });
                                                Toast toast = Toast.makeText(context, "Phone Number Updated", Toast.LENGTH_SHORT);
                                                toast.show();
                                                dialog.dismiss();
                                            }
                                        });
                                        button.setEnabled(false);
                                        button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                                        button.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {
                                                String phoneNumber = inputPhone.getText().toString();
                                                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                                    database.child("dealers").orderByChild("contact").equalTo(phoneNumber)
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.getChildrenCount() == 0) {
                                                                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
                                                                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                                                }
                                                                else{
                                                                    inputPhone.setError("Duplicate Phone Number Found!");
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                            }
                                                        });


                                            }
                                        });
                                        button.setEnabled(false);
                                        button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                                        button.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                });
                                dialog.show();
                                break;
                            case R.id.dealer_more_option_delete:
                                builder = new AlertDialog.Builder(context);
                                builder.setTitle("Delete this record?");
                                builder.setMessage("Are you sure you want to delete this Record?");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                        database.child("dealers").orderByChild("id").equalTo(mSelectedId)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot dealerSnapshot: dataSnapshot.getChildren()) {
                                                            dealerSnapshot.getRef().removeValue();
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    }
                                                });
                                        Toast.makeText(context, "Record deleted Successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context, "Record not Deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                builder.show();
                        }
                        return false;
                    }
                });
            }
        });
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").orderByKey().equalTo(dataSet.get(position).getAddedBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() == 0){
                            textViewAddedByUser.setText("---");
                            textViewAddedByUserPhone.setText("");
                            textViewAddedByUserPhone.setVisibility(View.GONE);
                            textViewAddedByUserSpace.setVisibility(View.GONE);
                        }
                        else {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                textViewAddedByUser.setText(userSnapshot.getValue(User.class).getName());
                                textViewAddedByUserPhone.setText("(" + userSnapshot.getValue(User.class).getPhone() + ")");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        textViewPhone.setText(dataSet.get(position).getContact());
        textViewEnterprise.setText(dataSet.get(position).getEnterpriseName());
        textViewAddr1.setText(dataSet.get(position).getAddr1());
        textViewAddr2.setText(dataSet.get(position).getAddr2());
        textViewAddr3.setText(dataSet.get(position).getAddr3());
        textViewPANNumber.setText(dataSet.get(position).getPan());
        textViewGSTINNUmber.setText(dataSet.get(position).getGstin());
        textViewAadharNUmber.setText(dataSet.get(position).getAadhar());

        final boolean isExpanded = position==mExpandedPosition;
        imageButtonOptions.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewAddr.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewAddr1.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewAddr2.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewAddr3.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewGSTIN.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewGSTINNUmber.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewAadhar.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewAadharNUmber.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewPAN.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewPANNumber.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewAddedBy.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewAddedByUser.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewAddedByUserSpace.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        textViewAddedByUserPhone.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        viewSeparator1.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        viewSeparator2.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        imageViewMap.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        final int listPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:listPosition;
                notifyItemChanged(listPosition);
            }
        });
        final Double latitude = dataSet.get(position).getLat();
        final Double longitude = dataSet.get(position).getLon();
        imageViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=%f,%f", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
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
                    List<Dealer> filteredList = new ArrayList<>();
                    for (Dealer row : dataSetOriginal) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getContact().contains(charSequence) || row.getEnterpriseName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    dataSetFiltered = (ArrayList<Dealer>) filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataSetFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                dataSet.clear();
                dataSetFiltered = (ArrayList<Dealer>) filterResults.values;
                dataSet = dataSetFiltered;
                notifyDataSetChanged();
            }
        };
    }
}
