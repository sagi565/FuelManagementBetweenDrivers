package com.example.fuelmanagementbetweendrivers;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeFragment extends Fragment {
    FirebaseAuth auth;

    FirebaseUser user;
    TextView userDetails;
    Button logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        logout = view.findViewById(R.id.logout);
        userDetails = view.findViewById(R.id.userDetails);
        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent( getActivity().getApplicationContext(), Login.class);
            startActivity(intent);
            getActivity().finish();
        }
        else{
            userDetails.setText(user.getEmail());
            userDetails.setTextSize(15);
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent( getActivity().getApplicationContext(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}