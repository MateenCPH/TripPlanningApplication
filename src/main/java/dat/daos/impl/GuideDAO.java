package dat.daos.impl;

import dat.dtos.GuideDTO;
import dat.entities.Guide;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class GuideDAO {
    private static GuideDAO instance;
    private static EntityManagerFactory emf;

    public static GuideDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GuideDAO();
        }
        return instance;
    }

    public List<GuideDTO> readAllWithTrips() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<GuideDTO> query = em.createQuery(
                    "SELECT new dat.dtos.GuideDTO(g) FROM Guide g LEFT JOIN FETCH g.trips", GuideDTO.class
            );
            return query.getResultList();
        }
    }
}