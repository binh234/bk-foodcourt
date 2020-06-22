package com.example.bk_foodcourt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OrderListFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var orderList: List<Row>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View =
            inflater.inflate(R.layout.fragment_order_list, container, false)
        recyclerView = view.findViewById(R.id.rvOrderList)

        recyclerView!!.setHasFixedSize(true)


        recyclerView!!.setLayoutManager(LinearLayoutManager(context))
        recyclerView!!.setAdapter(ItemAdapter(initData()))
        return view
    }

    private fun initData(): List<Row> {
        var oL = ArrayList<Row>()
        oL.add(Row("1", "181"))
        oL.add(Row("2", "191"))
        oL.add(Row("3", "201"))
        return oL
    }
}