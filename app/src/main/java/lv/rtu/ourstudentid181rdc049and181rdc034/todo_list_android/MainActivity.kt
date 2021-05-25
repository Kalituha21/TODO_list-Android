package lv.rtu.ourstudentid181rdc049and181rdc034.todo_list_android

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

// TODO nothing works
class MainActivity : AppCompatActivity(), TaskEventsInterface {
    private val taskViewModel: TasksViewModel by viewModels()
    private lateinit var taskRecyclerView: TaskRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tasks_list.layoutManager = LinearLayoutManager(this)
        taskRecyclerView = TaskRecyclerView(this)
        tasks_list.adapter = taskRecyclerView

        taskViewModel.getTasks().observe(this, Observer {
            taskRecyclerView.setTasks(it)
        })
    }

    override fun onItemDeleted(task: Task, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onViewClicked(task: Task) {
        TODO("Not yet implemented")
    }
}