package Utils;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.biddle.Activites.ProductDetailsActivity;
import com.example.biddle.Activites.ProductFormActivity;
import com.example.biddle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Models.Customer;
import Models.Products;
import Models.Receipt;
import Models.Seller;

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


    // this method create new Receipt
    // refrence param == database.getReference().child("Users")
    public static void CreateReceipt(String productSellerID, String productCustomerID, DataSnapshot product, DatabaseReference reference) {

        Receipt receipt = new Receipt();

        String receiptID = UUID.randomUUID().toString(); // generate unique  id


        receipt.setSellDate(product.getValue(Products.class).getEndingDate());
        receipt.setProductName(product.getValue(Products.class).getName());
        receipt.setPrice(product.getValue(Products.class).getPrice());
        receipt.setReceiptID(receiptID);


        readSellerDetails(productSellerID,productCustomerID,reference, receipt);

    }

    // fetch seller Details from firebase

    private static void readSellerDetails(String productSellerID, String productCustomerID, DatabaseReference reference, Receipt receipt) {
        reference.child(productSellerID).child("sellerDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                            receipt.setAddress(dataSnapshot.getValue(Seller.class).getAdress());
                            receipt.setCity(dataSnapshot.getValue(Seller.class).getCity());
                            receipt.setSellerName(dataSnapshot.getValue(Seller.class).getName());
                            receipt.setSellerPhoneNumber(dataSnapshot.getValue(Seller.class).getPhoneNumber());
                            receipt.setZipCode(dataSnapshot.getValue(Seller.class).getZip());
                            readCustomerDetails(productSellerID, productCustomerID,reference, receipt);

                    }
                else {
                    Log.d("FaildReadDB","didnt find product");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // would change to toast
                Log.d("FaildReadDB",databaseError.toString());
            }
        });
    }

    private static void readCustomerDetails(String productSellerID, String productCustomerID, DatabaseReference reference, Receipt receipt) {

        reference.child(productCustomerID).child("CustomerDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                        receipt.setCustomerName(dataSnapshot.getValue(Customer.class).getName());
                        receipt.setCustomerPhoneNumber(dataSnapshot.getValue(Customer.class).getPhoneNumber());


                        // write to db - set new receipt
                        reference.getParent().child("ReceiptsDetails").child(receipt.getReceiptID()).setValue(receipt, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("faildtoWriteDB", "didnt find product");
                                } else {
                                    // write to db - set new reference to receipt on both seller anc customer with a refrence name
                                    reference.child(productCustomerID).child("Receipts").child(receipt.getReceiptID()).setValue(receipt.getProductName());
                                    reference.child(productSellerID).child("Receipts").child(receipt.getReceiptID()).setValue(receipt.getProductName());
                                }
                            }
                        });

                }
                else {
                    Log.d("FaildReadDB","didnt find product");
                    return;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // would change to toast
                Log.d("FaildReadDB",databaseError.toString());
            }
        });
    }
}
