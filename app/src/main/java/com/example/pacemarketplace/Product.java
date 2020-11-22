package com.example.pacemarketplace;

import com.google.firebase.firestore.PropertyName;

public class Product {
    public String name;
    public String price;
    public String description;
    public String ImgURI;
    public String productID;
    public String sellerID;
    public String category;
    public Boolean pNegotiation;

    public Product(){
        //empty constructor needed
    }

    public Product(String name, String price, String description, String productID, String sellerID) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.productID = productID;
        this.sellerID = sellerID;
        this.ImgURI = ImgURI;
    }


    public Product(String name, String price, String description, String productID, String sellerID, String ImgURI,
                   Boolean pNegotiation) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.productID = productID;
        this.sellerID = sellerID;
        this.ImgURI = ImgURI;
        this.pNegotiation = pNegotiation;
    }

    public Product(String name, String price, String description, String productID, String sellerID, String ImgURI,
                   Boolean pNegotiation, String category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.productID = productID;
        this.sellerID = sellerID;
        this.ImgURI = ImgURI;
        this.pNegotiation = pNegotiation;
        this.category = category;
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

    @PropertyName("pNegotiation")
    public Boolean getpNegotiation() {
        return pNegotiation;
    }

    public void setpNegotiation(Boolean pNegotiation){
        this.pNegotiation = pNegotiation;
    }

    @PropertyName("category")
    public String getCategory() {
            return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
