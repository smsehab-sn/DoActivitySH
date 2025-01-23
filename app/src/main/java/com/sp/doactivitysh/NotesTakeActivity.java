package com.sp.doactivitysh;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sp.doactivitysh.Model.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakeActivity extends AppCompatActivity {

    EditText titleED, notesED;
    ImageView saveBtn;
    Notes notes;

    boolean isOldNotes = false;
    ImageView network;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes_take);


        network = findViewById(R.id.network);
        saveBtn = findViewById(R.id.savebtn);
        titleED = findViewById(R.id.titleEdt);
        notesED = findViewById(R.id.noteEdt);
        notes = new Notes();
        try {
            notes = (Notes) getIntent().getSerializableExtra("old_notes");
            titleED.setText(notes.getTitle());
            notesED.setText(notes.getNotes());
            isOldNotes = true;
        }catch (Exception e){
            e.printStackTrace();
        }



       network.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(NotesTakeActivity.this, Network.class);
               startActivity(intent);


           }
       });









        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!isOldNotes){
                    notes = new Notes();
                }

                String title = titleED.getText().toString();
                String description = notesED.getText().toString();


                if (description.isEmpty()){
                    Toast.makeText(NotesTakeActivity.this,"Please Write Something",Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat format = new SimpleDateFormat("EEE,d MMM yyyy HH:mm a" );
                Date date = new Date();

                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(format.format(date));

                Intent intent = new Intent();
                intent.putExtra("note",notes);
                setResult(Activity.RESULT_OK,intent);
                finish();

            }
        });

    }
}