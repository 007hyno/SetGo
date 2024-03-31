package com.paras.setgo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.paras.setgo.Models.HistoryItemModel;
import com.paras.setgo.Models.TaskItemModel;
import com.paras.setgo.Utilities.Constants;

import java.util.ArrayList;

public class dbHelper extends SQLiteOpenHelper {

    private static final String CREATE_TASK_TABLE = "CREATE TABLE "+ Constants.TABLE_NAME1+ "("
            +Constants.ROW_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +Constants.ROW_TASK_NAME+" TEXT,"
            +Constants.ROW_TOTAL_DURATION+" TEXT,"
            +Constants.ROW_SETS+" INTEGER,"
            +Constants.ROW_REPS+" INTEGER,"
            +Constants.ROW_DURATION+" INTEGER,"
            +Constants.ROW_REST+" INTEGER,"
            +Constants.ROW_DATE+" LONG);";

    private static final String CREATE_HISTORY_TABLE = "CREATE TABLE "+ Constants.TABLE_NAME2+ "("
            +Constants.ROW_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +Constants.ROW_TASK_NAME+" TEXT,"
            +Constants.ROW_TOTAL_DURATION+" TEXT,"
            +Constants.ROW_SETS+" INTEGER,"
            +Constants.ROW_REPS+" INTEGER,"
            +Constants.ROW_DURATION+" INTEGER,"
            +Constants.ROW_REST+" INTEGER,"
            +Constants.ROW_RATING+" INTEGER,"
            +Constants.ROW_DATE+" LONG);";

    public dbHelper(Context context) {
        super(context, Constants.DB_NAME,null,Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK_TABLE);
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME2);
        onCreate(db);
    }
    public void insertTaskItem(TaskItemModel data){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues taskItem = new ContentValues();
        taskItem.put(Constants.ROW_TASK_NAME, data.getTaskName());
        taskItem.put(Constants.ROW_TOTAL_DURATION, data.getTotalDuration());
        taskItem.put(Constants.ROW_SETS, data.getSets());
        taskItem.put(Constants.ROW_REPS, data.getReps());
        taskItem.put(Constants.ROW_DURATION, data.getDuration());
        taskItem.put(Constants.ROW_REST, data.getRest());
        taskItem.put(Constants.ROW_DATE, data.getDate());

        db.insert(Constants.TABLE_NAME1, null, taskItem);
    }

    public void updateTaskItem(String name,TaskItemModel data){
        ContentValues taskItem = new ContentValues();
        taskItem.put(Constants.ROW_TASK_NAME, data.getTaskName());
        taskItem.put(Constants.ROW_TOTAL_DURATION, data.getTotalDuration());
        taskItem.put(Constants.ROW_SETS, data.getSets());
        taskItem.put(Constants.ROW_REPS, data.getReps());
        taskItem.put(Constants.ROW_DURATION, data.getDuration());
        taskItem.put(Constants.ROW_REST, data.getRest());
        taskItem.put(Constants.ROW_DATE, data.getDate());

        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = Constants.ROW_TASK_NAME + " = ?";
            String[] whereArgs = {name}; // Replace with the appropriate identifier (e.g., task name)

        int numRowsUpdated = db.update(Constants.TABLE_NAME1, taskItem, whereClause, whereArgs);

        db.close();
    }

    public void insertHistoryItem(HistoryItemModel data){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues historyItem = new ContentValues();
        historyItem.put(Constants.ROW_TASK_NAME, data.getTaskName());
        historyItem.put(Constants.ROW_TOTAL_DURATION, data.getTotalDuration());
        historyItem.put(Constants.ROW_SETS, data.getSets());
        historyItem.put(Constants.ROW_REPS, data.getReps());
        historyItem.put(Constants.ROW_DURATION, data.getDuration());
        historyItem.put(Constants.ROW_REST, data.getRest());
        historyItem.put(Constants.ROW_DATE, data.getDate());
        historyItem.put(Constants.ROW_RATING, data.getRating());

        db.insert(Constants.TABLE_NAME2, null, historyItem);
    }
    public boolean checkDuplicate(String name){
        // Define a query to check for duplicate names
        String query = "SELECT COUNT(*) FROM "+Constants.TABLE_NAME1+" WHERE "+Constants.ROW_TASK_NAME+" = ?";

// Use SQLiteDatabase.rawQuery to execute the query
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = { name };
        Cursor cursor = db.rawQuery(query, selectionArgs);
// Check if there are duplicates
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();

            if (count > 0) {
                return true;
            }
        }
                return false;
    }

    public ArrayList<TaskItemModel> retrieveTaskItem(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.TABLE_NAME1 + " ORDER BY " + Constants.ROW_DATE , null);

        ArrayList<TaskItemModel> taskArray = new ArrayList<>();
        while (cursor.moveToNext()){
                taskArray.add(new TaskItemModel(cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getLong(5),cursor.getString(6),cursor.getLong(7) ));
        }
        return taskArray;
    }

    public ArrayList<HistoryItemModel> retrieveLyrics(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.TABLE_NAME2 + " ORDER BY " + Constants.ROW_DATE , null);

        ArrayList<HistoryItemModel> historyArray = new ArrayList<>();
        while (cursor.moveToNext()){
            historyArray.add(new HistoryItemModel(cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getLong(6),cursor.getLong(7),cursor.getLong(8) ));
        }
        return historyArray;
    }

    public boolean ifAnyTaskExist(String taskNameToCheck){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + Constants.TABLE_NAME1 + " WHERE " + Constants.ROW_TASK_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{taskNameToCheck});

        boolean taskExists = cursor.moveToFirst();
        cursor.close();
        return taskExists;

    }

    public void deleteTask(String name){
        SQLiteDatabase db = this.getWritableDatabase();

// Define the DELETE query with a WHERE clause
        String deleteQuery = "DELETE FROM "+Constants.TABLE_NAME1+" WHERE "+Constants.ROW_TASK_NAME+" = ?";

// Execute the DELETE query with the specified name as a parameter
        db.execSQL(deleteQuery, new String[]{name});

// Close the database connection
        db.close();
    }

}
