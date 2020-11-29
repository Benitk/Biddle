package com.example.biddle.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.biddle.R;
import com.google.firebase.auth.FirebaseAuth;

public class SellerActivity extends AppCompatActivity {

    private Button menu_seller_btn;
    private Button product_publish_btn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        firebaseAuth = FirebaseAuth.getInstance();

        menu_seller_btn = (Button) findViewById(R.id.menu_seller_btn);
        product_publish_btn = (Button) findViewById(R.id.product_publication_btn);


        product_publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to publish product activity
            }
        });

        menu_seller_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //want to open the menu from the left side
            }
        });
        class MyAdapter extends BaseAdapter {

            // override other abstract methods here

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup container) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
                }

                ((TextView) convertView.findViewById(android.R.id.text1))
                        .setText(getItem(position));
                return convertView;
            }
        }



    }
}