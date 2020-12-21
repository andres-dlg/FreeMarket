package com.dlgsoft.freemarket.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dlgsoft.freemarket.data.db.entities.Category
import com.dlgsoft.freemarket.data.db.entities.SubCategory

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM Category WHERE serverId = :serverId")
    suspend fun getByServerId(serverId: String): Category?

    @Query("SELECT COUNT(*) FROM Category WHERE serverId = :serverId")
    suspend fun categoryExists(serverId: String): Int

    @Insert
    suspend fun insertCategory(category: Category): Long

    @Query("SELECT * FROM Category WHERE id = :categoryId")
    suspend fun getById(categoryId: Long): Category

    @Query("DELETE FROM Category")
    suspend fun clearCategories()

    @Query("SELECT * FROM SubCategory WHERE categoryId = :categoryId")
    suspend fun getSubcategoriesForCategoryId(categoryId: Long): List<SubCategory>

    @Insert
    suspend fun insertSubCategory(subCategory: SubCategory)

    @Query("DELETE FROM SubCategory")
    suspend fun clearSubCategories()
}