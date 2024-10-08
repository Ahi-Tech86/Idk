package testFactories;

import com.ahitech.dtos.TemporaryUserDto;
import com.ahitech.factories.UserEntityFactory;
import com.ahitech.storage.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static com.ahitech.enums.AppRole.USER;
import static org.junit.jupiter.api.Assertions.*;

public class UserEntityFactoryTest {
    @InjectMocks
    private UserEntityFactory factory;

    private TemporaryUserDto userDto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        userDto = TemporaryUserDto.builder()
                .email("example@mail.com")
                .firstname("Firstname")
                .lastname("Lastname")
                .password("password")
                .activationCode("123456")
                .build();
    }

    @Test
    public void testMakingUserEntity() {
        UserEntity createdEntity = factory.makeUserEntity(userDto);

        assertNotNull(createdEntity);
        assertEquals(createdEntity.getEmail(), userDto.getEmail());
        assertEquals(createdEntity.getFirstname(), userDto.getFirstname());
        assertEquals(createdEntity.getLastname(), userDto.getLastname());
        assertEquals(createdEntity.getRole(), USER);
    }
}
