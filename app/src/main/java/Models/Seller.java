package Models;

import java.util.Date;

public class Seller {
        private String UserId ,City,Adress, Bank, Branch;
        private int zip, BankAcountNumber;

        public Seller(){
        }


    public Seller(String userId, String city, String adress, String bank, String branch, int zipn, int bankAcountNumber) {
        UserId = userId;
        City = city;
        Adress = adress;
        Bank = bank;
        Branch = branch;
        zip = zipn;
        BankAcountNumber = bankAcountNumber;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public String getBank() {
        return Bank;
    }

    public void setBank(String bank) {
        Bank = bank;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public int getBankAcountNumber() {
        return BankAcountNumber;
    }

    public void setBankAcountNumber(int bankAcountNumber) {
        BankAcountNumber = bankAcountNumber;
    }
}
