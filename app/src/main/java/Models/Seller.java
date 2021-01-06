package Models;

public class Seller {
        private String UserId ,City,Adress, Bank, Branch , Name;
        private int zip, BankAcountNumber, PhoneNumber , personalID;

        public Seller(){
        }


    public Seller(String userId, String city, String adress, String bank, String branch, int zipn, int bankAcountNumber, String name , int phoneNumber , int personaliD) {
        UserId = userId;
        City = city;
        Adress = adress;
        Bank = bank;
        Branch = branch;
        zip = zipn;
        BankAcountNumber = bankAcountNumber;
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
