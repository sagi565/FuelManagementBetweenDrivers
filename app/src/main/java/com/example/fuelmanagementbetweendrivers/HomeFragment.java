package com.example.fuelmanagementbetweendrivers;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fuelmanagementbetweendrivers.Classes.Driver;
import com.example.fuelmanagementbetweendrivers.Classes.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeFragment extends Fragment implements Model.IModelUpdate{
    FirebaseAuth auth;
    TextView userDetails;
    Button logout;
    Button addCar;
    LinearLayout carDetails;
    TextView carName, carCode;
    private Model model = Model.getInstance();
    private Driver currentDriver = model.getCurrentDriver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        logout = view.findViewById(R.id.logout);
        userDetails = view.findViewById(R.id.userDetails);
        auth = FirebaseAuth.getInstance();
        addCar = view.findViewById(R.id.btnAddCar);
        carDetails = view.findViewById(R.id.carDetails);
        carName = view.findViewById(R.id.carName);
        carCode = view.findViewById(R.id.carCode);

        if(currentDriver == null){
            Intent intent = new Intent( getActivity().getApplicationContext(), Login.class);
            startActivity(intent);
            getActivity().finish();
        }

        if(currentDriver.getCar() != null){
            addCar.setVisibility(View.GONE);
            carDetails.setVisibility(View.VISIBLE);

        }
        else{
            carDetails.setVisibility(View.GONE);
            addCar.setVisibility(View.VISIBLE);
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
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getActivity().getApplicationContext(), AddCarActivity.class);
                startActivity(intent);
                //getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void driverUpdate() {

    }

    @Override
    public void carUpdate() {

    }
}