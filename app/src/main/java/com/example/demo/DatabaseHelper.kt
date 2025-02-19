package com.example.demo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "students.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "students"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ROLL = "rollNumber"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_CITY = "city"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_ROLL TEXT, $COLUMN_NAME TEXT, $COLUMN_CITY TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertStudent(student: Student): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ROLL, student.rollNumber)
            put(COLUMN_NAME, student.name)
            put(COLUMN_CITY, student.city)
        }
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return success != -1L
    }

    fun getAllStudents(): List<Student> {
        val students = mutableListOf<Student>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_ID DESC")
        while (cursor.moveToNext()) {
            val rollNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLL))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val city = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY))
            students.add(Student(rollNumber, name, city))
        }
        cursor.close()
        db.close()
        return students
    }
}