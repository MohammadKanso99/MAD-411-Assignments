package com.example.assignment6

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    private lateinit var editTextName: EditText
    private lateinit var textViewResult: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // using id to find UI elements
        editTextName = findViewById(R.id.editTextName)
        textViewResult = findViewById(R.id.textViewResult)
        val buttonShow = findViewById<Button>(R.id.buttonShow)

        buttonShow.setOnClickListener {
            displayMessage(it)
        }
    }

    fun displayMessage(view: View) {
        val name = editTextName.text.toString().trim()
        textViewResult.text = "Hello, $name!"
    }
}
