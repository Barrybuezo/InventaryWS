package com.example.inventaryws.home.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.inventaryws.home.domain.model.Product

@Composable
fun IWSProductItem(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IWSAsyncImage(
                data = product.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(130.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(color = Color.Gray.copy(alpha = 0.3f))
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                InventaryWSText(value = product.name)
                InventaryWSText(value = product.category)
                InventaryWSText(value = product.stock.toString())
            }

        }
    }

}