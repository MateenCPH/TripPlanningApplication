package dat.routes;

import dat.Populator.Populator;
import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.daos.impl.TripDAO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.hamcrest.CoreMatchers.equalTo;

class TripRoutesTest {

    private static Javalin app;
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final TripDAO dao = TripDAO.getInstance(emf);
    private static final Populator populator = new Populator(emf, dao);
    private static final String BASE_URL = "http://localhost:7007/api/trips";

    private static TripDTO t1DTO, t2DTO, t3DTO, t4DTO, t5DTO, t6DTO;

    @BeforeAll
    static void beforeAll() {
        app = ApplicationConfig.startServer(7007);
    }

    @BeforeEach
    void setUp() {
        populator.populateDatabase();

        t1DTO = dao.readAll().get(0);
        t2DTO = dao.readAll().get(1);
        t3DTO = dao.readAll().get(2);
        t4DTO = dao.readAll().get(3);
        t5DTO = dao.readAll().get(4);
        t6DTO = dao.readAll().get(5);
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Trip").executeUpdate();
            em.createQuery("DELETE FROM Guide").executeUpdate();
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
    void getAllTrips() {
        TripDTO[] tripDTOS =
                given()
                        .when()
                        .get(BASE_URL+"/")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(TripDTO[].class);
        for (TripDTO tripDTO : tripDTOS) {
            System.out.println(tripDTO);
        }
        assertThat(tripDTOS, arrayContainingInAnyOrder(t1DTO, t2DTO, t3DTO, t4DTO, t5DTO, t6DTO));
    }

    @Test
    void getTripById() {
        TripDTO tripDTO =
                given()
                        .when()
                        .get(BASE_URL + "/" + t1DTO.getId())
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(TripDTO.class);
        assertThat(tripDTO.getName(), is(equalTo(t1DTO.getName())));
    }

    @Test
    void filterTripsByCategory() {
        TripDTO[] tripDTOS =
                given()
                        .when()
                        .queryParam("category", t1DTO.getCategory().name())
                        .get(BASE_URL + "/categories")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(TripDTO[].class);
        assertThat(tripDTOS, arrayContainingInAnyOrder(t1DTO));
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
                        .post(BASE_URL)
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract()
                        .as(TripDTO.class);
        assertThat(created.getName(), equalTo(toCreate.getName()));
    }

    @Test
    void updateTrip() {
        assertThat(t1DTO.getName(), equalTo("Beach Paradise"));
        TripDTO toUpdate = new TripDTO("Updated Beach Paradise", t1DTO.getPrice(),
                t1DTO.getCategory(), t1DTO.getStartTime(), t1DTO.getEndTime(),
                t1DTO.getLongitude(), t1DTO.getLatitude());

        TripDTO updated =
                given()
                        .contentType("application/json")
                        .body(toUpdate)
                        .when()
                        .put(BASE_URL + "/" + t1DTO.getId())
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract()
                        .as(TripDTO.class);
        assertThat(updated.getName(), equalTo(toUpdate.getName()));
    }

    @Test
    void deleteTrip() {
        assertThat(dao.readAll(), hasSize(6));
        assertThat(dao.readAll(), containsInAnyOrder(t1DTO, t2DTO, t3DTO, t4DTO, t5DTO, t6DTO));

        given()
                .when()
                .delete(BASE_URL + "/" + t2DTO.getId())
                .then()
                .log().all()
                .statusCode(204);
        assertThat(dao.readAll(), hasSize(5));
        assertThat(dao.readAll(), containsInAnyOrder(t1DTO, t3DTO, t4DTO, t5DTO, t6DTO));
    }
}
