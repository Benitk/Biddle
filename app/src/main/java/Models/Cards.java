package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Cards {

    private String productId;
    private String productName;
    private String currentPrice;
    private String endingDate;
    private Date dateType;
    private String productCategory;

    public Cards(String productName, String currentPrice, String endingDate, String id, Date date, String category) {
        this.productName = productName;
        this.currentPrice = currentPrice;
        this.endingDate = endingDate;
        this.productId = id;
        this.dateType = date;
        this.productCategory = category;
    }

    public Cards(){}

    public String getProductId() {return productId; }

    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public Date getDateType() {
        return dateType;
    }

    public void setDateType(Date dateType) {
        this.dateType = dateType;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}