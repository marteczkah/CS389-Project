package com.example.pacemarketplace;

import com.google.firebase.firestore.PropertyName;

public class Product {
    public String name;
    public String price;
    public String description;
    public String productID;
    public String sellerID;

    public Product(){
        //empty constructor needed
    }

    public Product(String name, String price, String description, String productID, String sellerID) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.productID = productID;
        this.sellerID = sellerID;
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

    public void setDescription(String description){
        this.description = description;
    }

    @PropertyName("productID")
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    @PropertyName("sellerID")
    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }
}
