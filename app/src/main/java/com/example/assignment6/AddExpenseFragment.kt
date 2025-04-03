package com.example.assignment6

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.util.Calendar
import java.util.Locale

class AddExpenseFragment : Fragment() {

    private var expenseToEdit: Expense? = null
    private lateinit var expenseDateEditText: EditText
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_expense, container, false)

        val expenseNameEditText: EditText = view.findViewById(R.id.editTextName)
        expenseDateEditText = view.findViewById(R.id.editTextDate)
        val expenseAmountEditText: EditText = view.findViewById(R.id.editTextAmount)
        val saveButton: Button = view.findViewById(R.id.submitButton)
        val financialTipsButton: Button = view.findViewById(R.id.financialTipsButton)


        financialTipsButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.theglobeandmail.com/investing/personal-finance/")
            startActivity(intent)
        }

        // Open DatePicker when clicking the date field
        expenseDateEditText.apply {
            isFocusable = false
            isClickable = true
            setOnClickListener {
                showDatePickerDialog()
            }
        }

        // Retrieve expense data if editing
        arguments?.let {
            val id = it.getInt("expenseId", -1)
            val name = it.getString("expenseName", "")
            val amount = it.getDouble("expenseAmount", 0.00)
            val date = it.getString("expenseDate", "")

            if(id != -1) {
                expenseToEdit = Expense(id, name, amount, date)
                expenseNameEditText.setText(name)
                expenseAmountEditText.setText(amount.toString())
                expenseDateEditText.setText(date)

            }

        }

        saveButton.setOnClickListener {
            val expenseName = expenseNameEditText.text.toString()
            val expenseAmount = expenseAmountEditText.text.toString().toDoubleOrNull() ?: 0.0
            val expenseDate = expenseDateEditText.text.toString()
            val expenseId = expenseToEdit?.id ?: (System.currentTimeMillis() / 1000).toInt()

            val bundle = Bundle().apply {
                putInt("expenseId", expenseId)
                putString("expenseName", expenseName)
                putDouble("expenseAmount", expenseAmount)
                putString("expenseDate", expenseDate)
            }

            val navController = findNavController()
            navController.previousBackStackEntry?.savedStateHandle?.set("newExpense", bundle)
            navController.popBackStack()
        }

        return view
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                expenseDateEditText.setText(formattedDate)
            },
            year, month, day
        )

        datePickerDialog.show()
    }
}