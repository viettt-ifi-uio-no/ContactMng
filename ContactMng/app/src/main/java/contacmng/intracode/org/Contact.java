package contacmng.intracode.org;

import android.net.Uri;

/**
 * Created by trantn1010 on 12/05/16.
 */
public class Contact {

    private String name, phone, email, address;
    private Uri imgURI;
    private int id;

    public Contact(int id, String name, String phone, String email, String address, Uri imgURI){
        this.name    = name;
        this.phone   = phone;
        this.email   = email;
        this.address = address;
        this.imgURI = imgURI;
        this.id = id;
    }

    public int getId(){return this.id;}

    public String getNameUser(){
        return this.name;
    }

    public String getPhone(){
        return this.phone;
    }

    public String getEmail(){
        return this.email;
    }

    public String getAddress(){
        return this.address;
    }

    public Uri getImgURI(){
        return this.imgURI;
    }

}
