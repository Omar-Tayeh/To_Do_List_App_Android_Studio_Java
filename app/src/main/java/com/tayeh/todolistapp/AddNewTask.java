package com.tayeh.todolistapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tayeh.todolistapp.Helpers.DatabaseHelper;
import com.tayeh.todolistapp.Model.ToDoModel;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";

    //widget
    private EditText editText;
    private Button saveButton;
    private DatabaseHelper myDb;
    public static AddNewTask newRecord(){
     return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task , container , false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = view.findViewById(R.id.edittext);
        saveButton = view.findViewById(R.id.save_button);
        myDb = new DatabaseHelper(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            editText.setText(task);

            //disable button if text is empty.
            if (task.length() > 0) {
                saveButton.setEnabled(false);
            }
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    saveButton.setEnabled(false);
                    saveButton.setBackgroundColor(getResources().getColor(R.color.grey));
                } else {
                    saveButton.setEnabled(true);
                    saveButton.setBackgroundColor(getResources().getColor(R.color.navy));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();

                if (finalIsUpdate) {
                    myDb.updateTask(bundle.getInt("id"), text);
                } else {
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    myDb.insertTask(item);
                }
                dismiss();
            }
        });
    }

    //refresh view
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof onDialogCloseListner){
            ((onDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}
