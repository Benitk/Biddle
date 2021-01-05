package Models;

import com.example.biddle.Activites.ExpireDate;

import java.util.Date;

public class Customer {

    private String UserId;
    private int CardNumber, Cvv;
    private ExpireDate exipreDate;

    public  Customer(){}
    public Customer(String userId, int cardNumber, int cvv, ExpireDate exipreDate) {
        UserId = userId;
        CardNumber = cardNumber;
        Cvv = cvv;
        this.exipreDate = exipreDate;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(int cardNumber) {
        CardNumber = cardNumber;
    }

    public int getCvv() {
        return Cvv;
    }

    public void setCvv(int cvv) {
        Cvv = cvv;
    }

    public ExpireDate getExipreDate() {
        return exipreDate;
    }

    public void setExipreDate(ExpireDate exipreDate) {
        this.exipreDate = exipreDate;
    }
}
