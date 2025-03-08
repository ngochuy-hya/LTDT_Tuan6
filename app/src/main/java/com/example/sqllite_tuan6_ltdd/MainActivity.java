package com.example.sqllite_tuan6_ltdd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    ListView listView;
    ArrayList<NotesModel> arrayList;
    NotesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        InitDatabaseSQLite();

        listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new NotesAdapter(this, R.layout.item_row, arrayList);
        listView.setAdapter(adapter);

        setSupportActionBar(findViewById(R.id.toolbar));

        databaseSQLite();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            NotesModel note = arrayList.get(position);
            DialogUpdateNotes(note.getNameNote(), note.getIdNote());
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuAddNotes) {
            DialogAddNotes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void DialogAddNotes() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_note);

        EditText editText = dialog.findViewById(R.id.edtNoteName);
        Button buttonAdd = dialog.findViewById(R.id.btnAdd);
        Button buttonHuy = dialog.findViewById(R.id.btnCancel_Add);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString().trim();
                if(name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên Note!", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseHandler.QueryData("INSERT INTO Notes VALUES(null, '"+ name +"')");
                    Toast.makeText(MainActivity.this, "Đã thêm Note ", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    databaseSQLite();
                }
            }
        });

        buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void DialogUpdateNotes(String name, int id) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_notes);

        EditText editText = dialog.findViewById(R.id.edtNoteName);
        Button buttonEdit = dialog.findViewById(R.id.btnUpdate);
        Button buttonHuy = dialog.findViewById(R.id.btnCancel_Update);
        editText.setText(name);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString().trim();
                if(name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên Note", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseHandler.QueryData("UPDATE notes SET name_notes ='"+ name +"' WHERE id = '"+ id +"'");
                    Toast.makeText(MainActivity.this, "Đã cập nhật Note ", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    databaseSQLite();
                }
            }
        });

        buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void DialogDeleteNotes(String name, final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn xóa Note "+ name+ " không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHandler.QueryData("DELETE FROM notes WHERE id = '"+id+"'");
                Toast.makeText(MainActivity.this, "Đã xóa Notes "+name+" thành công", Toast.LENGTH_SHORT).show();
                databaseSQLite();
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    private void databaseSQLite() {
        arrayList.clear();
        Cursor cursor = databaseHandler.GetData("SELECT * FROM notes");
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            int id = cursor.getInt(0);
            arrayList.add(new NotesModel(id, name));
        }
        adapter.notifyDataSetChanged();
    }

    private void InitDatabaseSQLite() {
        databaseHandler = new DatabaseHandler(this, "22110332.sqlite", null, 1);
        databaseHandler.QueryData("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY AUTOINCREMENT, name_notes VARCHAR(150))");
    }
}