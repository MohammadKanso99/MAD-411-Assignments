package com.example.assignment6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(
    private val expenseArray: MutableList<Expense>,
    private val eventListener: ExpenseListener
)
    :RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>(){

    interface ExpenseListener {

        fun onDetails(expense: Expense)

        fun onEditClick(expense: Expense)

        fun onDeleteClick(expense: Expense)

    }

        inner class ExpenseViewHolder(view: View): RecyclerView.ViewHolder(view){
            val textViewName: TextView = view.findViewById(R.id.textViewName)
            val textViewAmount: TextView = view.findViewById(R.id.textViewAmount)
            val button: Button = view.findViewById(R.id.deleteButton)
            val editButton: Button = view.findViewById(R.id.editButton)

            fun bind(expense: Expense){
                textViewName.text = expense.name
                textViewAmount.text = "${expense.amount}"

                button.setOnClickListener{
                    eventListener.onDeleteClick(expense)

                }

                editButton.setOnClickListener{
                    eventListener.onEditClick(expense)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_layout_list_item, parent, false)

        return ExpenseViewHolder(view)    }

    override fun getItemCount() = expenseArray.size

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenseArray[position]
        holder.bind(expense)
        holder.itemView.setOnClickListener{
            eventListener.onDetails(expense)
        }

    }
}