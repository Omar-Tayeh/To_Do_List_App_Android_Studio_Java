package com.tayeh.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tayeh.todolistapp.Adapters.ToDoAdapter;
import com.tayeh.todolistapp.Helpers.DatabaseHelper;
import com.tayeh.todolistapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements onDialogCloseListner{

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private DatabaseHelper myDb;
    private List<ToDoModel> modelList;
    private ToDoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        myDb = new DatabaseHelper(MainActivity.this);
        modelList = new ArrayList<>();
        adapter = new ToDoAdapter(myDb , MainActivity.this);

        modelList = myDb.getAllTasks();
        Collections.reverse(modelList);
        adapter.setTask(modelList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newRecord().show(getSupportFragmentManager() , AddNewTask.TAG);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        modelList = myDb.getAllTasks();
        Collections.reverse(modelList);
        adapter.setTask(modelList);
        adapter.notifyDataSetChanged();
    }
}