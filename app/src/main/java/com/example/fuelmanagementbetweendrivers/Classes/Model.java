package com.example.fuelmanagementbetweendrivers.Classes;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;

public class Model {

    //region ModelUpdate
    public interface IModelUpdate {
        public void driverUpdate();

        public void carUpdate();

    }

    private ArrayList<IModelUpdate> iModelUpdates = new ArrayList<>();

    public void registerModelUpdate(IModelUpdate iModelUpdate) {
        this.iModelUpdates.add(iModelUpdate);
    }

    public void unRegisterModelUpdate(IModelUpdate iModelUpdate) {
        if (iModelUpdates.contains(iModelUpdate)) iModelUpdates.remove(iModelUpdate);
    }
    //endregion

    //region Properties, Constructor, Getters and Setters

    private Model() {
        registerDBRef();
    }

    private void setCurrentDriver(){
        if (currentDriver == null) {
            for (int i = 0; i < drivers.size(); i++) {
                if (drivers.get(i).getDocumentId().equals(mAuth.getUid())) {
                    currentDriver = drivers.get(i);
                }
            }
        }
    }

    private static Model instance;
    private Driver currentDriver;
    private ArrayList<Driver> drivers = new ArrayList<>();
    private ArrayList<Car> cars = new ArrayList<>();
    private static final String TAG = "FuelManagementBetweenDrivers.Model";
    private Context context;

    public ArrayList<Driver> getDrivers() {
        return drivers;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }


    public Driver getCurrentDriver() {
        return currentDriver;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static Model getInstance() {
        if (instance == null)
            instance = new Model();
        return instance;
    }


    //endregion

    //region FireBase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference driversRef;
    private CollectionReference carsRef;
    private DocumentReference driverRef;

    public FirebaseUser getAuthUser() {
        return mAuth.getCurrentUser();
    }

    private void registerDBRef() {
        if (getAuthUser() != null) {
            driversRef = db.collection("Drivers");
            carsRef = db.collection("Cars");
            driverRef = driversRef.document(getAuthUser().getUid());
            registerDriversData();
            registerCarData();

        } else {
            driversRef = null;
            carsRef = null;
            driverRef = null;
            if (driverListenerRegistration != null)
                driverListenerRegistration.remove();
            if (carsListenerRegistration != null)
                carsListenerRegistration.remove();
        }
    }
    //endregion

    //region Current Driver
    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //set user
//                        currentDriver = new Driver(getAuthUser()); TODO: לבדוק מה זה
//                        for(Car c : cars){
//                            if(c.getUser().getDocumentId().equals(currentDriver.getDocumentId())){
//                                currentDriver.setMyChargingStation(c);
//                            }
//                        }
                        setCurrentDriver();
                        raiseDriverUpdate();
                        raiseCarUpdate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: login " + e.getMessage());
                        Toast.makeText(context, "sign in failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        raiseDriverUpdate();
                    }
                });
    }

    public void createUser(String email, String password, String displayName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName)
                                .build();
                        mAuth.getCurrentUser().updateProfile(profileUpdates);
                        registerDBRef();
                        Driver driver = new Driver(displayName);
                        driver.setDocumentId(getAuthUser().getUid());
                        addDriver(driver);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(context, "Create user failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                        raiseDriverUpdate();
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
        registerDBRef();
        raiseDriverUpdate();
    }
    //endregion

    //region Drivers
    private ListenerRegistration driverListenerRegistration;

    private void updateDriver(Driver driver) {
        driverRef.set(driver);
    }

    private void raiseDriverUpdate() {
        for (IModelUpdate iModelUpdate : iModelUpdates) {
            iModelUpdate.driverUpdate();
        }
    }

    private void addDriver(Driver driver) {
        driversRef.document(driver.getDocumentId()).set(driver)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        raiseDriverUpdate();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(context, "Add Driver Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void registerDriversData() {
        driverListenerRegistration = driversRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e(TAG, "onEvent: drivers changed " + error.getMessage());
                    return;
                }
                Driver driver = null;
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    driver = documentChange.getDocument().toObject(Driver.class);
                    switch (documentChange.getType()) {
                        case ADDED:
                            driver.setDocumentId(documentChange.getDocument().getId());
                            drivers.add(driver);
                            break;
                        case MODIFIED:
                            String noteId = documentChange.getDocument().getId();
                            Driver driver1 = drivers.stream()
                                    .filter(n -> n.getDocumentId().equals(noteId))
                                    .findAny()
                                    .orElse(null);
                            if (driver1 != null) {
                                driver1.setCar(driver.getCar());
                                driver1.setName(driver.getName());
                                driver1.setDocumentId(driver1.getDocumentId());
                            }
                        case REMOVED:
                            drivers.remove(driver);
                            break;
                    }
                }
                setCurrentDriver();
                raiseDriverUpdate();
            }
        });
    }

    //endregion

    //region Cars

    public void addCar(Car car) {
        carsRef.add(car)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "onSuccess: added car " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {

                    }
                });
    }

    private ListenerRegistration carsListenerRegistration;

    private void registerCarData() {

        carsListenerRegistration = carsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                Car car = null;
                for (DocumentChange documentChange : value.getDocumentChanges()) {

                    car = documentChange.getDocument().toObject(Car.class);
                    switch (documentChange.getType()) {
                        case ADDED:
                            car.setDocumentId(documentChange.getDocument().getId());
                            cars.add(car);
//                            if (car.getUser().getDocumentId().equals(getAuthUser().getUid())) TODO: לבדוק את זה
//                                getCurrentDriver().setMyChargingStation(car);
                            break;
                        case MODIFIED:
                            String docId = documentChange.getDocument().getId();
                            Car car_1 = cars.stream()
                                    .filter(n -> n.getDocumentId().equals(docId))
                                    .findAny()
                                    .orElse(null);
                            if (car_1 != null) {
                                car_1.setDocumentId(car.getDocumentId());
                                car_1.setCode(car.getCode());
                                car_1.setDrivers(car.getDrivers());
                                car_1.setName(car.getName());
                            }
                        case REMOVED:
                            cars.remove(car);
                            break;
                    }
                }
                raiseCarUpdate();
            }
        });
    }

    public void updateCar(Car car) {
        carsRef.document(car.getDocumentId()).set(car)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: Car update " + car.getDocumentId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i(TAG, "onFailure:  Car update " + car.getDocumentId());
                    }
                });
    }

    private void raiseCarUpdate() {
        for (IModelUpdate iModelUpdate : iModelUpdates) {
            iModelUpdate.carUpdate();
        }
    }

    //endregion
}