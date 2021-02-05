package com.eachubkov.todoapp.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.res.use

fun hideKeyboard(activity: Activity) {
    val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun getArrayFromRes(activity: Activity, resArrayId: Int) : IntArray {
    return activity.resources.obtainTypedArray(resArrayId).use { typedArray ->
        IntArray(typedArray.length()) {
            typedArray.getColor(it, 0)
        }
    }
}