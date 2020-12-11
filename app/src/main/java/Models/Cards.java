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

    public Cards(String productName, String currentPrice, String endingDate, String id) {
        this.productName = productName;
        this.currentPrice = currentPrice;
        this.endingDate = endingDate;
        this.productId = id;
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

    /*public static Comparator<Cards> compareByPrice = new Comparator<Cards>() {
            @Override
            public int compare(Cards c1, Cards c2) {
                return c1.currentPrice.compareTo(c2.currentPrice);
            }
        };

    public static Comparator<Cards> compareByDate = new Comparator<Cards>() {
            @Override
            public int compare(Cards c1, Cards c2) {
                return c1.dateType.compareTo(c2.dateType);
            }
        };

    public static ArrayList<Cards> sort(ArrayList<Cards> cards_list, boolean sortByPrice) {
        if(sortByPrice) Collections.sort(cards_list, compareByPrice);
        else Collections.sort(cards_list, compareByDate);
        return cards_list;
    }*/

}
