package com.example.cafe.repository;

import com.example.cafe.model.entity.BillEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BillRepositoryIntegrationTest {

    private final Integer billId = 1;

    @Autowired
    private BillRepository billRepository;

    @Test
    public void findAllBills() {
        List<BillEntity> bills = billRepository.findAll();
        assertEquals(3, bills.size());
    }

    @Test
    public void findBill() {
        final Optional<BillEntity> billWrapper = billRepository.findById(billId);
        assertTrue(billWrapper.isPresent());

        final BillEntity bill = billWrapper.get();
        assertEquals("lucka", bill.getName());
        assertEquals(1, bill.getId());
    }

    @Test
    public void findNotExistentBill() {
        final Optional<BillEntity> billWrapper = billRepository.findById(65);
        assertTrue(billWrapper.isEmpty());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void createBill() {
        BillEntity bill = new BillEntity();
        bill.setName("Bill test");
        assertEquals("Bill test", bill.getName());
    }

    @Test
    @DirtiesContext
    public void updateBill() {
        BillEntity bill = billRepository.findById(billId).get();

        bill.setName("Test name");

        billRepository.save(bill);

        BillEntity updatedBill = billRepository.findById(billId).get();
        assertEquals("Test name", updatedBill.getName());
    }

    @Test
    @DirtiesContext
    public void deleteBill() {
        billRepository.deleteById(billId);
        assertTrue(billRepository.findById(billId).isEmpty());
    }
}