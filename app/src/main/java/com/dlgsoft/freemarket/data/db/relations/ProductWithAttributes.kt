package com.dlgsoft.freemarket.data.db.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.dlgsoft.freemarket.data.db.entities.Product
import com.dlgsoft.freemarket.data.db.entities.ProductAttribute
import com.dlgsoft.freemarket.data.db.entities.ProductPicture

data class ProductWithAttributes(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId",
        entity = ProductAttribute::class
    )
    val attributes: List<ProductAttribute>,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId",
        entity = ProductPicture::class
    )
    val pictures: List<ProductPicture>
)
