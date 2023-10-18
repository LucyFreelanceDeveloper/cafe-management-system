package com.example.cafe.service.impl;

import com.example.cafe.config.security.JwtFilter;
import com.example.cafe.constants.CafeConstants;
import com.example.cafe.mapper.ProductMapper;
import com.example.cafe.model.dto.ProductDto;
import com.example.cafe.model.entity.CategoryEntity;
import com.example.cafe.model.entity.ProductEntity;
import com.example.cafe.repository.CategoryRepository;
import com.example.cafe.repository.ProductRepository;
import com.example.cafe.service.ProductService;
import com.example.cafe.util.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final JwtFilter jwtFilter;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              JwtFilter jwtFilter) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> create(ProductDto productDto) {
        try {
            if (jwtFilter.isAdmin()) {
                ProductEntity productEntity = ProductMapper.INSTANCE.productDtoToProductEntity(productDto);
                Optional<CategoryEntity> categoryEntityWrapper = categoryRepository.findById(productDto.getCategoryId());
                if (categoryEntityWrapper.isEmpty()) {
                    return CafeUtils.getResponseEntity(String.format("Category %s not fond", productDto.getCategoryId()), HttpStatus.NOT_FOUND);
                }

                productEntity.setId(null); //Prevent remove before create new record
                productEntity.setCategory(categoryEntityWrapper.get());
                productEntity.setStatus("true");
                productEntity = productRepository.save(productEntity);
                return CafeUtils.getResponseEntity(String.format("Product Added Successfully: [id:%s]", productEntity.getId()), HttpStatus.CREATED);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            log.error("Failed create product", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ProductDto>> findAll() {
        try {
            List<ProductDto> products = productRepository.findAll()
                    .stream()
                    .map(product -> ProductMapper.INSTANCE.productEntityToProductDto(product))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Failed call findAll", ex);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ProductDto> findById(Integer id) {
        try {
            final Optional<ProductEntity> productEntityWrapper = productRepository.findById(id);
            if (productEntityWrapper.isPresent()) {
                return new ResponseEntity<>(
                        ProductMapper.INSTANCE.productEntityToProductDto(productEntityWrapper.get()),
                        HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            log.error("Failed call findById", ex);
            return new ResponseEntity<>(new ProductDto(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ProductDto>> findByCategoryId(Integer categoryId) {
        try {
            Optional<CategoryEntity> categoryEntityWrapper = categoryRepository.findById(categoryId);

            if (categoryEntityWrapper.isPresent()) {
                List<ProductDto> products = categoryEntityWrapper.get().getProducts()
                        .stream()
                        .map(product -> ProductMapper.INSTANCE.productEntityToProductDto(product))
                        .collect(Collectors.toList());
                return new ResponseEntity<>(products, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            log.error("Failed call findByCategoryId", ex);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> update(ProductDto productDto) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<ProductEntity> productEntityWrapper = productRepository.findById(productDto.getId());
                if (productEntityWrapper.isPresent()) {
                    ProductEntity productEntity = productEntityWrapper.get();

                    if (productDto.getName() != null) {
                        productEntity.setName(productDto.getName());
                    }
                    if (productDto.getDescription() != null) {
                        productEntity.setDescription(productDto.getDescription());
                    }
                    if (productDto.getPrice() != null) {
                        productEntity.setPrice(productDto.getPrice());
                    }
                    if (productDto.getStatus() != null) {
                        productEntity.setStatus(productDto.getStatus());
                    }

                    if (productDto.getCategoryId() != null) {
                        Optional<CategoryEntity> categoryEntityWrapper = categoryRepository.findById(productDto.getCategoryId());

                        if (categoryEntityWrapper.isEmpty()) {
                            return CafeUtils.getResponseEntity(String.format("Category %s not fond", productDto.getCategoryId()), HttpStatus.NOT_FOUND);
                        }

                        productEntity.setCategory(categoryEntityWrapper.get());
                    }

                    productRepository.save(productEntity);
                    return CafeUtils.getResponseEntity("Product Updated Successfully", HttpStatus.OK);

                } else {
                    return CafeUtils.getResponseEntity("Product id does not exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            log.error("Failed update product", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> delete(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {
                if (productRepository.existsById(id)) {
                    productRepository.deleteById(id);
                    return CafeUtils.getResponseEntity("Product Delete Successfully", HttpStatus.OK);

                } else {
                    return CafeUtils.getResponseEntity("Product id does not exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            log.error("Failed delete product", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ProductEntity updateProductEntity(ProductDto productDto, Optional<ProductEntity> productEntityWrapper) {
        ProductEntity productEntity = productEntityWrapper.get();

        productEntity.setId(productDto.getId());
        productEntity.setName(productDto.getName());
        productEntity.setDescription(productDto.getDescription());
        productEntity.setPrice(productDto.getPrice());
        productEntity.setStatus(productDto.getStatus());

        if (productDto.getCategoryId() != null) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(productDto.getCategoryId());
            productEntity.setCategory(categoryEntity);
        }

        return productEntity;
    }
}