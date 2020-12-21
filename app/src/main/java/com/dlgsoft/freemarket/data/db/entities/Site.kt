package com.dlgsoft.freemarket.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Site(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(defaultValue = "")
    val code: String? = "",

    @ColumnInfo(defaultValue = "")
    val name: String? = "",

    @ColumnInfo(defaultValue = "")
    val defaultCurrencyCode: String? = ""
) {
    override fun toString(): String {
        return name ?: ""
    }
}