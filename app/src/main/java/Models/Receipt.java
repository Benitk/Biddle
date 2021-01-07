package Models;

import java.util.Date;

public class Receipt {

    private int price;
    private int sellerPhoneNumber;
    private int customerPhoneNumber;
    private int zipCode;
    private String productName;
    private String sellerName;
    private String customerName;
    private Date sellDate;
    private String address;
    private String city;
    private String receiptID;

    public Receipt() {
    }

    public Receipt(String receiptID , int price, int sellerPhoneNumber, int customerPhoneNumber, int zipCode, String productName, String sellerName, String customerName, Date sellDate, String address, String city) {
        this.price = price;
        this.sellerPhoneNumber = sellerPhoneNumber;
        this.customerPhoneNumber = customerPhoneNumber;
        this.zipCode = zipCode;
        this.productName = productName;
        this.sellerName = sellerName;
        this.customerName = customerName;
        this.sellDate = sellDate;
        this.address = address;
        this.city = city;
        this.receiptID = receiptID;
    }

    public String getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSellerPhoneNumber() {
        return sellerPhoneNumber;
    }

    public void setSellerPhoneNumber(int sellerPhoneNumber) {
        this.sellerPhoneNumber = sellerPhoneNumber;
    }

    public int getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(int customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
