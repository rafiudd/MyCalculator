package com.example.mycalculator

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.mycalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var expression: Expression

    var lastNumeric = false
    var stateError = false
    var lastDot = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClear(view: View) {
        binding.dataTv.text = ""
        binding.resultTv.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.resultTv.visibility = View.GONE
    }

    fun onDelete(view: View) {
        binding.dataTv.text = binding.dataTv.text.dropLast(1)
        try {
            var lastchar = binding.dataTv.toString().last()
            if(lastchar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {
            binding.resultTv.visibility = View.GONE
            binding.resultTv.text = ""
            Log.e("ERROR", e.toString())
        }
    }


    fun onDigitClicked(view: View) {
        if(stateError) {
            binding.dataTv.text = (view as Button).text
            stateError = false
        } else {
            binding.dataTv.append((view as Button).text)
        }

        lastNumeric = true
        onEqual()
    }

    fun onEqual() {
        if(lastNumeric && !stateError) {
            val txt = binding.dataTv.text.toString()
            expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                binding.resultTv.visibility = View.VISIBLE
                binding.resultTv.text = result.toString()

            } catch (ex: ArithmeticException) {
                Log.e("evaluate error", ex.toString())
                binding.resultTv.text = "ERROR"
                stateError = true
                lastNumeric = false
            }
        }
    }

    fun onEqualClick(view: View) {
        onEqual()
        binding.dataTv.text = binding.resultTv.text.toString()
    }

    fun onOperatorClick(view: View) {
        if(!stateError && lastNumeric) {
            binding.dataTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }
}