package Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biddle.Activites.ProductDetailsActivity;
import com.example.biddle.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import Models.Cards;
import Models.Receipt;

public class ReciptsAdapter extends RecyclerView.Adapter<ReciptsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private Context currentActivity;
    private List<Receipt> data;
    private String user_type;
    private FirebaseDatabase database;

    public ReciptsAdapter(Context context, List<Receipt> data, String type) {
        this.layoutInflater = LayoutInflater.from(context);
        this.currentActivity = context;
        this.data = data;
        this.user_type = type;
        this.database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.receipt_layout, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // bind the textview with data received

        Receipt r = data.get(i);
        String product_name = r.getProductName();
        // String product_endingDate = c.getEndingDate();
        String receiptID = r.getReceiptID();


        viewHolder.getProductName_tv().setText(product_name);
        // viewHolder.getProduct_endingDate().setText(product_endingDate);
        viewHolder.getReceiptID_tv().setText(receiptID);


        viewHolder.getmyView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (!database.getReference().child("Users").child(data.get(i).getCurCostumerID().toString()).child("CostumerDetails").child("cvv").toString().equals(null)) {
                Intent intent = new Intent(currentActivity, ProductDetailsActivity.class);
                intent.putExtra("user_type", user_type);
                intent.putExtra("receiptID", receiptID);
                currentActivity.startActivity(intent);
                Log.d("check", user_type);
                Log.d("check", receiptID);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView productName_tv, ReceiptID_tv;

        private View myView;

        public View getmyView() {
            return myView;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myView = itemView;

            productName_tv = itemView.findViewById(R.id.product_name);
            ReceiptID_tv = itemView.findViewById(R.id.reciptID);
        }

        public TextView getProductName_tv() {
            return productName_tv;
        }

        public TextView getReceiptID_tv() {
            return ReceiptID_tv;
        }
    }

}