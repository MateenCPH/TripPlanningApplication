package dat.daos.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dat.daos.IDAO;
import dat.daos.ITripGuideDAO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.exceptions.ApiException;
import jakarta.persistence.*;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Set;

public class TripDAO implements IDAO<TripDTO, Integer>, ITripGuideDAO<Integer, Integer> {

    private static TripDAO instance;
    private static EntityManagerFactory emf;

    public static TripDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TripDAO();
        }
        return instance;
    }


    @Override
    public TripDTO readById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip tripCheck = em.find(Trip.class, id);
            if (tripCheck.getGuide() != null) {
                TypedQuery<TripDTO> query = em.createQuery("SELECT new dat.dtos.TripDTO(t) FROM Trip t JOIN FETCH t.guide WHERE t.id = :id", TripDTO.class);
                query.setParameter("id", id);
                TripDTO tripDTO = query.getSingleResult();
                return tripDTO;
            } else {
                TypedQuery<TripDTO> query = em.createQuery("SELECT new dat.dtos.TripDTO(t) FROM Trip t WHERE t.id = :id", TripDTO.class);
                query.setParameter("id", id);
                TripDTO tripDTO = query.getSingleResult();
                return tripDTO;
            }
        }
    }

    @Override
    public List<TripDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TripDTO> query = em.createQuery("SELECT new dat.dtos.TripDTO(t) FROM Trip t", TripDTO.class);
            List<TripDTO> trips = query.getResultList();
            if (trips.isEmpty()) {
                throw new EntityNotFoundException("No trips found");
            }
            return trips;
        }
    }

    @Override
    public TripDTO create(TripDTO tripDTO) throws InvalidFormatException, JsonParseException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = new Trip(tripDTO);
            em.persist(trip);
            em.getTransaction().commit();
            return new TripDTO(trip);
        } catch (Exception e) {
            throw new ApiException(400, "Trip could not be created: " + e.getMessage());
        }
    }

    @Override
    public TripDTO update(Integer id, TripDTO updatedTripDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);

            if (trip == null) {
                em.getTransaction().rollback();
                throw new EntityNotFoundException("Trip with id: " + id + " not found");
            }

            if (updatedTripDTO.getName() != null && !updatedTripDTO.getName().isEmpty()) {
                trip.setName(updatedTripDTO.getName());
            }
            if (updatedTripDTO.getPrice() > 0) {
                trip.setPrice(updatedTripDTO.getPrice());
            }
            if (updatedTripDTO.getCategory() != null) {
                trip.setCategory(updatedTripDTO.getCategory());
            }
            if (updatedTripDTO.getStartTime() != null && !updatedTripDTO.getStartTime().isEmpty()) {
                trip.setStartTime(updatedTripDTO.getStartTime());
            }
            if (updatedTripDTO.getEndTime() != null && !updatedTripDTO.getEndTime().isEmpty()) {
                trip.setEndTime(updatedTripDTO.getEndTime());
            }
            if (updatedTripDTO.getLongitude() != 0) {
                trip.setLongitude(updatedTripDTO.getLongitude());
            }
            if (updatedTripDTO.getLatitude() != 0) {
                trip.setLatitude(updatedTripDTO.getLatitude());
            }

            Trip mergedTrip = em.merge(trip);
            em.getTransaction().commit();
            return new TripDTO(mergedTrip);
        } catch (ConstraintViolationException e) {
            throw new ApiException(400, "Trip could not be updated: " + e.getMessage());
        }
    }


    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);

            if (trip == null) {
                em.getTransaction().rollback();
                throw new EntityNotFoundException("Trip with id: " + id + " not found");
            }
            em.remove(trip);
            em.getTransaction().commit();
        }
    }

    @Override
    public void addGuideToTrip(Integer tripId, Integer guideId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, tripId);
            Guide guide = em.find(Guide.class, guideId);

            if (trip == null) {
                em.getTransaction().rollback();
                throw new EntityNotFoundException("Trip with id: " + tripId + " not found");
            }
            if (guide == null) {
                em.getTransaction().rollback();
                throw new EntityNotFoundException("Guide with id: " + guideId + " not found");
            }

            trip.setGuide(guide);
            em.merge(trip);
            em.getTransaction().commit();
        }
    }

    @Override
    public Set<TripDTO> getTripsByGuideId(Integer guideId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TripDTO> query = em.createQuery("SELECT new dat.dtos.TripDTO(t) FROM Trip t WHERE t.guide.id = :guideId", TripDTO.class);
            query.setParameter("guideId", guideId);
            Set<TripDTO> trips = Set.copyOf(query.getResultList());
            if (trips.isEmpty()) {
                throw new EntityNotFoundException("No trips found for guide with id: " + guideId);
            }
            return trips;
        }
    }
}