package Models;

public class Customer {

    private String UserId ,Name;
    private int CardNumber, Cvv , PhoneNumber , personalID;
    private ExpireDate exipreDate;

    public  Customer(){}
    public Customer(String userId, int cardNumber, int cvv, ExpireDate exipreDate, String name , int phoneNumber , int personaliD) {
        UserId = userId;
        CardNumber = cardNumber;
        Cvv = cvv;
        this.exipreDate = exipreDate;
        Name= name;
        PhoneNumber = phoneNumber;
        personalID = personaliD;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public int getPersonalID() {
        return personalID;
    }

    public void setPersonalID(int personaliD) {  personalID = personaliD; }

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
