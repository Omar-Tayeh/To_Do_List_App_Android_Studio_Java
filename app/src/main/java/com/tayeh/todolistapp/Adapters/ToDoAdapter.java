package com.tayeh.todolistapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tayeh.todolistapp.AddNewTask;
import com.tayeh.todolistapp.Helpers.DatabaseHelper;
import com.tayeh.todolistapp.MainActivity;
import com.tayeh.todolistapp.Model.ToDoModel;
import com.tayeh.todolistapp.R;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> modelList;
    private MainActivity activity;
    private DatabaseHelper myDb;
    public ToDoAdapter(DatabaseHelper myDb , MainActivity activity){
        this.activity = activity;
        this.myDb = myDb;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout , parent , false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = modelList.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(toBolean(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    myDb.updateStatus(item.getId() , 1);
                } else {
                    myDb.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    public boolean toBolean(int num){
        return num != 0;
    }

    public Context getContext(){
        return activity;
    }

    public void setTask(List<ToDoModel> modelList){
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    public void deleteTask(int id){
        ToDoModel item = modelList.get(id);
        myDb.deleteTask(item.getId());
        modelList.remove(id);
        notifyItemRemoved(id);
    }

    public void editTask(int id){
        ToDoModel item = modelList.get(id);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("task" , item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());
    }
    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
