package dat.routes;

import dat.controllers.impl.TripController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TripRoutes {
    private final TripController tripController = new TripController();
    protected EndpointGroup getRoutes() {
        return () -> {
            get("/", tripController::readAll, Role.ANYONE);
            get("/categories", tripController::filterTripsByCategory, Role.ANYONE);
            get("/{id}/packing-weight", tripController::getPackingItemsWeightSum, Role.ANYONE);
            get("/tripsByGuide/{guideId}", tripController::getTripsByGuideId, Role.ANYONE);

            get("/{id}", tripController::readById, Role.ANYONE);

            post("/", tripController::create, Role.ANYONE);
            post("/populate", tripController::populate, Role.ANYONE);

            put("/{id}", tripController::update, Role.ANYONE);
            put("/{tripId}/guides/{guideId}", tripController::addExistingGuideToExistingTrip, Role.ANYONE);

            delete("/{id}", tripController::delete, Role.ANYONE);
        };
    }
}