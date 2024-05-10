package com.example.task_management_app

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class NewTaskActivity : AppCompatActivity() {
    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH) + 1 // Month starts from 0
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
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

        val extras = intent.extras
        val TASK_NAME = extras?.getString("TASK_NAME")
        val DESCRIPTION = extras?.getString("DESCRIPTION")
        val PRIORITY = extras?.getInt("PRIORITY")
        var DEADLINE = extras?.getString("DEADLINE")
        var NEED_TO_EDIT = extras?.getBoolean("NEED_TO_EDIT")


        if (DEADLINE != null) {
            if(DEADLINE.isEmpty()){
                DEADLINE = "$year-$month-$day"
            }
        }

        fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

        println("TASK_NAME EDIT $TASK_NAME")
        println("DESCRIPTION EDIT $DESCRIPTION")
        println("PRIORITY EDIT $PRIORITY")
        println("DEADLINE EDIT $DEADLINE")

        if(taskName.toString().isNotEmpty() && NEED_TO_EDIT == true){
            taskName.isEnabled = false
            if (TASK_NAME != null) {
                taskName.text = TASK_NAME.toEditable()
            }
            if (DESCRIPTION != null) {
                description.text = DESCRIPTION.toEditable()
            }
            if (PRIORITY != null) {
                numberPicker.value = PRIORITY
            }
            deadline.updateDate(DEADLINE.toString().split(Regex("-")).get(0).toInt(), DEADLINE.toString().split(Regex("-")).get(1).toInt() - 1, DEADLINE.toString().split(Regex("-")).get(2).toInt());
        }
        else taskName.isEnabled = true

        var selectedDate: String = ""
        var selectedDesc: String = ""

        deadline.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
             selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
        }

        if(NEED_TO_EDIT == true){
            selectedDate = DEADLINE.toString()
        }

        selectedDesc = description.text.toString().ifEmpty {
            "Not provided"
        }


        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(taskName.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val taskName = taskName.text.toString()
                val description = selectedDesc
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

                if(NEED_TO_EDIT == true){
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("TASK_NAME", taskName)
                    intent.putExtra("DESCRIPTION", description)
                    intent.putExtra("PRIORITY", priority)
                    intent.putExtra("DEADLINE", deadline)
                    intent.putExtra("EDITED", true)
                    applicationContext.startActivity(intent)
                }
                else setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
}
