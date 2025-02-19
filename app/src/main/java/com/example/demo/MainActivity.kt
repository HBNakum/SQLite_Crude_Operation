package com.example.demo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var adapter: StudentAdapter
    private lateinit var studentList: MutableList<Student>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        studentList = databaseHelper.getAllStudents().toMutableList()
        adapter = StudentAdapter(studentList)

        findViewById<RecyclerView>(R.id.rv_students).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        val rollNumber = findViewById<EditText>(R.id.et_roll_number)
        val name = findViewById<EditText>(R.id.et_name)
        val city = findViewById<EditText>(R.id.et_city)
        val submitBtn = findViewById<Button>(R.id.btn_submit)
        val cancelBtn = findViewById<Button>(R.id.btn_cancel)

        submitBtn.setOnClickListener {
            val roll = rollNumber.text.toString().trim()
            val fullName = name.text.toString().trim()
            val cityName = city.text.toString().trim()

            if (roll.isNotEmpty() && fullName.isNotEmpty() && cityName.isNotEmpty()) {
                val student = Student(roll, fullName, cityName)
                if (databaseHelper.insertStudent(student)) {
                    studentList.add(0, student)
                    adapter.notifyDataSetChanged()
                    rollNumber.text.clear()
                    name.text.clear()
                    city.text.clear()
                }
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

        cancelBtn.setOnClickListener {
            rollNumber.text.clear()
            name.text.clear()
            city.text.clear()
        }
    }
}