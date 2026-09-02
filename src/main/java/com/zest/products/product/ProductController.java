package com.zest.products.product;
import com.zest.products.product.ProductDtos.*; import io.swagger.v3.oas.annotations.security.SecurityRequirement; import jakarta.validation.Valid; import org.springframework.data.domain.*; import org.springframework.data.web.PageableDefault; import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1/products") @SecurityRequirement(name="bearerAuth") public class ProductController { private final ProductService service; public ProductController(ProductService s){service=s;}
 @GetMapping @PreAuthorize("hasAnyRole('USER','ADMIN')") Page<ProductResponse> all(@PageableDefault(size=20,sort="id") Pageable p){return service.findAll(p);}
 @GetMapping("/{id}") @PreAuthorize("hasAnyRole('USER','ADMIN')") ProductResponse one(@PathVariable Long id){return service.find(id);}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest r,Authentication a){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r,a.getName()));}
 @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") ProductResponse update(@PathVariable Long id,@Valid @RequestBody ProductRequest r,Authentication a){return service.update(id,r,a.getName());}
 @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") ResponseEntity<Void> delete(@PathVariable Long id){service.delete(id);return ResponseEntity.noContent().build();}
 @GetMapping("/{id}/items") @PreAuthorize("hasAnyRole('USER','ADMIN')") List<ItemResponse> items(@PathVariable Long id){return service.items(id);}
}
