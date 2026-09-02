package com.zest.products.product;
import jakarta.persistence.*; import java.time.Instant; import java.util.*;
@Entity @Table(name="product",indexes=@Index(name="idx_product_name",columnList="product_name")) public class Product {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="product_name",nullable=false,length=255) private String productName;
 @Column(name="created_by",nullable=false,length=100,updatable=false) private String createdBy;
 @Column(name="created_on",nullable=false,updatable=false) private Instant createdOn;
 @Column(name="modified_by",length=100) private String modifiedBy; @Column(name="modified_on") private Instant modifiedOn;
 @OneToMany(mappedBy="product",cascade=CascadeType.ALL,orphanRemoval=true) private List<Item> items=new ArrayList<>();
 @PrePersist void created(){createdOn=Instant.now();} @PreUpdate void modified(){modifiedOn=Instant.now();}
 public Long getId(){return id;} public String getProductName(){return productName;} public void setProductName(String v){productName=v;} public String getCreatedBy(){return createdBy;} public void setCreatedBy(String v){createdBy=v;} public Instant getCreatedOn(){return createdOn;} public String getModifiedBy(){return modifiedBy;} public void setModifiedBy(String v){modifiedBy=v;} public Instant getModifiedOn(){return modifiedOn;} public List<Item> getItems(){return items;}
}
