package com.example.myshoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

@Composable
fun ShoppingListApp(viewModel: CounterViewModel) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { viewModel.showDialog.value = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(viewModel.sItems.value) {
                item ->
                if (item.isEditing) {
                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = {
                                         editedName,
                                         editedQuantity -> viewModel.sItems.value = viewModel.sItems.value.map {
                                             it.copy(
                                                 isEditing = false
                                             )
                                         }
                            val editedItem = viewModel.sItems.value.find { it.id == item.id }
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuantity
                            }
                        }
                    )
                } else {
                    ShoppingListItem(
                        item = item,
                        onEditClick = {
                            viewModel.sItems.value = viewModel.sItems.value.map {
                                it.copy(isEditing = it.id == item.id)
                            }
                        },
                        onDeleteClick = {
                            viewModel.sItems.value -= item
                        }
                    )
                }
            }
        }
    }
    
    if (viewModel.showDialog.value) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onDismissRequest = {},
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (viewModel.itemName.value.isNotBlank()) {
                                val newItem = ShoppingItem(
                                    id = viewModel.sItems.value.size + 1,
                                    name = viewModel.itemName.value,
                                    quantity = viewModel.itemQuantity.value.toInt(),
                                    isEditing = false
                                )
                                viewModel.sItems.value += newItem
                                viewModel.showDialog.value = false
                                viewModel.itemName.value = ""
                                viewModel.itemQuantity.value = ""
                            }
                        }
                    ) {
                        Text(text = "Add")
                    }
                    Button(onClick = { viewModel.showDialog.value = false }) {
                        Text(text = "Cancel")
                    }
                }
            },
            title = {Text("Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(
                        label = {Text(  "Enter Item")},
                        value = viewModel.itemName.value,
                        onValueChange = {viewModel.itemName.value = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        label = {Text(  "Enter Quantity")},
                        value = viewModel.itemQuantity.value,
                        onValueChange = {viewModel.itemQuantity.value = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editedQuantity,
                onValueChange = {editedQuantity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
        }) {
            Text(text = "Save")
        }
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color.Blue),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name, modifier = Modifier
            .padding(8.dp)
        )
        Text(text = "Qty: ${item.quantity}", modifier = Modifier
            .padding(8.dp)
        )
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Item")
            }
            IconButton(onClick = onDeleteClick ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Item")
            }
        }
    }
}