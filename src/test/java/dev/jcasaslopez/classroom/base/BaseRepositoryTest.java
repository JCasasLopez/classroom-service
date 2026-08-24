package dev.jcasaslopez.classroom.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;

import dev.jcasaslopez.classroom.repository.ClassroomRepository;

@DataJpaTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseRepositoryTest {

    @Autowired protected ClassroomRepository repository;

    @ServiceConnection
    static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3");

    static {
        mySQLContainer.start();
    }

}
