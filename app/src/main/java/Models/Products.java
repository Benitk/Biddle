package Models;

import android.util.Log;

import java.util.Date;

public class Products {
    private String id;
    private String name;
    private double price;
    private String category;
    private String description;
    private Date endingDate;
    private String imgPath;

    public Products(){
    }

    public Products(String id ,String name, double price, String category, Date endingdate, String description, String imgPath){
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.endingDate = endingdate;
        this.imgPath = imgPath;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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
