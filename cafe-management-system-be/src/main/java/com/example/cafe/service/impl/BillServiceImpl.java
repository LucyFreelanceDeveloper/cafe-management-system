package com.example.cafe.service.impl;

import com.example.cafe.config.security.JwtFilter;
import com.example.cafe.constants.CafeConstants;
import com.example.cafe.mapper.BillMapper;
import com.example.cafe.model.dto.BillDto;
import com.example.cafe.model.entity.BillEntity;
import com.example.cafe.repository.BillRepository;
import com.example.cafe.service.BillService;
import com.example.cafe.util.CafeUtils;
import com.example.cafe.util.PdfUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    private final JwtFilter jwtFilter;

    public BillServiceImpl(BillRepository billRepository, JwtFilter jwtFilter) {
        this.billRepository = billRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> generateReport(BillDto billDto) {
        try {
            billDto.setUuid(CafeUtils.getUUID());
            billDto.setCreatedBy(jwtFilter.getCurrentUser());
            BillEntity billEntity = BillMapper.INSTANCE.billDtoToBillEntity(billDto);
            billRepository.save(billEntity);
            PdfUtils.generateAndSaveBillReport(billDto);
            return CafeUtils.getResponseEntity(String.format("Successfully create bill - [uuid:%s]", billEntity.getUuid()), HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("Failed call generateReport", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}