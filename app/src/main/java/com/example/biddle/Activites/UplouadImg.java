package com.example.biddle.Activites;

public class UplouadImg {
    private  String Name;
    private  String ImagUrl;

    UplouadImg(){
    }
    UplouadImg(String name,String url){
  if (name.trim().equals("")){
      name ="no name";
  }
   Name = name;
   ImagUrl =url;
    }
    //getter & setter
    public void SetName (String n){
        Name = n;
    }
    public void SetUrl (String url){
        ImagUrl =url;
    }

public String getName(){
        return  Name;
}

public String getImagUrl(){
        return  ImagUrl;
    }


}
