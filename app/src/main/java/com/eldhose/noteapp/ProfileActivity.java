package com.eldhose.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    TextInputEditText textInputEditText;
    ProgressBar progressBar;
    Button updateProfileButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        circleImageView=findViewById(R.id.circleImageView);
        textInputEditText=findViewById(R.id.textInputEditText);
        progressBar=findViewById(R.id.progressBar);
        updateProfileButton=findViewById(R.id.updateProfileButton);
    }

    public void UpdateProfile(View view)
    {

    }
}
