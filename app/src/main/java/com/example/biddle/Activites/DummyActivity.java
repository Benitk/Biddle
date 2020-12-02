package com.example.biddle.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.biddle.R;

public class DummyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
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
                return true;
            case R.id.starProducts:
                return true;
            case R.id.priceOfferedProducts:
                return true;
            case R.id.purchasedProducts:
                return true;
            case R.id.waitForPickUpProducts:
                return true;
            case R.id.editProfile:
                return true;
            case R.id.logOut:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}