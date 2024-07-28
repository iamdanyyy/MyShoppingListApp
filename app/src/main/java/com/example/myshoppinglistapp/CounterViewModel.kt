package com.example.myshoppinglistapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CounterViewModel: ViewModel() {
    private val _sItems = mutableStateOf(listOf<ShoppingItem>())
    private val _showDialog = mutableStateOf(false)
    private val _itemName = mutableStateOf("")
    private val _itemQuantity = mutableStateOf("")

    val sItems: MutableState<List<ShoppingItem>> = _sItems
    val showDialog: MutableState<Boolean> = _showDialog
    val itemName: MutableState<String> = _itemName
    val itemQuantity: MutableState<String> = _itemQuantity
}