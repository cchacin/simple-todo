package com.github.cchacin.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.widget.AdapterView.OnItemLongClickListener;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> todoItems;
    private ArrayAdapter<String> aToDoAdapter;
    private EditText etEditText;
    private ListView lvItems;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        file = new File(getFilesDir(), "todo.txt");
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("text", todoItems.get(position));
                i.putExtra("position", position);
                startActivityForResult(i, 20);
            }
        });
    }

    private void populateArrayItems() {
        readItems();
        this.aToDoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    private void readItems() {
        try {
            file = new File(getFilesDir(), "todo.txt");
            todoItems = new ArrayList<>(FileUtils.readLines(file));
        } catch (IOException ignored) {
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException ignored) {
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(final View view) {
        aToDoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 20) {
            final String text = data.getExtras().getString("text");
            final int position = data.getExtras().getInt("position");
            todoItems.remove(position);
            todoItems.add(position, text);
            aToDoAdapter.notifyDataSetChanged();
            writeItems();
        }
    }
}
