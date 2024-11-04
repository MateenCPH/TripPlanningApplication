package dat.utils;

import dat.config.HibernateConfig;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.entities.Trip.TripCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Populator {

    private EntityManagerFactory emf;

    public static void populate() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create guides
            Guide guide1 = new Guide("John", "Doe", "john.doe@example.com", "1234567890", 5);
            Guide guide2 = new Guide("Jane", "Smith", "jane.smith@example.com", "0987654321", 10);
            Guide guide3 = new Guide("Emily", "Brown", "emily.brown@example.com", "1112223333", 3);

            em.persist(guide1);
            em.persist(guide2);
            em.persist(guide3);

            // Create trips
            Trip trip1 = new Trip("Beach Paradise", 499.99, TripCategory.beach, "2023-06-01 09:00", "2023-06-10 18:00", 123456L, 654321L);
            trip1.setGuide(guide1);

            Trip trip2 = new Trip("City Adventure", 299.99, TripCategory.city, "2023-07-15 08:00", "2023-07-20 17:00", 123457L, 654322L);
            trip2.setGuide(guide1);

            Trip trip3 = new Trip("Forest Escape", 399.99, TripCategory.forest, "2023-08-05 07:00", "2023-08-15 16:00", 123458L, 654323L);
            trip3.setGuide(guide2);

            Trip trip4 = new Trip("Lake Retreat", 199.99, TripCategory.lake, "2023-09-10 06:00", "2023-09-20 15:00", 123459L, 654324L);
            trip4.setGuide(guide2);

            Trip trip5 = new Trip("Sea Expedition", 599.99, TripCategory.sea, "2023-10-01 05:00", "2023-10-10 14:00", 123460L, 654325L);
            trip5.setGuide(guide3);

            Trip trip6 = new Trip("Snowy Peaks", 699.99, TripCategory.snow, "2023-11-15 04:00", "2023-11-25 13:00", 123461L, 654326L);
            trip6.setGuide(guide3);

            em.persist(trip1);
            em.persist(trip2);
            em.persist(trip3);
            em.persist(trip4);
            em.persist(trip5);
            em.persist(trip6);

            em.getTransaction().commit();
            em.close();
        }
    }
}