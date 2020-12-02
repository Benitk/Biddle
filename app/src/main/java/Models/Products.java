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



    public Products(){
    }

    public Products(String id ,String name, double price, String category, String endingdate, String description){
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        String[] date = endingdate.split("/");
        if(date.length > 3)
            Log.d("dateLength", "date format invalid");

        // format is year/month/day
        this.endingDate = new Date(Integer.parseInt(date[0]),Integer.parseInt(date[1]), Integer.parseInt(date[2]));
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
