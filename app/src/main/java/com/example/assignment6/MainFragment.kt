package com.example.assignment6

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

private const val FILE_NAME = "expenses.txt"

class MainFragment : Fragment(), ExpenseAdapter.ExpenseListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private val expenseList = mutableListOf<Expense>()
    private lateinit var footerFragment: FooterFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        expenseList.clear()
        expenseList.addAll(loadExpensesFromFile(requireContext()))

        footerFragment = FooterFragment()
        addFooterFragment()
        update()

        expenseAdapter = ExpenseAdapter(expenseList, this)
        recyclerView.adapter = expenseAdapter

        val addExpenseButton: FloatingActionButton = view.findViewById(R.id.addExpenseFab)
        addExpenseButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addExpenseFragment)
        }

        return view
    }

    private fun addFooterFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.footer, footerFragment)
            .commit()
    }

    private fun update(){
        val total = expenseList.sumOf { it.amount }
        footerFragment.update(total)
    }

    override fun onDetails(expense: Expense) {
        val bundle = Bundle().apply {
            putInt("expenseId", expense.id)
            putString("expenseName", expense.name)
            putDouble("expenseAmount", expense.amount)
            putString("expenseDate", expense.date)
        }
        findNavController().navigate(R.id.action_mainFragment_to_DetailsFragment, bundle)
    }

    override fun onEditClick(expense: Expense) {
        val bundle = Bundle().apply {
            putInt("expenseId", expense.id)
            putString("expenseName", expense.name)
            putDouble("expenseAmount", expense.amount)
            putString("expenseDate", expense.date)
        }
        findNavController().navigate(R.id.action_mainFragment_to_addExpenseFragment, bundle)
    }

    override fun onDeleteClick(expense: Expense) {
        expenseList.remove(expense)
        expenseAdapter.notifyDataSetChanged()
        saveExpensesToFile(requireContext(), expenseList)
        update()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>("newExpense")
            ?.observe(viewLifecycleOwner) { bundle ->
                val updatedExpense = Expense(
                    bundle.getInt("expenseId"),
                    bundle.getString("expenseName", ""),
                    bundle.getDouble("expenseAmount", 0.00),
                    bundle.getString("expenseDate", "")
                )
                Log.d("onViewCreated", updatedExpense.id.toString())
                //Changed to id instead of name
                val index = expenseList.indexOfFirst { it.id == updatedExpense.id }
                if (index != -1) {
                    expenseList[index] = updatedExpense
                    expenseAdapter.notifyItemChanged(index)
                    update()
                } else {
                    expenseList.add(updatedExpense)
                    expenseAdapter.notifyItemInserted(expenseList.size - 1)
                    update()
                }

                saveExpensesToFile(requireContext(), expenseList)
            }
    }

    private fun saveExpensesToFile(context: Context, expenseList: List<Expense>) {
        try {
            val json = Gson().toJson(expenseList)
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { output ->
                output.write(json.toByteArray())
            }
            Log.d("FileStorage", "Expenses saved successfully")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error saving expenses: ${e.message}")
        }
    }

    private fun loadExpensesFromFile(context: Context): MutableList<Expense> {
        val expenseList: MutableList<Expense> = mutableListOf()
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (!file.exists()) return expenseList

            val json = file.readText()
            val loadedExpenses: List<Expense> = Gson().fromJson(json, object : com.google.gson.reflect.TypeToken<List<Expense>>() {}.type)
            expenseList.addAll(loadedExpenses)

            Log.d("FileStorage", "Expenses loaded successfully")
        } catch (e: FileNotFoundException) {
            Log.e("FileStorage", "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error reading file: ${e.message}")
        }
        return expenseList
    }

    override fun onStart() {
        super.onStart()
        update()
    }
}
