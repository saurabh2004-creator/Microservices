package com.app.ecom.service;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;
import com.app.ecom.model.Product;
import com.app.ecom.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product,productRequest);
        Product savedproduct = productRepository.save(product);
        return mapToProductResponse(savedproduct);
    }

    @Override
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct,productRequest);
                    Product savedproduct = productRepository.save(existingProduct);
                    return mapToProductResponse(savedproduct);
                });
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    @Override
    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    private ProductResponse mapToProductResponse(Product savedproduct) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(String.valueOf(savedproduct.getId()));
        productResponse.setName(savedproduct.getName());
        productResponse.setDescription(savedproduct.getDescription());
        productResponse.setImageUrl(savedproduct.getImageUrl());
        productResponse.setCategory(savedproduct.getCategory());
        productResponse.setPrice(savedproduct.getPrice());
        productResponse.setStockQuantity(savedproduct.getStockQuantity());
        productResponse.setActive(savedproduct.getActive());
        return productResponse;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImageUrl(productRequest.getImageUrl());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
    }
}
