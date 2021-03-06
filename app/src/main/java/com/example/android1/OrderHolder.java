package com.example.android1;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.*;


public class OrderHolder extends RecyclerView.ViewHolder {
    String db_key_username = AppProperties.getInstance().username;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference orders =  database.getReference("accounts")
            .child(db_key_username).child("orders");
    DatabaseReference order_stats =  database.getReference("accounts")
            .child(db_key_username).child("order_stats");
    DatabaseReference order_web =  database.getReference("accounts")
            .child(db_key_username).child("order_web");

    View mView;
    TextView textViewOrder;
    TextView textViewTime;
    CheckBox checkBoxFinished;
    String orderId;

    public OrderHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        textViewOrder = mView.findViewById(R.id.cardView_order_item);
        textViewTime = mView.findViewById(R.id.cardView_time_of_ordering);
        checkBoxFinished = mView.findViewById(R.id.checkBox_finished);
        checkBoxFinished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    // update finish time
                    order_web.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot order : snapshot.getChildren()) {
                                if (orderId.equals(order.child("orderID").getValue())) {
                                    String postKey = order.getRef().getKey();
                                    assert postKey != null;
                                    order_web.child(postKey).child("finishTime").setValue(System.currentTimeMillis());

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    // remove from `orders` table and from cook activity
                    orders.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot order : snapshot.getChildren()) {
                                if (orderId.equals(order.child("orderID").getValue())) {
                                    order.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }


    void setOrder(Order order) {
        String food_content = get_order_item(order.getFood());
        System.out.println(order.getFood());
        Long orderTime_content = order.getOrderTime();
        String orderDate_content = order.getOrderDate();

        textViewOrder.setText(food_content);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        Date date = new Date(orderTime_content);
        String orderTime = simpleDateFormat.format(date);
        String dateTime = orderDate_content + "   " +orderTime;
        textViewTime.setText(dateTime);
        Log.i("ORDER_HOLDER", "An order is added to cook page");
    }


    void setId(String id){
        this.orderId = id;
        Log.i("ORDER_HOLDER", "ORDER ID: " + id);
    }

    String get_order_item( HashMap<String,String> order) {
        HashMap<String,String> food_return = new HashMap<>();
        for (String key : order.keySet()) {
            if (key.equals("receiptOrder")  || key.equals("totalCost")) {
                continue;
            }
            food_return.put(key, order.get(key));
        }
        return food_return.toString();
    }
}
