package com.github.cchacin.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.github.cchacin.simpletodo.models.Item;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class MainActivity extends AppCompatActivity {
    private List<Item> todoItems;
    private ArrayAdapter<Item> aToDoAdapter;
    @Bind(R.id.etEditText) EditText etEditText;
    @Bind(R.id.lvItems) ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        populateArrayItems();
        lvItems.setAdapter(aToDoAdapter);
    }

    @OnItemLongClick(R.id.lvItems) boolean onItemLongClick(int position) {
        todoItems.remove(position);
        aToDoAdapter.notifyDataSetChanged();
        return true;
    }

    @OnItemClick(R.id.lvItems) void onItemClick(int position) {
        final Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        i.putExtra("text", todoItems.get(position).getText());
        i.putExtra("position", position);
        startActivityForResult(i, 20);
    }

    private void populateArrayItems() {
        todoItems = Item.listAll(Item.class);
        this.aToDoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoItems);
        aToDoAdapter.notifyDataSetChanged();
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
        Item item = new Item(etEditText.getText().toString());
        item.save();
        todoItems.add(item);
        etEditText.setText("");
        aToDoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 20) {
            populateArrayItems();
        }
    }
}
