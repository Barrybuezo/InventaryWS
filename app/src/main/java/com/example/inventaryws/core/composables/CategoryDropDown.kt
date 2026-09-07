package com.example.inventaryws.core.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.inventaryws.core.domain.model.CreateProductCategory

@OptIn(ExperimentalMaterial3Api::class) //permiso
@Composable
fun CategoryDropDown(
    selectedCategory: CreateProductCategory,
    onCategorySelected: (CreateProductCategory) -> Unit, //avisar cuando cambie opcion
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) } //controla si la lista de opciones está abierta o cerrada

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }, //cada vez que el usuario toque el cuadro se invierte su estado
        modifier = modifier
    ) {
        InventaryStockOutlinedTextField(
            value = selectedCategory.label,
            onValueChange = {},
            readOnly = true,
            label = "Categoría",
            placeholder = "",
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } //si el usuario toca en otra parte que no sea el menú, lo cierra
        ) {
            CreateProductCategory.entries.forEach { category ->
                DropdownMenuItem(
                    text = { InventaryWSText(value = category.label) },
                    onClick = {
                        expanded = false
                        onCategorySelected(category)
                    }
                )
            }
        }
    }
}