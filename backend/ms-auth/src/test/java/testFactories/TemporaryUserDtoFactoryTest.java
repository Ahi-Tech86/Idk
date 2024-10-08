package testFactories;

import com.ahitech.dtos.SignUpRequest;
import com.ahitech.dtos.TemporaryUserDto;
import com.ahitech.factories.TemporaryUserDtoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class TemporaryUserDtoFactoryTest {

    @InjectMocks
    private TemporaryUserDtoFactory factory;

    private SignUpRequest request;
    private String activationCode;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        activationCode = "666999";
        request = SignUpRequest.builder()
                .email("example@mail.com")
                .firstname("Firstname")
                .lastname("Lastname")
                .password("password")
                .build();
    }

    @Test
    public void testMakeTemporaryUserDto() {
        TemporaryUserDto userDto = factory.makeTemporaryUserDto(request, activationCode);

        assertNotNull(userDto);
        assertEquals(userDto.getEmail(), request.getEmail());
        assertEquals(userDto.getFirstname(), request.getFirstname());
        assertEquals(userDto.getLastname(), request.getLastname());
        assertEquals(userDto.getPassword(), request.getPassword());
        assertEquals(userDto.getActivationCode(), activationCode);
    }
}
