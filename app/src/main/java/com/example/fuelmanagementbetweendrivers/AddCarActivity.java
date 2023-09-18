package com.example.fuelmanagementbetweendrivers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuelmanagementbetweendrivers.Classes.Car;
import com.example.fuelmanagementbetweendrivers.Classes.Model;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddCarActivity extends AppCompatActivity {
    FirebaseAuth auth;

    FirebaseUser user;

    TextInputEditText editTextName, editTextCode;

    Button buttonCreate, buttonCancel;

    Model model = Model.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        editTextName = findViewById(R.id.name);
        editTextCode = findViewById(R.id.code);
        buttonCreate = findViewById(R.id.btnCreate);
        buttonCancel = findViewById(R.id.btnCancel);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String code = editTextCode.getText().toString();

                if(name == ""){
                    Toast.makeText(AddCarActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(code == ""){
                    Toast.makeText(AddCarActivity.this, "Enter code", Toast.LENGTH_SHORT).show();
                    return;
                }
                Car car = new Car(name, code, model.getCurrentDriver().getDriverID());
                model.getCurrentDriver().setCar(car);
                model.addCar(car);

                finish();


            }
        });


    }
}