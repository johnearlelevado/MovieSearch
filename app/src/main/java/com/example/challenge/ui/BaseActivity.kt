package com.example.challenge.ui

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


open class BaseActivity : AppCompatActivity() {

    open fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    open fun hideKeyboard(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}