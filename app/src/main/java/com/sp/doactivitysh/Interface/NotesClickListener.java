package com.sp.doactivitysh.Interface;

import androidx.cardview.widget.CardView;

import com.sp.doactivitysh.Model.Notes;

public interface NotesClickListener {

    void onClick(Notes notes);

    void onLongpress(Notes notes, CardView cardView);
}
