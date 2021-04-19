package com.example.todo_android.Utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todo_android.Models.Task


class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "TODO_LIST"
    }

    private val TABLE_NAME = "TODO_Table"
    private val COLUMN_ID = "id"
    private val COLUMN_TITLE = "title"
    private val COLUMN_DESCRIPTION = "description"
    private val COLUMN_TIMESTAMP = "timestamp"
    private val COLUMN_STATUS = "status"

    private val CREATE_TABLE = (
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATE DEFAULT CURRENT_TIMESTAMP(datetime('now','localtime')),"
                    + COLUMN_STATUS + " TEXT"
                    + ")")

    val allNotes: ArrayList<Task>
        get() {
            val notes = ArrayList<Task>()
            val selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_TIMESTAMP + " DESC"

            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val task = Task(cursor!!.getInt(cursor.getColumnIndex(COLUMN_ID)), cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)), cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)))
                    task.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                    task.taskTitle = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                    task.taskDescription = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                    task.timestamp = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP))
                    task.status = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS))
                    notes.add(task)
                } while (cursor.moveToNext())
            }
            db.close()
            return notes
        }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun insertTask(task: Task): Long {
        val db = this.writableDatabase   // get writable database as we want to write data
        val values = ContentValues()
        values.put(COLUMN_TITLE, task.taskTitle)
        values.put(COLUMN_DESCRIPTION, task.taskDescription)
        values.put(COLUMN_STATUS, task.status)
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getTask(id: Long): Task {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_TIMESTAMP, COLUMN_STATUS), COLUMN_ID + "=?",
            arrayOf(id.toString()), null, null, null, null)

        cursor?.moveToFirst()
        val task = Task(
            cursor!!.getInt(cursor.getColumnIndex(COLUMN_ID)),
            cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
            cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
            cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)),
            cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)))

        cursor.close()
        return task
    }

    fun updateStatus(task: Task): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_STATUS, task.status)
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", arrayOf(task.id.toString()))
    }

    fun deleteTask(task: Task): Boolean {
        val db = writableDatabase
        db.delete(TABLE_NAME, COLUMN_ID + " LIKE ?", arrayOf(task.id.toString()))
        return true
    }
}