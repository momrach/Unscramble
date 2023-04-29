package com.example.android.unscramble.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Categories(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "category_name") val categoryName: String
)

@Entity(tableName = "words",
    foreignKeys = arrayOf(
        ForeignKey(entity = Categories::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categories_id"),
        onDelete = ForeignKey.CASCADE)
    )
)
data class Words (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "categories_id") val categories_id: Long,
    @ColumnInfo(name = "word") val word: String
)

