package com.fanyichen.focusbalance.countdown.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.fanyichen.focusbalance.R
import com.fanyichen.focusbalance.countdown.viewmodel.CountDownViewModel
import com.fanyichen.focusbalance.databinding.ActivityCountDownBinding
import com.fanyichen.focusbalance.mdc_ui.util.getSurface1
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CountDownActivity : AppCompatActivity() {
    private val viewModel: CountDownViewModel = CountDownViewModel(this)
    private lateinit var binding: ActivityCountDownBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayoutCompat>
    private lateinit var todoListAdapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountDownBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initStyle()
        initView()
    }

    private fun initStyle() {
        val surface1Color = getSurface1(context = this)
        binding.countdownToolbar.setBackgroundColor(surface1Color)
        binding.background.setBackgroundColor(surface1Color)
        window.statusBarColor = surface1Color
        binding.todoCard.setCardBackgroundColor(surface1Color)
    }

    private fun initView() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        todoListAdapter = TodoListAdapter()
        binding.todolist.adapter = todoListAdapter
        todoListAdapter.setOnLongClickListener {
            true
        }
        binding.addTodo.setOnClickListener { showAddTodoDialog() }
    }

    private fun setUpObservation() {
        viewModel.viewState.observe(this, Observer { state ->
            when (state) {
                is CountDownViewModel.ViewState.CountingDown -> {

                }
            }
        })
    }

    private fun showAddTodoDialog() {

    }

    private class TodoListAdapter : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {
        private val todoListDataSet: MutableList<String> = mutableListOf()
        private lateinit var onLongClickItemListener: View.OnLongClickListener

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_todolist, parent, false)
            view.setOnLongClickListener(onLongClickItemListener)
            return ViewHolder(view)
        }

        private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView
            val divider: View

            init {
                textView = view.findViewById(R.id.title)
                divider = view.findViewById(R.id.divider)
            }
        }

        fun setData(list: List<String>) {
            if (list.isEmpty()) {
                return
            }
            todoListDataSet.clear()
            todoListDataSet.addAll(list)
            notifyDataSetChanged()
        }

        fun setOnLongClickListener(listener: View.OnLongClickListener) {
            onLongClickItemListener = listener
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = todoListDataSet[position]
            if (position == todoListDataSet.size - 1) {
                holder.divider.visibility = View.GONE
            }
        }

        override fun getItemCount(): Int {
            return todoListDataSet.size
        }

    }
}