package com.group35.nutripath.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

object DialogHelper {

    fun showSetBudgetDialog(context: Context, onBudgetEntered: (Float) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Set Monthly Budget")
        val input = EditText(context)
        input.hint = "Enter your budget"
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            val budget = input.text.toString().toFloatOrNull()
            if (budget != null) onBudgetEntered(budget)
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    //new e
    fun showAddExpenseDialog(context: Context, onExpenseEntered: (Float) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Expense")
        val input = EditText(context)
        input.hint = "Enter expense amount"
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            val expense = input.text.toString().toFloatOrNull()
            if (expense != null) onExpenseEntered(expense)
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }
}
