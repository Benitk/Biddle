package Utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class DBmethods {

    // delete product from db when product ending time already past

    public static void DeleteProduct(String productId, String productCategory, String productSellerID, DatabaseReference reference) {

        Map<String, Object> childUpdates = new HashMap<>();

        addChildrenFromFavorties(productId,productCategory,childUpdates, productSellerID, reference);
    }


    private static void addChildrenFromFavorties(String productID, String productCategory, Map<String, Object> childUpdates, String productSellerID, DatabaseReference reference){
        // retrive all favorite products in each user that equal ProductID
        reference.child("Users").orderByChild("favoriteProducts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        if(ds.getKey().equals("favoriteProducts")) {
                            for (DataSnapshot favorites : ds.getChildren()) {
                                if(favorites.getKey().equals(productID)) {
                                    // sub string from index 35 because is prefix equal to getReference()
                                    childUpdates.put(favorites.getRef().toString().substring(35), null);
                                }
                            }
                        }
                    }
                    addChildrenFromOnBid(productID, productCategory, childUpdates, productSellerID, reference);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FaildReadDB",databaseError.toString());
            }
        });
    }

    private static void addChildrenFromOnBid(String productID, String productCategory, Map<String, Object> childUpdates, String productSellerID, DatabaseReference reference){
        // retrive all favorite products in each user that equal ProductID
        reference.child("Users").orderByChild("ProductOnBid").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        if(ds.getKey().equals("ProductOnBid")) {
                            for (DataSnapshot favorites : ds.getChildren()) {
                                if(favorites.getKey().equals(productID)) {
                                    // sub string from index 35 because is prefix equal to getReference()
                                    childUpdates.put(favorites.getRef().toString().substring(35), null);
                                }
                            }
                        }
                    }
                    DeleteProduct(childUpdates, productID, productCategory, productSellerID, reference);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FaildReadDB",databaseError.toString());
            }
        });
    }


    // this method called delete product from all relevant nodes
    private static void DeleteProduct(Map<String, Object> childUpdates, String productID, String productCategory, String productSellerID, DatabaseReference reference){

        childUpdates.put("/Products/" + productID, null);
        childUpdates.put("/Categories/" + productCategory + "/" + productID, null);
        childUpdates.put("/Users/" + productSellerID + "/sellerProducts/" + productID, null);



        reference.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                childUpdates.clear(); // prevent array to cost alot of memory
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("deleteproduct", "onFailure: " + e.toString());
                childUpdates.clear(); //prevent array to cost alot of memory
            }
        });
    }


}
