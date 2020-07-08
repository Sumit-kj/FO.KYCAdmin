package com.example.kycadmin.ui.dashboard;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kycadmin.Dealer;
import com.example.kycadmin.DealerCustomAdapter;
import com.example.kycadmin.MainActivity;
import com.example.kycadmin.R;
import com.example.kycadmin.User;
import com.example.kycadmin.UserCustomAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private static DealerCustomAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ProgressBar progressBar;
    private static ArrayList<Dealer> data;
    private static ArrayList<User> removedItems;
    private SearchView searchView;
    private DatabaseReference database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.dealer_recycler_view);
        progressBar = (ProgressBar) root.findViewById(R.id.dealer_progress_bar);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setHasOptionsMenu(true);
        data = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference();
        database.child("dealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Dealer dealer = new Dealer();
                    dealer.setName(String.valueOf(ds.getValue(Dealer.class).getName().toString()));
                    dealer.setContact(String.valueOf(ds.getValue(Dealer.class).getContact().toString()));
                    dealer.setEnterpriseName(ds.getValue(Dealer.class).getEnterpriseName());
                    dealer.setAddr1(ds.getValue(Dealer.class).getAddr1());
                    dealer.setAddr2(ds.getValue(Dealer.class).getAddr2());
                    dealer.setAddr3(ds.getValue(Dealer.class).getAddr3());
                    dealer.setGstin(ds.getValue(Dealer.class).getGstin());
                    dealer.setPan(ds.getValue(Dealer.class).getPan());
                    dealer.setLat(ds.getValue(Dealer.class).getLat());
                    dealer.setLon(ds.getValue(Dealer.class).getLon());
                    dealer.setId(ds.getValue(Dealer.class).getId());
                    dealer.setAadhar(ds.getValue(Dealer.class).getAadhar());
                    dealer.setAddedBy(ds.getValue(Dealer.class).getAddedBy());
                    data.add(dealer);
                }
                adapter = new DealerCustomAdapter(getContext(), data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
        removedItems = new ArrayList<User>();
        adapter = new DealerCustomAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
//        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
//        searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change]
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }
}
