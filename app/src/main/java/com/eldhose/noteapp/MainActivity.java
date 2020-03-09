package com.eldhose.noteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, NotesRecyclerAdapter.NoteListener {

    RecyclerView recyclerView;
    NotesRecyclerAdapter notesRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView=findViewById(R.id.recyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogue();
            }
        });


    }
    private void showAlertDialogue()
    {
        final EditText noteEditText = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Add Note")
                .setView(noteEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addNotes(noteEditText.getText().toString());

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addNotes(String text)
    {
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        notes note=new notes(text, new Timestamp(new Date()),false,userId);

        FirebaseFirestore.getInstance()
                .collection("notes")
                .add(note);
    }

    private void startLoginActivity()
    {
        Intent intent =new Intent(this,LoginRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
        case R.id.action_profile :
                return true;
            case R.id.action_logout :
                AuthUI.getInstance().signOut(this);
                       /* .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    startLoginActivity();
                                else
                                    Log.e(TAG," onCompleteListener : "+task.getException());

                            }
                        });*/
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
        if(notesRecyclerAdapter!=null)
            notesRecyclerAdapter.stopListening();
    }

    //This function is called whenever there is a change in user state(log in or out , user tokens)
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startLoginActivity();
             return;
        }

        initRecyclerView(firebaseAuth.getCurrentUser());

    }

    private void initRecyclerView(FirebaseUser user) {
        Query query = FirebaseFirestore.getInstance()
                .collection("notes")
                .whereEqualTo("userId", user.getUid());

        FirestoreRecyclerOptions<notes> options= new FirestoreRecyclerOptions.Builder<notes>()
                .setQuery(query,notes.class)
                .build();
        notesRecyclerAdapter= new NotesRecyclerAdapter(options,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notesRecyclerAdapter);
        notesRecyclerAdapter.startListening();

        ItemTouchHelper itemtouchHelper= new ItemTouchHelper(simpleCallback);
        itemtouchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction == ItemTouchHelper.LEFT) {

                NotesRecyclerAdapter.NoteViewHolder noteViewHolder = (NotesRecyclerAdapter.NoteViewHolder) viewHolder;
                ((NotesRecyclerAdapter.NoteViewHolder) viewHolder).deleteNote();
            }
        }


        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent))
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
    @Override
    public void handleCheckChanged(boolean isChecked, DocumentSnapshot snapshot) {

        snapshot.getReference().update("completed",isChecked)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("currentstatus","handle check succesfull");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("currentstatus","handle check error"+e);
                    }
                });

    }

    @Override
    public void handleEditNote(final DocumentSnapshot snapshot) {

            final notes note = snapshot.toObject(notes.class);

            final EditText editText = new EditText(this);
            editText.setText(note.getText());
            editText.setSelection(note.getText().length());


            new AlertDialog.Builder(this)
                    .setTitle("Edit Note")
                    .setView(editText)
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                String newText =editText.getText().toString();
                                note.setText(newText);
                                snapshot.getReference().set(note);
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();
    }

    @Override
    public void handleDeleteNote(DocumentSnapshot snapshot) {

        final DocumentReference documentReference = snapshot.getReference();
        final notes note = snapshot.toObject(notes.class);

        documentReference.delete();

        Snackbar.make(recyclerView,"Item deleted",Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        documentReference.set(note);
                    }
                })
                .show();
    }
}
