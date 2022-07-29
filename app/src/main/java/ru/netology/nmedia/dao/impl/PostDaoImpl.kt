package ru.netology.nmedia.dao.impl

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    override fun getAll() = db.query(
        PostTable.NAME,
        PostTable.ALL_COLUMNS,
        null,
        null,
        null,
        null,
        "${PostTable.Column.ID.columnName} DESC"
    ).use {cursor ->
        List(cursor.count) {
            cursor.moveToNext()
            cursor.toModel()
        }
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            if (post.id != 0L) {
                put(PostTable.Column.ID.columnName, post.id)
            }
            put(PostTable.Column.AUTHOR.columnName, post.author)
            put(PostTable.Column.AUTHOR_AVATAR.columnName, post.authorAvatar)
            put(PostTable.Column.CONTENT.columnName, post.content)
            put(PostTable.Column.PUBLISHED.columnName, post.published)
            put(PostTable.Column.LIKED_BY_ME.columnName, post.likedByMe)
            put(PostTable.Column.LIKED_COUNT.columnName, post.likedCount)
            put(PostTable.Column.SHARED_COUNT.columnName, post.sharedCount)
            put(PostTable.Column.VIEWED_COUNT.columnName, post.viewedCount)
            put(PostTable.Column.VIDEO_URL.columnName, post.videoURL)
        }
        val id = db.replace(PostTable.NAME,null,values)
        db.query(
            PostTable.NAME,
            PostTable.ALL_COLUMNS,
            "${PostTable.Column.ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return it.toModel()
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
                UPDATE ${PostTable.NAME} SET
                    ${PostTable.Column.LIKED_COUNT.columnName} = ${PostTable.Column.LIKED_COUNT.columnName} + CASE WHEN ${PostTable.Column.LIKED_BY_ME.columnName} THEN -1 ELSE 1 END,
                    ${PostTable.Column.LIKED_BY_ME.columnName} = CASE WHEN ${PostTable.Column.LIKED_BY_ME.columnName} THEN 0 ELSE 1 END
                WHERE id=?
            """.trimIndent(),
            arrayOf(id) // массив аргументов
        )
    }


    override fun removeById(id: Long) {
        db.delete(
            PostTable.NAME,
            "${PostTable.Column.ID.columnName} = ?",
            arrayOf(id.toString())
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
                UPDATE ${PostTable.NAME} SET
                    ${PostTable.Column.SHARED_COUNT} = ${PostTable.Column.SHARED_COUNT} + 1
                WHERE id=?
            """.trimIndent(),
            arrayOf(id)
        )
    }

    private fun Cursor.toModel() = Post(
        id = getLong(getColumnIndexOrThrow(PostTable.Column.ID.columnName)), // получение по названию индекса, а по индексу содержимое колонки
        author = getString(getColumnIndexOrThrow(PostTable.Column.AUTHOR.columnName)),
        authorAvatar = getString(getColumnIndexOrThrow(PostTable.Column.AUTHOR_AVATAR.columnName)),
        content = getString(getColumnIndexOrThrow(PostTable.Column.CONTENT.columnName)),
        published = getString(getColumnIndexOrThrow(PostTable.Column.PUBLISHED.columnName)),
        likedByMe = getInt(getColumnIndexOrThrow(PostTable.Column.LIKED_BY_ME.columnName)) !=0,
        likedCount = getLong(getColumnIndexOrThrow(PostTable.Column.LIKED_COUNT.columnName)),
        sharedCount = getLong(getColumnIndexOrThrow(PostTable.Column.SHARED_COUNT.columnName)),
        viewedCount = getLong(getColumnIndexOrThrow(PostTable.Column.VIEWED_COUNT.columnName)),
        videoURL = getString(getColumnIndexOrThrow(PostTable.Column.VIDEO_URL.columnName))
    )
}