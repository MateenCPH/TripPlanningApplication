package dat.daos;

import dat.dtos.TripDTO;

import java.util.Set;

public interface ITripGuideDAO<T, G> {
    void addGuideToTrip(T t, G g);
    Set<TripDTO> getTripsByGuideId(G g);
}