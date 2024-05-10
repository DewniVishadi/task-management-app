package com.example.task_management_app

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class NewTaskActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_item)
        val taskName = findViewById<EditText>(R.id.edit_word1)
        val description = findViewById<EditText>(R.id.edit_word2)
        val deadline = findViewById<DatePicker>(R.id.edit_word4)
        val button = findViewById<Button>(R.id.button_save)
        val numberPicker = findViewById<NumberPicker>(R.id.edit_word3);
        numberPicker.minValue = 0; // Set the minimum value
        numberPicker.maxValue = 5; // Set the maximum value
        numberPicker.value = 0; // Set the default value

        var selectedDate: String = ""

        deadline.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
             selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
        }


        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(taskName.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val taskName = taskName.text.toString()
                val description = description.text.toString()
                val priority = numberPicker.value
                val deadline = selectedDate
                replyIntent.putExtra("TASK_NAME", taskName)
                replyIntent.putExtra("DESCRIPTION", description)
                replyIntent.putExtra("PRIORITY", priority)
                replyIntent.putExtra("DEADLINE", deadline)

                println("taskName $taskName")
                println("description $description")
                println("priority $priority")
                println("deadline $deadline")
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
}
