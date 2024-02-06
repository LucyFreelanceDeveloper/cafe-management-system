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
    @Transactional
    @DirtiesContext
    public void findAllBills() {
        BillEntity createdBill = createTestBill("Bill test");
        BillEntity createdBillOne = createTestBill("Bill test one");
        assertEquals("Bill test", createdBill.getName());
        assertEquals("Bill test one", createdBillOne.getName());

        List<BillEntity> bills = billRepository.findAll();
        assertEquals(2, bills.size());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void findBill() {
        BillEntity createdBill = createTestBill("Bill test");
        assertEquals("Bill test", createdBill.getName());

        final Optional<BillEntity> billWrapper = billRepository.findById(createdBill.getId());
        assertTrue(billWrapper.isPresent());

        final BillEntity bill = billWrapper.get();
        assertEquals("Bill test", bill.getName());
        assertEquals(1, bill.getId());
    }

    @Test
    public void findNotExistentBill() {
        final Optional<BillEntity> categoryWrapper = billRepository.findById(65);
        assertTrue(categoryWrapper.isEmpty());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void createBill() {
        BillEntity createdBill = createTestBill("Bill test");
        assertEquals("Bill test", createdBill.getName());
    }

    @Test
    @DirtiesContext
    public void updateBill() {
        BillEntity createdBill = createTestBill("Bill test");
        Integer billId = createdBill.getId();

        createdBill.setName("Test name");

        billRepository.save(createdBill);

        BillEntity updatedBill = billRepository.findById(billId).get();
        assertEquals("Test name", updatedBill.getName());
    }

    @Test
    @DirtiesContext
    public void deleteBill() {
        BillEntity createdBill = createTestBill("Bill test");
        Integer billId = createdBill.getId();
        assertTrue(billRepository.findById(billId).isPresent());

        billRepository.deleteById(billId);
        assertTrue(billRepository.findById(billId).isEmpty());
    }

    public BillEntity createTestBill(String name) {
        BillEntity bill = new BillEntity();

        bill.setName(name);

        final Integer savedBillId = billRepository.save(bill).getId();

        return billRepository.findById(savedBillId).get();
    }
}