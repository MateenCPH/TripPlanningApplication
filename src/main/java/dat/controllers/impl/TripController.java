package dat.controllers.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dat.config.HibernateConfig;
import dat.daos.impl.TripDAO;
import dat.dtos.PackingItemDTO;
import dat.dtos.PackingListDTO;
import dat.dtos.TripDTO;
import dat.entities.Trip;
import dat.exceptions.ApiException;
import dat.utils.Populator;
import io.javalin.http.Context;
import io.javalin.validation.ValidationException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;


import java.util.List;
import java.util.Map;
import java.util.Set;

public class TripController {
    private final TripDAO dao;

    public TripController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = TripDAO.getInstance(emf);
    }

    public void readAll(Context ctx) {
        try {
            List<TripDTO> trips = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(trips);
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "No trips found");
        }
    }

    public void readById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO tripDTO = dao.readById(id);

            if (tripDTO.getCategory() != null) {
                PackingListDTO packingList = dat.services.PackingService.getPackingItemsByCategory(tripDTO.getCategory().name());
                tripDTO.setPackingItems(packingList.getItems());
            }

            ctx.res().setStatus(200);
            ctx.json(tripDTO);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (NullPointerException e) {
            throw new ApiException(404, "Trip with id: " + ctx.pathParam("id") + " not found");
        }
    }

    public void create(Context ctx) {
        TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
        try {
            TripDTO createdTrip = dao.create(tripDTO);
            ctx.res().setStatus(201);
            ctx.json(createdTrip);
        } catch (ApiException e) {
            throw new ApiException(e.getStatusCode(), e.getMessage());
        } catch (InvalidFormatException | JsonParseException | ValidationException e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO jsonRequest = ctx.bodyAsClass(TripDTO.class);
            TripDTO updatedTrip = dao.update(id, jsonRequest);
            ctx.res().setStatus(201);
            ctx.json(updatedTrip);
        } catch (ApiException e) {
            throw new ApiException(e.getStatusCode(), e.getMessage());
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Trip with id: " + ctx.pathParam("id") + " not found");
        }
    }

    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.delete(id);
            ctx.res().setStatus(204);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Trip with id: " + ctx.pathParam("id") + " not found");
        }
    }

    public void addExistingGuideToExistingTrip(Context ctx) {
        try {
            int tripId = Integer.parseInt(ctx.pathParam("tripId"));
            int guideId = Integer.parseInt(ctx.pathParam("guideId"));
            dao.addGuideToTrip(tripId, guideId);
            ctx.res().setStatus(200);
            ctx.json("Guide with id: " + guideId + " added to Trip with id: " + tripId);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("tripId") + " or " + ctx.pathParam("guideId"));
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Trip with id: " + ctx.pathParam("tripId") + " or Guide with id: " + ctx.pathParam("guideId") + " not found");
        }
    }

    public void getTripsByGuideId(Context ctx) {
        try {
            int guideId = Integer.parseInt(ctx.pathParam("guideId"));
            Set<TripDTO> trips = dao.getTripsByGuideId(guideId);
            ctx.res().setStatus(200);
            ctx.json(trips);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("guideId"));
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Guide with id: " + ctx.pathParam("guideId") + " not found");
        }
    }

    //Populate the database using the static *populate* method in the Populator class
    public void populate(Context ctx) {
        Populator.populate();
        ctx.res().setStatus(200);
        ctx.json("Database populated");
    }

    //Filter trips by category using streams and not dao methods
    public void filterTripsByCategory(Context ctx) {
        try {
            String categoryParam = ctx.queryParam("category");
            if (categoryParam == null || categoryParam.isEmpty()) {
                throw new ApiException(400, "Category query parameter is missing");
            }

            Trip.TripCategory category = Trip.TripCategory.valueOf(categoryParam);
            List<TripDTO> trips = dao.readAll();
            List<TripDTO> filteredTrips = trips.stream()
                    .filter(trip -> trip.getCategory().equals(category))
                    .toList();

            if (filteredTrips.isEmpty()) {
                throw new ApiException(404, "No trips found with category: " + category);
            }

            ctx.res().setStatus(200);
            ctx.json(filteredTrips);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid category: " + ctx.queryParam("category"));
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, e.getMessage());
        }
    }

    public void getPackingItemsWeightSum(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO tripDTO = dao.readById(id);

            // Fetch packing items based on the trip's category
            if (tripDTO.getCategory() != null) {
                PackingListDTO packingList = dat.services.PackingService.getPackingItemsByCategory(tripDTO.getCategory().name());

                // Calculate the total weight
                int totalWeight = packingList.getItems().stream()
                        .mapToInt(PackingItemDTO::getWeightInGrams)
                        .sum();

                // Return the total weight as JSON
                ctx.res().setStatus(200);
                ctx.json(Map.of("totalWeightInGrams", totalWeight));
            } else {
                throw new ApiException(400, "Trip category is missing or invalid for trip with id: " + id);
            }
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (NoResultException e) {
            throw new ApiException(404, "Trip with id: " + ctx.pathParam("id") + " not found");
        }
    }
}