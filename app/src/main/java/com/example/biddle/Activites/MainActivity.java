package com.example.biddle.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biddle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Utils.InputValidator;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL1 = "higherPrice";
    public static final String CHANNEL2 = "productSoldToYou";
    public static final String CHANNEL3 = "yourProductSold";

    private EditText et_email;
    private EditText et_password;
    private TextView tv_login_btn;
    private TextView tv_signup_btn;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            createChannels();
        }

        progressb = (ProgressBar)findViewById(R.id.progressBar);
        tv_signup_btn = (TextView)findViewById(R.id.signup_btn);
        tv_login_btn = (TextView)findViewById(R.id.login_btn);

        progressb.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        // if already logged in
        if(firebaseAuth.getCurrentUser() != null)
            startActivity(new Intent(MainActivity.this, LandingPageActivity.class));

        /* login button listener - on click getting user input
         * call validate function to confirm valid input
         * check if exist in firebase
         */

        tv_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_email = (EditText)findViewById(R.id.email);
                et_password = (EditText)findViewById(R.id.password);
                String user_email = et_email.getText().toString().trim();
                String user_password = et_password.getText().toString().trim();

                InputValidator validator = new InputValidator();
                boolean flag = true;

                if(!validator.isValidEmail(user_email)) {
                    et_email.setError("מייל בפורמט לא תקין.");
                    flag = false;
                }

                if(!validator.isValidPassword(user_password)) {
                    et_password.setError("סיסמא שגויה, נסה שנית");
                    flag = false;
                }

                if(flag)
                    LoginDB(user_email, user_password);
            }
        });

        /* signup button listener - on click return to login page
         */

        tv_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });
    }

    private void LoginDB(String user_email, String user_password){
        progressb.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Toast.makeText(MainActivity.this, "הכניסה הושלמה.", Toast.LENGTH_SHORT).show();
                    progressb.setVisibility(View.GONE);

                    startActivity(new Intent(MainActivity.this, LandingPageActivity.class));

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(MainActivity.this, "הכניסה נכשלה.", Toast.LENGTH_SHORT).show();
                    progressb.setVisibility(View.GONE);
                }
            }
        });
    }

    // Notifications

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createChannels() {
        NotificationManager mNotificationManager = getSystemService(NotificationManager.class);

        String id1 = CHANNEL1;
        String id2 = CHANNEL2;
        String id3 = CHANNEL3;

        CharSequence name1 = getString(R.string.higherPriceChanel);
        CharSequence name2 = getString(R.string.productSoldToYouChanel);
        CharSequence name3 = getString(R.string.yourProductChanel);

//        String description1 = getString(R.string.higherPriceChanel);
//        String description2 = getString(R.string.productSoldToYouChanel);
//        String description3 = getString(R.string.yourProductChanel);

        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel1, mChannel2, mChannel3 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel1 = new NotificationChannel(id1, name1, importance);
//            mChannel1.setDescription(description1);
            mNotificationManager.createNotificationChannel(mChannel1);

            mChannel2 = new NotificationChannel(id2, name2, importance);
//            mChannel2.setDescription(description2);
            mNotificationManager.createNotificationChannel(mChannel2);

            mChannel3 = new NotificationChannel(id3, name3, importance);
//            mChannel3.setDescription(description3);
            mNotificationManager.createNotificationChannel(mChannel3);
        }
    }

    public void addNotifications(String channel) {
        Notification.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationBuilder = new Notification.Builder(this, channel);
        else  //noinspection deprecation
            notificationBuilder = new Notification.Builder(this);

        Intent landingIntent = cIntent(channel);
        PendingIntent pendingLandingIntent = PendingIntent.getActivity(this, 0, landingIntent,0);

        String title = cTitle(channel);
        String text = cText(channel);

        Notification notification = notificationBuilder
                .setContentTitle(title)
                .setSmallIcon(R.drawable.auction_icon)
                .setContentText(text)
                .setContentIntent(pendingLandingIntent)
                .setAutoCancel(true).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

    private String cTitle(String channel) {
        if(channel == CHANNEL1)
            return getString(R.string.higherPriceTitle);
        else if(channel == CHANNEL2)
            return getString(R.string.productSoldToYouTitle);
        else  // CHANNEL3
            return getString(R.string.yourProductTitle);
    }

    private String cText(String channel) {
        if(channel == CHANNEL1)
            return getString(R.string.higherPriceText);
        else if(channel == CHANNEL2)
            return getString(R.string.productSoldToYouText);
        else  // CHANNEL3
            return getString(R.string.yourProductText);
    }

    private Intent cIntent(String channel) {
        if(channel == CHANNEL1)
            return new Intent(this, PriceOfferedProductsCustomerActivity.class);
        else if(channel == CHANNEL2)
            return new Intent(this, PurchasedProductsCustomerActivity.class);
        else  // CHANNEL3
            return new Intent(this, PurchasedProductsSellerActivity.class);
    }

    // add here remove notification function
}