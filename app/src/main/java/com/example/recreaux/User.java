package com.example.recreaux;

public class User {

    public String Name,Username,email,UserImage,Residence,Interests,Bio,PhoneNumber;
    public String Name,Username,email,IconID,Residence,Interests,Bio,PhoneNumber, UserQR;

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

    /*public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIconID() {
        return IconID;
    }

    public void setIconID(String iconID) {
        IconID = iconID;
    }

    public String getResidence() {
        return Residence;
    }

    public void setResidence(String residence) {
        Residence = residence;
    }

    public String getInterests() {
        return Interests;
    }

    public void setInterests(String interests) {
        Interests = interests;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }*/
}
