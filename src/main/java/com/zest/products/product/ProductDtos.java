package com.zest.products.product;
import jakarta.validation.Valid; import jakarta.validation.constraints.*; import java.time.Instant; import java.util.List;
public final class ProductDtos { private ProductDtos(){} public record ItemRequest(@NotNull @Positive Integer quantity){} public record ProductRequest(@NotBlank @Size(max=255) String productName,@Valid List<ItemRequest> items){} public record ItemResponse(Long id,int quantity){} public record ProductResponse(Long id,String productName,String createdBy,Instant createdOn,String modifiedBy,Instant modifiedOn,List<ItemResponse> items){} }
