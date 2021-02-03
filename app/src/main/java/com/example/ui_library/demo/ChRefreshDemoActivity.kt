package com.example.ui_library.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ui_library.R
import org.chen.chui.refresh.ChRefresh
import org.chen.chui.refresh.ChRefreshLayout
import org.chen.chui.refresh.ChTextOverView
import org.chen.cibrary.log.ChLog

class ChRefreshDemoActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh_demo)
        val refreshLayout = findViewById<ChRefreshLayout>(R.id.refresh_layout)
        val xOverView = ChTextOverView(this)
        //val lottieOverView = HiLottieOverView(this)
        refreshLayout.setRefreshOverView(xOverView)
        refreshLayout.setRefreshListener(object :
                ChRefresh.ChRefreshListener {
            override fun onRefresh() {
                Handler().postDelayed({ refreshLayout.refreshFinished() }, 1000)
            }

            override fun enableRefresh(): Boolean {
                return true
            }
        })
        refreshLayout.setDisableRefreshScroll(false)
        initRecycleView()
    }

    var myDataset = arrayOf("Refresh", "Refresh", "Refresh", "Refresh", "Refresh", "Refresh", "Refresh")

    private fun initRecycleView() {
        recyclerView = findViewById<View>(R.id.recycleview) as RecyclerView
        // use this setting to improve performance if you know that changes
// in content do not change the layout size of the RecyclerView
        recyclerView!!.setHasFixedSize(true)
        // use a linear layout manager
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(layoutManager)
        // specify an adapter (see also next example)
        val mAdapter =
                MyAdapter(
                        myDataset
                )
        recyclerView!!.setAdapter(mAdapter)
    }

    class MyAdapter // Provide a suitable constructor (depends on the kind of dataset)
    (private val mDataset: Array<String>) :
            RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
        class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            // each data item is just a string in this case
            var textView: TextView

            init {
                textView = v.findViewById(R.id.tv_title)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
        ): MyViewHolder { // create a new view
            val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_layout, parent, false)
            return MyViewHolder(
                    v
            )
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(
                holder: MyViewHolder,
                position: Int
        ) { // - get element from your dataset at this position
// - replace the contents of the view with that element
            holder.textView.text = mDataset[position]
            holder.itemView.setOnClickListener { ChLog.d("position:$position") }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount(): Int {
            return mDataset.size
        }

    }
}
