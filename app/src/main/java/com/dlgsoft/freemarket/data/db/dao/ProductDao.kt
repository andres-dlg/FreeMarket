package com.dlgsoft.freemarket.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.dlgsoft.freemarket.data.db.entities.Product
import com.dlgsoft.freemarket.data.db.entities.SubCategory
import com.dlgsoft.freemarket.data.db.relations.ProductWithAttributes

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product WHERE serverId = :serverId")
    fun getByServerId(serverId: String): Product?

    @Insert
    fun insertProduct(product: Product): Long

    @Transaction
    @Query("SELECT * FROM Product WHERE categoryId = :categoryId")
    fun getAllProductsByCategoryId(categoryId: Long): List<ProductWithAttributes>

    @Transaction
    @Query("SELECT * FROM Product WHERE id = :productId")
    fun getById(productId: Long): ProductWithAttributes

    @Query("DELETE FROM Product")
    fun clearData()

    @Query("SELECT DISTINCT serverId FROM Product WHERE categoryId = :categoryId")
    fun getProductsIdsForCategoryId(categoryId: Long): List<String>

    @Query("SELECT * FROM SubCategory WHERE categoryId = :categoryId")
    fun getSubCategoriesForCategoryId(categoryId: Long): List<SubCategory>

    @Transaction
    @Query("SELECT * FROM Product")
    fun getAllProducts(): List<ProductWithAttributes>
}