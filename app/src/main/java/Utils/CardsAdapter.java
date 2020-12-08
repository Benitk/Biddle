package Utils;

import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biddle.Activites.CustomerActivity;
import com.example.biddle.Activites.LandingPageActivity;
import com.example.biddle.Activites.ProductDetailsActivity;
import com.example.biddle.R;

import java.util.List;

import Models.Cards;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context currentActivity;
    private List<Cards> data;
    private String user_type;


    public CardsAdapter(Context context, List<Cards> data, String type){
        this.layoutInflater = LayoutInflater.from(context);
        this.currentActivity = context;
        this.data = data;
        this.user_type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.card_layout,viewGroup,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // bind the textview with data received

        Cards c = data.get(i);
        String product_name = c.getProductName();
        String product_endingDate = c.getEndingDate();
        String productPrice = c.getCurrentPrice();
        String productid = c.getProductId();


        viewHolder.getProductName().setText(product_name);
        viewHolder.getProduct_endingDate().setText(product_endingDate);
        viewHolder.getProductPrice().setText(productPrice);

        viewHolder.getmyView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(currentActivity, ProductDetailsActivity.class);
                intent.putExtra("user_type", user_type);
                intent.putExtra("productId", productid);
                currentActivity.startActivity(intent);
                Log.d("check",user_type);
                Log.d("check",productid);
            }
            });


        // similarly you can set new image for each card and descriptions
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView productPrice,product_endingDate, productName;
        private View myView;

        public View getmyView() {return myView;}

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myView = itemView;

            productName = itemView.findViewById(R.id.card_name);
            //textTitle = itemView.findViewById(R.id.card_image);
            product_endingDate = itemView.findViewById(R.id.card_endingDate);
            productPrice = itemView.findViewById(R.id.card_currentPrice);
        }
        public TextView getProductPrice() { return productPrice; }
        public TextView getProduct_endingDate() { return product_endingDate; }
        public TextView getProductName() { return productName;}
    }
}