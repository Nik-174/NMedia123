package ru.netology.nmedia.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.impl.PostDaoImpl
import ru.netology.nmedia.dao.impl.PostTable

class AppDb private constructor(db : SQLiteDatabase) {

    val postDao : PostDao = PostDaoImpl(db)

    companion object{
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context : Context) : AppDb{
            return instance ?: synchronized(this){
                instance ?: AppDb(
                    buildDatabase(context, arrayOf(PostTable.DDL))
                ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context, DDLs : Array<String>) = DbHelper(
            context, 1, "app.db", DDLs,
        ).writableDatabase
    }

}
