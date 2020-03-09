package com.eldhose.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
    }

    public void createDocument(View view)
    {
        Toast.makeText(this, "createDocument", Toast.LENGTH_SHORT).show();
    }
    public void readDocument(View view)
    {
        Toast.makeText(this, "createDocument", Toast.LENGTH_SHORT).show();
        FirebaseFirestore.getInstance()
                .collection("notes")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.i("currentstatus","read is succesfull");
                        List<DocumentSnapshot> listSnapshots = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot listShot : listSnapshots)
                            Log.i("currentstatus",listShot.getData().toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("currentstatus","read not  succesfull : "+e);
                    }
                });
    }
    public void updateDocument(View view)
    {
        Toast.makeText(this, "createDocument", Toast.LENGTH_SHORT).show();
    }
    public void deleteDocument(View view)
    {
        Toast.makeText(this, "createDocument", Toast.LENGTH_SHORT).show();
    }
    public void getAllDocument(View view)
    {
        Toast.makeText(this, "createDocument", Toast.LENGTH_SHORT).show();
    }
    public void getAllDocumentWithRealTimeUpdates(View view)
    {
        Toast.makeText(this, "createDocument", Toast.LENGTH_SHORT).show();
    }
}
