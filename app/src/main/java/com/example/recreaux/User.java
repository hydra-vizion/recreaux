package com.example.recreaux;

public class User {

    public String Name,Username,email,UserImage,Residence,Interests,Bio,PhoneNumber, UserQR;


    public User(){
    }

    public User(String Name,String Username, String Email, String UserQR){
        this.Name = Name;
        this.Username = Username;
        this.email = Email;
        this.UserQR = UserQR;
    }

    public User(String Name,String Username, String Email,String UserImage,String Residence,String Interests,String Bio,String PhoneNumber){
        this.Name = Name;
        this.Username = Username;
        this.email = Email;
        this.UserImage = UserImage;
        this.Residence = Residence;
        this.Interests = Interests;
        this.Bio = Bio;
        this.PhoneNumber = PhoneNumber;
    }

}
