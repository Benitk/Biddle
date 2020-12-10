package Models;

import android.util.Log;

import java.util.Date;

public class Products {
    private String id;
    private String customerID;
    private String sellerID;
    private String name;
    private int price;
    private String category;
    private String description;
    private Date endingDate;
    private String imgPath;

    public Products(){
    }


    public Products(String id , String sellerID,String customerID, String name, int price, String category, Date endingdate, String description, String imgPath){
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.endingDate = endingdate;
        this.imgPath = imgPath;
        this.sellerID = sellerID;
        this.customerID = customerID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }
}
