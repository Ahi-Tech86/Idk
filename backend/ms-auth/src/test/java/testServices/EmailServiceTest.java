package testServices;

import com.ahitech.services.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

public class EmailServiceTest {
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl service;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSendActivationCodeToEmail() {
        String email = "test@mail.com";
        String activationCode = "123456";
        String titleMessage = "Account activation";

        service.sendActivationCodeToEmail(email, activationCode, titleMessage);

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom(fromEmail);
        expectedMessage.setTo(email);
        expectedMessage.setSubject(titleMessage);
        expectedMessage.setText("Yours activation code: " + activationCode);

        verify(javaMailSender, times(1)).send(expectedMessage);
    }
}
