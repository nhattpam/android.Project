package com.example.myprojectprm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class ProfileFragment extends Fragment {
    private RecyclerView recyclerViewBills;
    private BillAdapter billAdapter;

    // Other code in the ProfileFragment
    private String username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Retrieve the passed username from the arguments
        username = getArguments().getString("username");
        // Initialize the RecyclerView
        recyclerViewBills = view.findViewById(R.id.recyclerViewBills);
        recyclerViewBills.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load and display the user's bills
        loadBills();

        return view;
    }

    private void loadBills() {
        // Retrieve the bills from the database
        List<Bill> billList = getBillsFromDatabase();

        // Set up the BillAdapter
        billAdapter = new BillAdapter(billList);
        recyclerViewBills.setAdapter(billAdapter);
    }

    private List<Bill> getBillsFromDatabase() {
        // Retrieve the bills for the logged-in user from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        int userId = databaseHelper.getUserIdByUsername(username);
        return databaseHelper.getBillsByUserId(userId);
    }

    // Other methods and classes in the ProfileFragment
}
