package com.example.pacemarketplace;

import com.google.firebase.firestore.PropertyName;

public class Product {
    public String name;
    public String price;
    public String description;
    public String ImgURI;

    public Product(){
        //empty constructor needed
    }

    public Product(String name, String price, String description, String imgURI) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.ImgURI = imgURI;
    }

    @PropertyName("name")
    public String getProductName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("price")
    public String getProductPrice() {
        return  price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @PropertyName("description")
    public String getProductDescription() {
        return  description;
    }

    @PropertyName("ImgURI")
    public String getImgURI(){return ImgURI;}



    public void setDescription(String description){
        this.description = description;
    }
}
