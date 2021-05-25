package lv.rtu.ourstudentid181rdc049and181rdc034.todo_list_android

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Entity(tableName = "tasks")
data class Task (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var text: String,
    var created: Long,
    var completed: Long? = null
)

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAll(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE completed <> NULL")
    fun getCompletedTasks(name: String): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Int): LiveData<Task>

    @Query("SELECT Count(*) FROM tasks")
    fun getCount(id: Int): Int

    @Insert
    fun insert(vararg task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)
}

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java, "task_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}

class TaskRepository(application: Application) {
    private val taskDao: TaskDao = TaskDatabase.getInstance(application).taskDao()
    private val tasks: LiveData<List<Task>>

    init {
        tasks = taskDao.getAll()
    }

    fun getTasks():  LiveData<List<Task>> = tasks

    fun saveNewTask(task: Task) = runBlocking {
        this.launch(Dispatchers.IO) {
            taskDao.insert(task)
        }
    }

    fun updateTask(task: Task) = runBlocking {
        this.launch(Dispatchers.IO) {
            taskDao.update(task)
        }
    }

    fun deleteTask(task: Task) = runBlocking {
        this.launch(Dispatchers.IO) {
            taskDao.delete(task)
        }
    }
}
