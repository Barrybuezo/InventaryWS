package com.example.inventaryws.core.util

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction


fun formKeyboardActions(
    index: Int,
    focusRequesters: List<FocusRequester>,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
): KeyboardActions {
    val isLast = index == focusRequesters.lastIndex
    return if (isLast) {
        KeyboardActions(onDone = { focusManager.clearFocus() })
    } else {
        KeyboardActions(
            onNext = {
                focusRequesters[index + 1].requestFocus()
                keyboardController?.show()
            }
        )
    }
}

fun formImeAction(index: Int, lastIndex: Int): ImeAction =
    if (index == lastIndex) ImeAction.Done else ImeAction.Next

@Composable
fun rememberFocusRequesters(count: Int): List<FocusRequester> {
    return remember { List(count) { FocusRequester() } }
}