package com.example.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoActivity extends Activity {

	final private String filename = "todo.txt";
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	EditText etNewItem;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        initialize();
    }


 	private void initialize() {
 		etNewItem = (EditText)findViewById(R.id.etNewItem);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
        lvItems.setAdapter(todoAdapter);
        setupListViewListener();		
	}


	private void setupListViewListener() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
				todoItems.remove(pos);
				todoAdapter.notifyDataSetChanged();
				writeItems();
				return true;
			}
			
		});
		
		lvItems.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, final View item, int pos, long id) {
                editAlert(item, pos);
			}
		});
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
        return true;
    }

 	public void onAddedItem(View v) {
 		String itemText = etNewItem.getText().toString();
		if ((itemText == null) || (itemText.trim().length() == 0)) {
	 		etNewItem.setText("");
			return;
		}
 		todoAdapter.add(itemText);
		writeItems();
 		etNewItem.setText("");
 	}
 	
 	private void readItems() {
 		File filesDir = getFilesDir();
 		File todoFile = new File(filesDir, filename);
 		try {
 			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
 		}
 		catch (IOException ex) {
 			todoItems = new ArrayList<String>();
 			ex.printStackTrace();
 		}
 	}
 	
 	private void writeItems() {
 		File filesDir = getFilesDir();
 		File todoFile = new File(filesDir, filename);
 		try {
 			FileUtils.writeLines(todoFile, todoItems);
 		}
 		catch (IOException ex) {
 			ex.printStackTrace();
 		}
 	}
 	
 	private void editAlert(View view, final int pos) {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ToDoActivity.this);
		
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setText(todoItems.get(pos));
		alertDialogBuilder.setTitle(R.string.edit_task);
		alertDialogBuilder.setMessage(R.string.edit_msg);
		alertDialogBuilder.setView(input);

		alertDialogBuilder.setPositiveButton(R.string.save,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						String value = input.getText().toString();
						if ((value == null) || (value.trim().length() == 0)) {
							input.setText("");
							return;
						}
						todoItems.set(pos, value);
						todoAdapter.notifyDataSetChanged();
						writeItems();
						input.setText("");
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		// show alert
		alertDialog.show();
	}

}
