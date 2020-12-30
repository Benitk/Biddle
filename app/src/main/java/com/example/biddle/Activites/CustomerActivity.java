package com.example.biddle.Activites;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biddle.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import Models.Cards;
import Models.Products;
import Utils.CardsAdapter;
import Utils.AlgoLibrary;


public class CustomerActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private ArrayList<Cards> cards;
    private CardsAdapter cardsAdapter;
    private TextView tv_noProductText;
    private String userId;
    private ProgressBar progressb;
    private TextView sort_tv;
    private ImageView cancelSort;

    private boolean sortByPrice = false;
    private boolean sortByDate = false;
    private String selected_category = "";

    private DatabaseReference refProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refProducts = database.getReference().child("Products");
        userId = firebaseAuth.getCurrentUser().getUid();
        cards = new ArrayList<Cards>();
        progressb = (ProgressBar)findViewById(R.id.progressBar);
        progressb.setVisibility(View.GONE);
        tv_noProductText = (TextView) findViewById(R.id.noProducts);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        sort_tv = (TextView) findViewById(R.id.sort_tv);

        cancelSort = (ImageView) findViewById(R.id.cancel_sort);
        cancelSort.setVisibility(View.GONE);

        sort_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CustomerActivity.this);
                builder.setTitle(R.string.pick_sort);
                final String[] options = new String[]{"לפי זמן עד סיום המכירה", "לפי מחיר", "לפי קטגוריה"};
                builder.setSingleChoiceItems(options, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String selectedItem = Arrays.asList(options).get(i);
                                if(i == 0) sortByDate = true;
                                else if(i == 1) sortByPrice = true;
                                else categorySort();  // i == 2, sort by category
                            }
                        });

                builder.setPositiveButton(R.string.accpet, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sort_cards();
                        sortByPrice = sortByDate = false;  // reset
                        selected_category = "";
                       // initRecyclerAdapter();
                        cardsAdapter.notifyDataSetChanged();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        cancelSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());  // refresh activity
            }
        });

        initRecyclerAdapter();
        ReadFromDB();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void categorySort() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(CustomerActivity.this);
        builder.setTitle(R.string.category_sort);
        final String[] options = getResources().getStringArray(R.array.CategoriesArray);
        builder.setSingleChoiceItems(options, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selected_category = Arrays.asList(options).get(i);
                    }
                });
        builder.setPositiveButton(R.string.accpet, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sort_cards() {
        cancelSort.setVisibility(View.VISIBLE);  // user can press it if regrets (don't want to sort)
        Toast.makeText(CustomerActivity.this, R.string.sorting, Toast.LENGTH_SHORT).show();
        if(sortByPrice) {
            Collections.sort(cards, new Comparator<Cards>() {
                @Override
                public int compare(Cards c1, Cards c2) {
                    return Integer.parseInt(c1.getCurrentPrice()) - Integer.parseInt(c2.getCurrentPrice());
                }
            });
        } else if (sortByDate) {
            Collections.sort(cards, new Comparator<Cards>() {
                @Override
                public int compare(Cards c1, Cards c2) {
                    return c1.getDateType().compareTo(c2.getDateType());
                }
            });
        } else {  // sortByCategory
            cards.removeIf(c -> (!c.getProductCategory().equals(selected_category)));
        }
    }


    // read all product from
    private void ReadFromDB(){
        progressb.setVisibility(View.VISIBLE);

        refProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cards.clear();
                tv_noProductText.setText("");
                if (dataSnapshot.exists())
                    addNewCard(dataSnapshot);
                else {
                    tv_noProductText.setText(R.string.noProduct);
                    progressb.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // would change to toast
                Log.d("FaildReadDB",databaseError.toString());
                progressb.setVisibility(View.GONE);
            }
        });
    }

    // create new card from given snapshot
    private void addNewCard(DataSnapshot ds){
        Cards card = null;
        for(DataSnapshot product : ds.getChildren()){

            // user cant bid on his own product
            if(!userId.equals(product.getValue(Products.class).getSellerID())) {
                card = new Cards();

                card.setProductName(product.getValue(Products.class).getName());
                card.setCurrentPrice(Integer.toString(product.getValue(Products.class).getPrice()));
                card.setEndingDate(AlgoLibrary.DateFormating(product.getValue(Products.class).getEndingDate()));
                card.setProductId(product.getValue(Products.class).getId());
                card.setProductCategory(product.getValue(Products.class).getCategory());
                card.setDateType(product.getValue(Products.class).getEndingDate());

                cards.add(card);
            }
        }

        if(cards.isEmpty())
            tv_noProductText.setText(R.string.noProduct);

        cardsAdapter.notifyDataSetChanged();
        progressb.setVisibility(View.GONE);
    }

    // print the cards arrays on the current activity recyclerView
    private void initRecyclerAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String user_type = getIntent().getStringExtra("user_type");
        cardsAdapter = new CardsAdapter(this,cards, user_type);
        recyclerView.setAdapter(cardsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.customer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homePage:
                finish();
                return true;
            case R.id.starProducts:
                startActivity(new Intent(CustomerActivity.this, StarProductsCustomerActivity.class));
                return true;
            case R.id.priceOfferedProducts:
                startActivity(new Intent(CustomerActivity.this, PriceOfferedProductsCustomerActivity.class));
                return true;
            case R.id.purchasedProducts:
                startActivity(new Intent(CustomerActivity.this, PurchasedProductsCustomerActivity.class));
                return true;
            case R.id.editProfile:
                return true;
            case R.id.logOut:
                firebaseAuth.signOut();
                startActivity(new Intent(CustomerActivity.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}