package com.example.sqllite_tuan6_ltdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class NotesAdapter extends BaseAdapter {

    private MainActivity context;
    private int layout;
    private List<NotesModel> notesList;

    public NotesAdapter(MainActivity context, int layout, List<NotesModel> notesList) {
        this.context = context;
        this.layout = layout;
        this.notesList = notesList;
    }

    private class ViewHolder{
        TextView tvNote;
        ImageView imgEdit;
        ImageView imgDelete;
    }

    @Override
    public int getCount() {
        return notesList.size();
    }

    @Override
    public Object getItem(int i) {
        return notesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            viewHolder.tvNote = (TextView) view.findViewById(R.id.tvNote);
            viewHolder.imgEdit = (ImageView) view.findViewById(R.id.btnEdit);
            viewHolder.imgDelete = (ImageView) view.findViewById(R.id.btnDelete);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        NotesModel notes = notesList.get(i);
        viewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cập nhật " + notes.getNameNote(), Toast.LENGTH_SHORT).show();
                context.DialogUpdateNotes(notes.getNameNote(), notes.getIdNote());
            }
        });
        viewHolder.tvNote.setText(notes.getNameNote());

        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.DialogDeleteNotes(notes.getNameNote(), notes.getIdNote());
            }
        });
        return view;
    }
}
