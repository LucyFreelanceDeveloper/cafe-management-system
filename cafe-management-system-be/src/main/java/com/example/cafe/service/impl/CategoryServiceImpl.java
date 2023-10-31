package com.example.cafe.service.impl;

import com.example.cafe.config.security.JwtFilter;
import com.example.cafe.constants.CafeConstants;
import com.example.cafe.mapper.CategoryMapper;
import com.example.cafe.model.dto.CategoryDto;
import com.example.cafe.model.entity.CategoryEntity;
import com.example.cafe.repository.CategoryRepository;
import com.example.cafe.service.CategoryService;
import com.example.cafe.util.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final JwtFilter jwtFilter;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, JwtFilter jwtFilter) {
        this.categoryRepository = categoryRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> create(CategoryDto categoryDto) {
        try {
            if (jwtFilter.isAdmin()) {
                CategoryEntity categoryEntity = CategoryMapper.INSTANCE.categoryDtoToCategoryEntity(categoryDto);
                categoryEntity = categoryRepository.save(categoryEntity);
                return CafeUtils.getResponseEntity(String.format("Category Added Successfully: [id:%s]", categoryEntity.getId()), HttpStatus.CREATED);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            log.error("Failed create category", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<CategoryDto>> findAll() {
        try {
            List<CategoryDto> categories = categoryRepository.findAll()
                    .stream()
                    .map(c -> CategoryMapper.INSTANCE.categoryEntityToCategoryDto(c))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Failed call findAll", ex);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> update(CategoryDto categoryDto) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<CategoryEntity> categoryWrapper = categoryRepository.findById(categoryDto.getId());
                if(categoryWrapper.isPresent()){
                    CategoryEntity category = categoryWrapper.get();
                    category.setName(categoryDto.getName());
                    categoryRepository.save(category);
                    return CafeUtils.getResponseEntity("Category Updated Successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Category id does not exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            log.error("Failed update category", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> delete(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {
                if (categoryRepository.existsById(id)) {
                    categoryRepository.deleteById(id);
                    return CafeUtils.getResponseEntity("Category Delete Successfully", HttpStatus.OK);

                } else {
                    return CafeUtils.getResponseEntity("Category id does not exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            log.error("Failed delete category", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}