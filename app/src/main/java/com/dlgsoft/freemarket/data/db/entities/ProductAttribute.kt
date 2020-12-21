package com.dlgsoft.freemarket.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductAttribute(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(defaultValue = "")
    val name: String? = "",

    @ColumnInfo(defaultValue = "")
    val value: String? = "",

    val productId: Long? = 0L
)
