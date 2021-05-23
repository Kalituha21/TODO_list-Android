package lv.rtu.ourstudentid181rdc049and181rdc034.todo_list_android

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "tasks")
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val text: String,
    val created: Int,
    val completed: Int?
)

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAll(): Array<Task>

    @Query("SELECT * FROM tasks WHERE completed = NULL")
    fun getCompletedTasks(name: String): LiveData<Task>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Int): LiveData<Task>

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
