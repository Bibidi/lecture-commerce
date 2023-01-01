package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SignUpCustomerServiceTest {

    @Autowired
    private SignUpCustomerService signUpCustomerService;

    @Test
    void signUp() {
        SignUpForm form = SignUpForm.builder()
                .name("name")
                .email("email")
                .password("password")
                .phone("01000000000")
                .birth(LocalDate.now())
                .build();
        Customer customer = signUpCustomerService.signUp(form);
        assertNotNull(customer.getId());
        assertNotNull(customer.getCreatedAt());
    }
}