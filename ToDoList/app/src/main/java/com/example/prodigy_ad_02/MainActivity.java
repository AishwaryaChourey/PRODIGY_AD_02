package com.example.prodigy_ad_02;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTask;
    private ListView listViewTasks;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        listViewTasks = findViewById(R.id.listViewTasks);
        Button buttonAddTask = findViewById(R.id.buttonAddTask);

        tasks = new ArrayList<>();
        adapter = new TaskAdapter(tasks);
        listViewTasks.setAdapter(adapter);

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = editTextTask.getText().toString();
                if (!task.isEmpty()) {
                    tasks.add(task);
                    adapter.notifyDataSetChanged();
                    editTextTask.setText("");
                }
            }
        });

        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedTask = tasks.get(position);
                showEditDeleteDialog(position, selectedTask);
            }
        });
    }

    private void showEditDeleteDialog(final int position, String task) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_delete, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.editTextTaskDialog);
        editText.setText(task);

        dialogBuilder.setTitle("Edit Task");
        dialogBuilder.setPositiveButton("Edit", (dialog, which) -> {
            String editedTask = editText.getText().toString();
            if (!editedTask.isEmpty()) {
                tasks.set(position, editedTask);
                adapter.notifyDataSetChanged();
            }
            dialog.dismiss();
        });

        dialogBuilder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private class TaskAdapter extends ArrayAdapter<String> {
        private final ArrayList<String> tasks;

        TaskAdapter(ArrayList<String> tasks) {
            super(MainActivity.this, 0, tasks);
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_task, parent, false);
            }

            TextView textViewTask = convertView.findViewById(R.id.textViewTask);
            Button buttonDelete = convertView.findViewById(R.id.buttonDelete);

            String task = tasks.get(position);
            textViewTask.setText(task);

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tasks.remove(position);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }
}
