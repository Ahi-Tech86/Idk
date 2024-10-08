package testServices;

import com.ahitech.services.AuthServiceImpl;
import com.ahitech.storage.entities.UserEntity;
import com.ahitech.storage.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private AuthServiceImpl authService;

    private Method method;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        method = AuthServiceImpl.class.getDeclaredMethod("isEmailValid", String.class);
        method.setAccessible(true);
    }

    @Test
    public void testEmailIsNull() throws Exception {
        String email = null;
        assertEmailValidity(email, false);
    }

    @Test
    public void testEmailIsEmpty() throws Exception {
        String email = "";
        assertEmailValidity(email, false);
    }

    @Test
    public void testEmailIsValid() throws Exception {
        String email = "example@mail.com";
        assertEmailValidity(email, true);
    }

    @Test
    public void testEmailIsInvalid() throws Exception {
        String invalidEmail = "user@.afg";
        String invalidEmail2 = "example@@email.afg";
        assertEmailValidity(invalidEmail, false);
        assertEmailValidity(invalidEmail2, false);
    }

    @Test
    public void testEmailIsUniqueness() throws Exception {
        String email = "unique@mail.com";

        // specify that the method returns empty(the email is unique)
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        // getting access to private method
        Method method = AuthServiceImpl.class.getDeclaredMethod("isEmailUniqueness", String.class);
        method.setAccessible(true);

        // calling method
        assertDoesNotThrow(() -> {
            method.invoke(authService, email);
        });

        // checking that the method was called with this email
        verify(repository, times(1)).findByEmail(email);
    }

    @Test
    public void testEmailIsNotUniqueness() throws Exception {
        String email = "not-unique@email.com";

        // specify that the method found user entity and returning this entity
        when(repository.findByEmail(email)).thenReturn(Optional.of(new UserEntity()));

        // getting access to private method
        Method method = AuthServiceImpl.class.getDeclaredMethod("isEmailUniqueness", String.class);
        method.setAccessible(true);

        // checking that when we try register with existing email, we get exception
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(authService, email);
        });

        // checking that the method was called with this email
        verify(repository, times(1)).findByEmail(email);
    }

    @Test
    public void testGenerateActivationCode() throws Exception {
        // getting access to private method
        Method method = AuthServiceImpl.class.getDeclaredMethod("generateActivationCode");
        method.setAccessible(true);

        for (int i = 0; i < 10; i++) {
            String activationCode = (String) method.invoke(authService);
            // checking that code is not null
            assertNotNull(activationCode);
            // checking that code have length 6
            assertEquals(6, activationCode.length());

            int activationCodeAsInt = Integer.parseInt(activationCode.trim());
            assertTrue(activationCodeAsInt >= 1 && activationCodeAsInt <= 1000000);
        }
    }


    private void assertEmailValidity(String email, boolean expected) throws Exception {
        assertEquals(expected, (Boolean) method.invoke(authService, email));
    }
}
