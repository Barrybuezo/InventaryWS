package com.example.inventaryws.home.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.inventaryws.core.composables.IWSAsyncImage
import com.example.inventaryws.core.composables.InventaryWSText
import com.example.inventaryws.core.domain.model.Product
import com.example.inventaryws.home.domain.model.StockStatus
import com.example.inventaryws.home.domain.model.stockStatus

@Composable
fun IWSProductItem(
    product: Product,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = { onClick(product.documentId) },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IWSAsyncImage(
                data = product.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(color = Color.Gray.copy(alpha = 0.3f))
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                InventaryWSText(value = product.name)
                InventaryWSText(value = product.category)
                InventaryWSText(value = "Q${product.amount}")
                InventaryWSText(value = "Existencia: ${product.stock}")
            }

            val total = product.amount * product.stock
            InventaryWSText(value = "Q%.2f".format(total))
            StockStatusDot(status = product.stockStatus())
        }
    }
}

@Composable
private fun StockStatusDot(status: StockStatus) {
    val color = when (status) {
        StockStatus.NORMAL -> Color(0xFF3BA558)
        StockStatus.WARNING -> Color(0xFFF9A825)
        StockStatus.LOW -> Color(0xFFE53935)
    }

    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(CircleShape)
            .background(color)
    )
}