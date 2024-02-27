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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;

    private final JwtFilter jwtFilter;

    @Value("${pdf.store.relative-location}")
    private String pdfStoreRelativeLocation;

    public BillServiceImpl(BillRepository billRepository, JwtFilter jwtFilter) {
        this.billRepository = billRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> generateReport(BillDto billDto) {
        try {
            BillDto billDtoPrepared = new BillDto(
                    billDto.id(),
                    CafeUtils.getUUID(),
                    billDto.name(),
                    billDto.email(),
                    billDto.contactNumber(),
                    billDto.paymentMethod(),
                    billDto.total(),
                    billDto.productDetail(),
                    jwtFilter.getCurrentUser()
            );
            BillEntity billEntity = BillMapper.INSTANCE.billDtoToBillEntity(billDtoPrepared);
            BillEntity billEntitySaved = billRepository.save(billEntity);
            String absolutePath = Paths.get(System.getProperty("user.home"), pdfStoreRelativeLocation, billDto.uuid() + ".pdf").toString();
            PdfUtils.generateAndSaveBillReport(billDtoPrepared, absolutePath);
            return new ResponseEntity<String>(billEntitySaved.getId().toString(), HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("Failed call generateReport", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<BillDto>> findAll() {
        try {
            if (jwtFilter.isAdmin()) {
                List<BillDto> bills = billRepository.findAll()
                        .stream()
                        .map(bill -> BillMapper.INSTANCE.billEntityToBillDto(bill))
                        .collect(Collectors.toList());
                return new ResponseEntity<>(bills, HttpStatus.OK);
            } else {
                List<BillDto> bills = billRepository.findByCreatedBy(jwtFilter.getCurrentUser())
                        .stream()
                        .map(bill -> BillMapper.INSTANCE.billEntityToBillDto(bill))
                        .collect(Collectors.toList());
                return new ResponseEntity<>(bills, HttpStatus.OK);
            }

        } catch (Exception ex) {
            log.error("Failed call findAll", ex);
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> delete(Integer id) {
        try {
            if (billRepository.existsById(id)) {
                BillDto billDto = BillMapper.INSTANCE.billEntityToBillDto(billRepository.findById(id).get());
                String absolutePath = Paths.get(System.getProperty("user.home"), pdfStoreRelativeLocation, billDto.uuid() + ".pdf").toString();
                new File(absolutePath).delete();
                billRepository.deleteById(id);
                return CafeUtils.getResponseEntity("Bill Delete Successfully", HttpStatus.OK);
            } else {
                return CafeUtils.getResponseEntity("Bill id does not exist", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Failed call delete", ex);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<byte[]> findPdf(Integer id) {
        try {
            if (billRepository.existsById(id)) {
                BillDto billDto = BillMapper.INSTANCE.billEntityToBillDto(billRepository.findById(id).get());
                String absolutePath = Paths.get(System.getProperty("user.home"), pdfStoreRelativeLocation, billDto.uuid() + ".pdf").toString();
                byte[] bytes = Files.readAllBytes(Paths.get(absolutePath));
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(bytes);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            log.error("Failed call findPdf", ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}