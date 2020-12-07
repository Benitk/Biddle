package Models;

public class Cards {

    private String productId;
    private String productName;
    private String currentPrice;
    private String endingDate;

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
}
