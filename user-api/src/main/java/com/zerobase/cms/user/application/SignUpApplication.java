package com.zerobase.cms.user.application;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.SignUpCustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignUpApplication {

    private final MailgunClient mailgunClient;
    private final SignUpCustomerService signUpCustomerService;

    public void customerVerify(String email, String code) {
        signUpCustomerService.verifyEmail(email, code);
    }

    public String customerSignUp(SignUpForm signUpForm) {
        if (signUpCustomerService.isEmailExist(signUpForm.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER);
        }

        Customer customer = signUpCustomerService.signUp(signUpForm);

        String randomCode = getRandomCode();
        SendMailForm sendMailForm = SendMailForm.builder()
                .from("Excited User <Chaser@sandbox1443749e7382462db0227c44004dff26.mailgun.org>")
                .to(signUpForm.getEmail())
                .subject("Verification Email")
                .text(getVerificationEmailBody(signUpForm.getEmail(), signUpForm.getName(), randomCode))
                .build();
        mailgunClient.sendEmail(sendMailForm);
        signUpCustomerService.changeCustomerValidateEmail(customer.getId(), randomCode);

        return "회원 가입에 성공하였습니다.";
    }

    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(String email, String name, String code) {
        StringBuilder builder = new StringBuilder();
        return builder.append("Hello ")
                .append(name)
                .append("! Please Click Link for verification.\n\n")
                .append("http://localhost:8080/signup/verify/customer?email=")
                .append(email)
                .append("&code=")
                .append(code).toString();
    }
}
