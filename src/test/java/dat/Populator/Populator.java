package dat.Populator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dat.daos.impl.TripDAO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.entities.Trip.TripCategory;
import dat.exceptions.ApiException;
import dat.security.daos.SecurityDAO;
import dk.bugelhartmann.UserDTO;
import dat.security.entities.Role;
import dat.security.entities.User;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;

public class Populator {
    private static EntityManagerFactory emf;
    private static TripDAO tripDao;

    public Populator(EntityManagerFactory _emf, TripDAO tripDao_) {
        emf = _emf;
        tripDao = tripDao_;
    }

    public static void populateDatabase() {
        // Create guide entities
        Guide guide1 = Guide.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .yearsOfExperience(5)
                .build();

        Guide guide2 = Guide.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phoneNumber("0987654321")
                .yearsOfExperience(10)
                .build();

        Guide guide3 = Guide.builder()
                .firstName("Emily")
                .lastName("Brown")
                .email("emily.brown@example.com")
                .phoneNumber("1112223333")
                .yearsOfExperience(3)
                .build();

        // Create trip entities and assign to guides
        Trip trip1 = Trip.builder()
                .name("Beach Paradise")
                .price(499.99)
                .category(TripCategory.beach)
                .startTime("2023-06-01 09:00")
                .endTime("2023-06-10 18:00")
                .longitude(123456L)
                .latitude(654321L)
                .guide(guide1)
                .build();

        Trip trip2 = Trip.builder()
                .name("City Adventure")
                .price(299.99)
                .category(TripCategory.city)
                .startTime("2023-07-15 08:00")
                .endTime("2023-07-20 17:00")
                .longitude(123457L)
                .latitude(654322L)
                .guide(guide1)
                .build();

        Trip trip3 = Trip.builder()
                .name("Forest Escape")
                .price(399.99)
                .category(TripCategory.forest)
                .startTime("2023-08-05 07:00")
                .endTime("2023-08-15 16:00")
                .longitude(123458L)
                .latitude(654323L)
                .guide(guide2)
                .build();

        Trip trip4 = Trip.builder()
                .name("Lake Retreat")
                .price(199.99)
                .category(TripCategory.lake)
                .startTime("2023-09-10 06:00")
                .endTime("2023-09-20 15:00")
                .longitude(123459L)
                .latitude(654324L)
                .guide(guide2)
                .build();

        Trip trip5 = Trip.builder()
                .name("Sea Expedition")
                .price(599.99)
                .category(TripCategory.sea)
                .startTime("2023-10-01 05:00")
                .endTime("2023-10-10 14:00")
                .longitude(123460L)
                .latitude(654325L)
                .guide(guide3)
                .build();

        Trip trip6 = Trip.builder()
                .name("Snowy Peaks")
                .price(699.99)
                .category(TripCategory.snow)
                .startTime("2023-11-15 04:00")
                .endTime("2023-11-25 13:00")
                .longitude(123461L)
                .latitude(654326L)
                .guide(guide3)
                .build();

        // Save trips (which also saves guides because of cascade setting)
        try {
            tripDao.create(new TripDTO(trip1));
            tripDao.create(new TripDTO(trip2));
            tripDao.create(new TripDTO(trip3));
            tripDao.create(new TripDTO(trip4));
            tripDao.create(new TripDTO(trip5));
            tripDao.create(new TripDTO(trip6));
        } catch (ApiException | InvalidFormatException | JsonParseException e) {
            e.printStackTrace();
        }
    }

    public static UserDTO[] populateUsers() {

        User user, admin;
        Role userRole, adminRole;

        user = new User("usertest", "user123");
        admin = new User("admintest", "admin123");
        userRole = new Role("USER");
        adminRole = new Role("ADMIN");
        user.addRole(userRole);
        admin.addRole(adminRole);

        if (emf == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.getTransaction().commit();
        }
        UserDTO userDTO = new UserDTO(user.getUsername(), "user123");
        UserDTO adminDTO = new UserDTO(admin.getUsername(), "admin123");
        return new UserDTO[]{userDTO, adminDTO};
    }
}
