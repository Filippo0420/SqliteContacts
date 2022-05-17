package com.fpp.sqlitecontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnAddNew;
    private Button btnClearCompleted;
    private Button btnSave;
    private Button btnCancel;
    private EditText etNewName;
    private EditText etNewSurname;
    private EditText etNewPhone;
    private EditText etNewMail;
    private ListView lvTodos;
    private LinearLayout llControlButtons;
    private LinearLayout llNewTaskButtons;

    private ContactsDbAdapter todoDbAdapter;
    private Cursor todoCursor;
    private List<Contacts> tasks;
    private ContactsAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUiElements();
        initListView();
        initButtonsOnClickListeners();
    }

    private void initUiElements() {
        btnAddNew = findViewById(R.id.btnAddNew);
        btnClearCompleted = findViewById(R.id.btnClearCompleted);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        etNewName = findViewById(R.id.etNewName);
        etNewSurname = findViewById(R.id.etNewSurname);
        etNewPhone = findViewById(R.id.etNewPhone);
        etNewMail = findViewById(R.id.etNewMail);
        lvTodos = findViewById(R.id.lvTodos);
        llControlButtons = findViewById(R.id.llControlButtons);
        llNewTaskButtons = findViewById(R.id.llNewTaskButtons);
    }

    private void initListView() {
        fillListViewData();
        //initListViewOnItemClick();
    }

    private void fillListViewData() {
        todoDbAdapter = new ContactsDbAdapter(getApplicationContext());
        todoDbAdapter.open();
        getAllTasks();
        listAdapter = new ContactsAdapter(this, tasks);
        lvTodos.setAdapter(listAdapter);
    }

    private void getAllTasks() {
        tasks = new ArrayList<Contacts>();
        todoCursor = getAllEntriesFromDb();
        updateTaskList();
    }

    private Cursor getAllEntriesFromDb() {
        todoCursor = todoDbAdapter.getAllContacts();
        if(todoCursor != null) {
            startManagingCursor(todoCursor);
            todoCursor.moveToFirst();
        }
        return todoCursor;
    }

    private void updateTaskList() {
        if(todoCursor != null && todoCursor.moveToFirst()) {
            do {
                long id = todoCursor.getLong(ContactsDbAdapter.ID_COLUMN);
                String name = todoCursor.getString(ContactsDbAdapter.NAME_COLUMN);
                String surname = todoCursor.getString(ContactsDbAdapter.SURNAME_COLUMN);
                String phone = todoCursor.getString(ContactsDbAdapter.PHONE_COLUMN);
                String mail = todoCursor.getString(ContactsDbAdapter.MAIL_COLUMN);
                tasks.add(new Contacts(id, name, surname, phone, mail));
            } while(todoCursor.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        if(todoDbAdapter != null)
            todoDbAdapter.close();
        super.onDestroy();
    }

    private void initListViewOnItemClick() {
        lvTodos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                Contacts task = tasks.get(position);
                /*if(task.isCompleted()){
                    todoDbAdapter.updateContacts(task.getId(), task.getDescription(), false);
                } else {
                    todoDbAdapter.updateContacts(task.getId(), task.getDescription(), true);
                }*/
                updateListViewData();
            }
        });
    }

    private void updateListViewData() {
        todoCursor.requery();
        tasks.clear();
        updateTaskList();
        listAdapter.notifyDataSetChanged();
    }

    private void initButtonsOnClickListeners() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnAddNew:
                        addNewTask();
                        break;
                    case R.id.btnSave:
                        saveNewTask();
                        break;
                    case R.id.btnCancel:
                        cancelNewTask();
                        break;
                    case R.id.btnClearCompleted:
                        //clearCompletedTasks();
                        break;
                    default:
                        break;
                }
            }
        };
        btnAddNew.setOnClickListener(onClickListener);
        btnClearCompleted.setOnClickListener(onClickListener);
        btnSave.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
    }

    private void showOnlyNewTaskPanel() {
        setVisibilityOf(llControlButtons, false);
        setVisibilityOf(llNewTaskButtons, true);
        setVisibilityOf(etNewName, true);
        setVisibilityOf(etNewSurname, true);
        setVisibilityOf(etNewPhone, true);
        setVisibilityOf(etNewMail, true);
    }

    private void showOnlyControlPanel() {
        setVisibilityOf(llControlButtons, true);
        setVisibilityOf(llNewTaskButtons, false);
        setVisibilityOf(etNewName, false);
        setVisibilityOf(etNewSurname, false);
        setVisibilityOf(etNewPhone, false);
        setVisibilityOf(etNewMail, false);
    }

    private void setVisibilityOf(View v, boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        v.setVisibility(visibility);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etNewName.getWindowToken(), 0);
    }

    private void addNewTask(){
        showOnlyNewTaskPanel();
    }

    private void saveNewTask(){
        String taskName = etNewName.getText().toString();
        String taskSurname = etNewSurname.getText().toString();
        String taskPhone = etNewPhone.getText().toString();
        String taskMail = etNewMail.getText().toString();
        if(taskName.equals("")){
            etNewName.setError("Your task description couldn't be empty string.");
        } else {
            todoDbAdapter.insertContacts(taskName, taskSurname, taskPhone, taskMail);
            etNewName.setText("");
            etNewSurname.setText("");
            etNewPhone.setText("");
            etNewMail.setText("");
            hideKeyboard();
            showOnlyControlPanel();
        }
        updateListViewData();
    }

    private void cancelNewTask() {
        etNewName.setText("");
        etNewSurname.setText("");
        etNewPhone.setText("");
        etNewMail.setText("");
        showOnlyControlPanel();
    }
/*
    private void clearCompletedTasks(){
        if(todoCursor != null && todoCursor.moveToFirst()) {
            do {
                if(todoCursor.getInt(ContactsDbAdapter.COMPLETED_COLUMN) == 1) {
                    long id = todoCursor.getLong(ContactsDbAdapter.ID_COLUMN);
                    todoDbAdapter.deleteContacts(id);
                }
            } while (todoCursor.moveToNext());
        }
        updateListViewData();
    }*/
}