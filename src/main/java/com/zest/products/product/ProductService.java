package com.zest.products.product;
import com.zest.products.product.ProductDtos.*; import org.springframework.data.domain.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.*;
@Service @Transactional public class ProductService { private final ProductRepository repo; public ProductService(ProductRepository r){repo=r;}
 public Page<ProductResponse> findAll(Pageable p){return repo.findAll(p).map(this::map);} public ProductResponse find(Long id){return map(entity(id));}
 public ProductResponse create(ProductRequest r,String actor){Product p=new Product();p.setProductName(r.productName());p.setCreatedBy(actor);items(p,r.items());return map(repo.save(p));}
 public ProductResponse update(Long id,ProductRequest r,String actor){Product p=entity(id);p.setProductName(r.productName());p.setModifiedBy(actor);p.getItems().clear();items(p,r.items());return map(p);}
 public void delete(Long id){repo.delete(entity(id));} public List<ItemResponse> items(Long id){return map(entity(id)).items();}
 private Product entity(Long id){return repo.findById(id).orElseThrow(()->new NoSuchElementException("Product "+id+" was not found"));}
 private void items(Product p,List<ItemRequest> xs){if(xs!=null) for(ItemRequest x:xs){Item i=new Item();i.setProduct(p);i.setQuantity(x.quantity());p.getItems().add(i);}}
 private ProductResponse map(Product p){return new ProductResponse(p.getId(),p.getProductName(),p.getCreatedBy(),p.getCreatedOn(),p.getModifiedBy(),p.getModifiedOn(),p.getItems().stream().map(i->new ItemResponse(i.getId(),i.getQuantity())).toList());}
}
