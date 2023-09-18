package com.example.fuelmanagementbetweendrivers.Classes;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Model {
    private Model() {

    }

    private static Model instance;
    private Driver currentDriver;

    public Driver getCurrentDriver() {
        return currentDriver;
    }

    public void setCurrentDriver(Driver currentDriver) {
        this.currentDriver = currentDriver;
    }

    public static Model getInstance() {
        if (instance == null)
            instance = new Model();

        return instance;
    }

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference driversRef = db.collection("Drivers");
    private CollectionReference carsRef = db.collection("Cars");
    private DocumentReference currentDriverRef = driversRef.document(getAuthUser().getUid());;

    public FirebaseUser getAuthUser() {
        return mAuth.getCurrentUser();
    }

    public void setCurrentUser(){
        if (currentDriver == null) {
            for (int i = 0; i < driversRef.get().getResult().size(); i++) {
                if (driversRef.get().getResult().getDocuments().get(i).getData().get("ID").equals(mAuth.getUid())) {
                    currentDriver = driversRef.get().getResult().getDocuments().get(i).toObject(Driver.class);
                }
            }
        }
    }

    public void addCar(Car car) {
        carsRef.add(car)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {

                    }
                });
    }


}
