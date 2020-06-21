package com.example.bkfoodcourt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment {

    public OrderListFragment() {

    }

    RecyclerView recyclerView;
    List<Row> orderList;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        recyclerView = view.findViewById(R.id.rvOrderList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(new ItemAdapter(initData()));
        return view;
    }

    private List<Row> initData() {
        orderList = new ArrayList<>();
        orderList.add(new Row("1", "181"));
        orderList.add(new Row("2", "191"));
        orderList.add(new Row("3", "201"));
        return orderList;
    }
}