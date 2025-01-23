package com.sp.doactivitysh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sp.doactivitysh.Adapter.NoteListAdapter;
import com.sp.doactivitysh.Database.RoomDb;
import com.sp.doactivitysh.Interface.NotesClickListener;
import com.sp.doactivitysh.Model.Notes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    NoteListAdapter noteListAdapter;
    RoomDb database;
    List<Notes> notes = new ArrayList<>();
    FloatingActionButton fabBtn;
    SearchView searchView;
    Notes selectedNotes;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.noteRv);

        fabBtn = findViewById(R.id.addBtn);

        searchView = findViewById(R.id.searchView);

        database = RoomDb.getInstance(this);

        notes = database.mainDAO().getAll();

        updateRecycle(notes);


        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,NotesTakeActivity.class);
                startActivityForResult(intent,101);

            }

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newtext){
        List<Notes>filterList = new ArrayList<>();
        for (Notes singleNote:notes){
            if (singleNote.getTitle().toLowerCase().contains(newtext.toLowerCase())
                    ||singleNote.getNotes().toLowerCase().contains(newtext.toLowerCase())){
                filterList.add(singleNote);
            }
        }

        noteListAdapter.filterList(filterList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 101){
            if (resultCode == Activity.RESULT_OK){
                Notes new_notes = (Notes)data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                noteListAdapter.notifyDataSetChanged();


            }
        }else if (requestCode == 102){
            if (resultCode == Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getID(),new_notes.getTitle(),new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                noteListAdapter.notifyDataSetChanged();
            }
        }

    }

    private void updateRecycle(List<Notes>notes){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        noteListAdapter = new NoteListAdapter(MainActivity.this,notes,notesClickListener);
        recyclerView.setAdapter(noteListAdapter);
    }
    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {

            Intent intent = new Intent(MainActivity.this,NotesTakeActivity.class);
            intent.putExtra("old_notes",notes);
            startActivityForResult(intent,102);
        }

        @Override
        public void onLongpress(Notes notes, CardView cardView) {

            selectedNotes = new Notes();
            selectedNotes = notes;
            showPop(cardView);
        }
    };

    private void showPop(CardView cardView) {

        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if (item.getItemId() == R.id.pin){
            if (selectedNotes.isPinned()){
                database.mainDAO().pin(selectedNotes.getID(),false);
                Toast.makeText(this,"UnPinned",Toast.LENGTH_SHORT).show();

            }else {
                database.mainDAO().pin(selectedNotes.getID(),true);
                Toast.makeText(this,"Pinned",Toast.LENGTH_SHORT).show();
            }
            notes.clear();
            notes.addAll(database.mainDAO().getAll());
            noteListAdapter.notifyDataSetChanged();
            return true;
        }else if (item.getItemId() == R.id.delete){
            database.mainDAO().delete(selectedNotes);
            notes.remove(selectedNotes);
            noteListAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Yes !! Successfully Delete",Toast.LENGTH_SHORT).show();
            return true;
        }


        return false;
    }

}