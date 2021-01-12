package Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biddle.Activites.ProductDetailsActivity;
import com.example.biddle.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import Models.Cards;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context currentActivity;
    private List<Cards> data;
    private String user_type;
    private FirebaseDatabase database;

    public CardsAdapter(Context context, List<Cards> data, String type){
        this.layoutInflater = LayoutInflater.from(context);
        this.currentActivity = context;
        this.data = data;
        this.user_type = type;
        this.database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.card2_layout,viewGroup,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // bind the textview with data received

        Cards c = data.get(i);
        String product_name = c.getProductName();
       // String product_endingDate = c.getEndingDate();
        String productPrice = c.getCurrentPrice();
        String productid = c.getProductId();
        String ImgPath = c.getImgPath();


        viewHolder.getProductName().setText(product_name);
        viewHolder.getProductPrice().setText("₪ "+productPrice);

        UploadPic(ImgPath, viewHolder.getProductImg());




        viewHolder.getmyView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(currentActivity, ProductDetailsActivity.class);
                    intent.putExtra("user_type", user_type);
                    intent.putExtra("productId", productid);
                    currentActivity.startActivity(intent);

            }
        });


        // similarly you can set new image for each card and descriptions
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView productPrice,  productName;

        private ImageView productImg;

        private View myView;

        public View getmyView() {return myView;}

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myView = itemView;

            productName = itemView.findViewById(R.id.card_name);
            productImg = itemView.findViewById(R.id.card_image);
            productPrice = itemView.findViewById(R.id.card_currentPrice);
        }
        public TextView getProductPrice() { return productPrice; }
        public TextView getProductName() { return productName;}

        public ImageView getProductImg() {return productImg; }
    }

    public void  UploadPic(String imagPath, ImageView imView){
         if (imagPath.isEmpty()){}
         else{
        final StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference(imagPath);
        final long FIVE_MEGABYTE = 1024 * 1024 * 5;

        mImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                mImageRef.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        DisplayMetrics dm = new DisplayMetrics();
                        ((Activity)currentActivity).getWindowManager().getDefaultDisplay().getMetrics(dm);
                        imView.setMinimumHeight(dm.heightPixels);
                        imView.setMinimumWidth(dm.widthPixels);
                        imView.setImageBitmap(bm);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors

                        Toast.makeText( ((Activity)currentActivity), "Error with image" , Toast.LENGTH_LONG).show();

                    }
                });

            }
        });

    }
}}