package com.example.task_management_app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.map
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val newTaskActivityRequestCode = 1
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TasksApplication).repository)
    }

    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH) + 1 // Month starts from 0
    private val day = calendar.get(Calendar.DAY_OF_MONTH)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val searchText = findViewById<EditText>(R.id.searchText)
        val adapter = TaskListAdapter(taskViewModel, applicationContext);
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewTaskActivity::class.java)
            startActivityForResult(intent, newTaskActivityRequestCode)
        }

//        searchText.requestFocus()
        val noResult = findViewById<TextView>(R.id.noResult)

        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                println("CharSequence $s")
                val res = taskViewModel.getSearchedTasks(s.toString())
                res.observe(owner = this@MainActivity) { tasks ->
                    tasks.let { adapter.submitList(it) } }
                if(res.value == null) {
                    taskViewModel.allTasks.observe(owner = this@MainActivity) { tasks ->
                        // Update the cached copy of the words in the adapter.
                        tasks.let { adapter.submitList(it) }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called after the text has been changed.
                val text = s.toString()
                // Do something with the updated text
            }
        })

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        taskViewModel.allTasks.observe(owner = this) { tasks ->
            // Update the cached copy of the words in the adapter.
            tasks.let { adapter.submitList(it) }
        }

        val extras = intent.extras
        val TASK_NAME = extras?.getString("TASK_NAME")
        val DESCRIPTION = extras?.getString("DESCRIPTION")
        val PRIORITY = extras?.getInt("PRIORITY")
        var DEADLINE = extras?.getString("DEADLINE")
        var EDITED = extras?.getBoolean("EDITED")

        if(EDITED == true) {
            if (TASK_NAME != null && DESCRIPTION != null && PRIORITY != null && DEADLINE != null) {
                taskViewModel.update(applicationContext, TASK_NAME, DESCRIPTION, PRIORITY, DEADLINE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        if (requestCode == newTaskActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val TASK_NAME = intentData?.getStringExtra("TASK_NAME").toString();
            val DESCRIPTION = intentData?.getStringExtra("DESCRIPTION").toString();
            val PRIORITY = intentData?.getIntExtra("PRIORITY",0);
            var DEADLINE = intentData?.getStringExtra("DEADLINE").toString();

            if(DEADLINE.isEmpty()){
                DEADLINE = "$year-$month-$day"
            }

            println("TASK_NAME $TASK_NAME")
            println("DESCRIPTION $DESCRIPTION")
            println("PRIORITY $PRIORITY")
            println("DEADLINE $DEADLINE")

            if (PRIORITY != null) {
                taskViewModel.insert(applicationContext ,TASK_NAME, DESCRIPTION, PRIORITY.toInt(), DEADLINE)
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
