package myspringboot.reactive.mongo.controller;

import lombok.RequiredArgsConstructor;
import myspringboot.reactive.mongo.dto.ProductDto;
import myspringboot.reactive.mongo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController
{
    private final ProductService service;

    @GetMapping
    public Flux<ProductDto> getProducts()
    {
        return service.getAllProducts();
    }

    @GetMapping("{id}")
    public Mono<ProductDto> getProduct(@PathVariable String id)
    {
        return service.getProduct(id).log();
    }

    @GetMapping("/re/{id}")
    public Mono<ResponseEntity<ProductDto>> getProductRE(@PathVariable String id)
    {
        return service.getProductRE(id).log();
    }

    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono)
    {
        return service.saveProduct(productDtoMono).log();
    }

    @PostMapping("/re")
    public Mono<ResponseEntity<ProductDto>> saveProductRE(@RequestBody Mono<ProductDto> productDtoMono)
    {
        return service.saveProductRE(productDtoMono).log();
    }

    @PatchMapping("/{id}")
    public Mono<ProductDto> updateProduct(@RequestBody Mono<ProductDto> productDtoMono, @PathVariable String id)
    {
        return service.updateProduct(productDtoMono, id);
    }

    @PatchMapping
    public Mono<ResponseEntity<ProductDto>> updateProductRE(@RequestBody Mono<ProductDto> productDtoMono, @PathVariable String id)
    {
        return service.updateProductRE(productDtoMono, id);
    }
}
