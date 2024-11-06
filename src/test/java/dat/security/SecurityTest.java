package dat.security;

import dat.Populator.Populator;
import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.daos.impl.TripDAO;
import dat.dtos.TripDTO;
import dat.entities.Trip;
import dat.security.controllers.SecurityController;
import dat.security.daos.SecurityDAO;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SecurityTest {
    private static Javalin app;
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final TripDAO tripDAO = TripDAO.getInstance(emf);
    private static final Populator populator = new Populator(emf, tripDAO);
    private static final String BASE_URL = "http://localhost:9009/api/";
    //Security
    private static SecurityDAO securityDAO = new SecurityDAO(emf);
    private static SecurityController securityController = new SecurityController();
    private static UserDTO userDTO, adminDTO;
    private static String userToken, adminToken;
    private static UserDTO[] users;

    private static TripDTO t1DTO, t2DTO, t3DTO, t4DTO, t5DTO, t6DTO;

    @BeforeAll
    static void beforeAll() {
        app = ApplicationConfig.startServer(9009);
    }

    @BeforeEach
    void setUp() {
        populator.populateDatabase();

        t1DTO = tripDAO.readAll().get(0);
        t2DTO = tripDAO.readAll().get(1);
        t3DTO = tripDAO.readAll().get(2);
        t4DTO = tripDAO.readAll().get(3);
        t5DTO = tripDAO.readAll().get(4);
        t6DTO = tripDAO.readAll().get(5);

        users = Populator.populateUsers();
        userDTO = users[0];
        adminDTO = users[1];

        try {
            UserDTO verifiedUser = securityDAO.getVerifiedUser(userDTO.getUsername(), userDTO.getPassword());
            UserDTO verifiedAdmin = securityDAO.getVerifiedUser(adminDTO.getUsername(), adminDTO.getPassword());
            userToken = "Bearer " + securityController.createToken(verifiedUser);
            adminToken = "Bearer " + securityController.createToken(verifiedAdmin);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Trip").executeUpdate();
            em.createQuery("DELETE FROM Guide").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE trip_trip_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE guide_guide_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @AfterAll
    static void afterAll() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void createTrip() {
        TripDTO toCreate = new TripDTO("Mountain Adventure", 750.00, Trip.TripCategory.forest,
                "2024-05-01T08:00", "2024-05-10T18:00", 456789L, 987654L);
        TripDTO created =
                given()
                        .contentType("application/json")
                        .body(toCreate)
                        .when()
                        .header("Authorization", adminToken)
                        .post(BASE_URL + "trips")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract()
                        .as(TripDTO.class);
        assertThat(created.getName(), equalTo(toCreate.getName()));
    }
}
