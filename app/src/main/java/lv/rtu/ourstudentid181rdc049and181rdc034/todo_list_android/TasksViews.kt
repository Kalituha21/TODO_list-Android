package lv.rtu.ourstudentid181rdc049and181rdc034.todo_list_android

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.task_item.view.*


interface TaskEventsInterface {
    fun onItemDeleted(task: Task, position: Int)
    fun onViewClicked(task: Task)
}

class TaskRecyclerView(taskEventsListener: TaskEventsInterface) : RecyclerView.Adapter<TaskRecyclerView.ViewHolder>() {
    private var tasks: List<Task> = arrayListOf()
    private val listener: TaskEventsInterface = taskEventsListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position], listener)
    }

    override fun getItemCount(): Int = tasks.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task, listener: TaskEventsInterface) {
            itemView.task_item_text.text = task.text
            itemView.task_item_complete_date.text = ""

            if (task.completed != null) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = Date((task.completed ?: (System.currentTimeMillis() / 1000L)) * 1000)
                itemView.task_item_complete_date.text = dateFormat.format(date)
            }

            itemView.setOnClickListener {
                listener.onViewClicked(task)
            }
        }
    }

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }
}

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: TaskRepository = TaskRepository(application)

    fun getTasks(): LiveData<List<Task>> {
        return repo.getTasks()
    }

    fun saveNewTask(task: Task) {
        repo.saveNewTask(task)
    }

    fun updateTask(task: Task) {
        repo.updateTask(task)
    }

    fun deleteTask(task: Task) {
        repo.deleteTask(task)
    }

    fun changeStatus(task: Task) {
        task.completed = if (task.completed == null) (System.currentTimeMillis() / 1000L) else null
        repo.updateTask(task)
    }
}