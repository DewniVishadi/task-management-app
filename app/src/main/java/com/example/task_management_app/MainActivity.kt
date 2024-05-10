package com.example.task_management_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val newTaskActivityRequestCode = 1
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory((application as TasksApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = TaskListAdapter(taskViewModel);
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewTaskActivity::class.java)
            startActivityForResult(intent, newTaskActivityRequestCode)
        }

        
        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        taskViewModel.allTasks.observe(owner = this) { tasks ->
            // Update the cached copy of the words in the adapter.
            tasks.let { adapter.submitList(it) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newTaskActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val TASK_NAME = intentData?.getStringExtra("TASK_NAME").toString();
            val DESCRIPTION = intentData?.getStringExtra("DESCRIPTION").toString();
            val PRIORITY = intentData?.getIntExtra("PRIORITY",0);
            val DEADLINE = intentData?.getStringExtra("DEADLINE").toString();

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
