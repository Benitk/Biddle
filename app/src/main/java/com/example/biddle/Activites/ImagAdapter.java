package com.example.biddle.Activites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.biddle.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagAdapter extends RecyclerView.Adapter<ImagAdapter.ImagViewHolder> {
     private Context context;
     private List<UplouadImg> Uploads;

     public  ImagAdapter(Context con, List<UplouadImg> uploadsList){
         context =con;
         Uploads =uploadsList;
     }

    @NonNull
    @Override
    public ImagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_product_details, parent ,false);
    return new ImagViewHolder(v);
     }

    @Override
    public void onBindViewHolder(@NonNull ImagViewHolder holder, int position) {
UplouadImg current =  Uploads.get(position) ;
holder.textView.setText(current.getName());
        Picasso.with(context)
                .load(current.getImagUrl() )
                .fit()
                .centerCrop()
                .into(holder.imagView);
    }

    @Override
    public int getItemCount() {
        return Uploads.size();
    }

    public class ImagViewHolder extends  RecyclerView.ViewHolder{
         public ImageView imagView;
         public TextView textView;

        public  ImagViewHolder(View itemView){
            super(itemView);
            imagView = itemView.findViewById(R.id.productpic);
            textView =  itemView.findViewById(R.id.productName);

        }
    }
}
