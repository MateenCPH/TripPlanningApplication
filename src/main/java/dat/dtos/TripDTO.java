package dat.dtos;

import dat.entities.Trip;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TripDTO {

    private Integer id;
    private String name;
    private double price;
    private Trip.TripCategory category;
    private String startTime;
    private String endTime;
    private long longitude;
    private long latitude;
    private GuideDTO guide;

    private List<PackingItemDTO> packingItems;

    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.price = trip.getPrice();
        this.category = trip.getCategory();
        this.startTime = trip.getStartTime();
        this.endTime = trip.getEndTime();
        this.longitude = trip.getLongitude();
        this.latitude = trip.getLatitude();
        this.guide = trip.getGuide() != null ? new GuideDTO(trip.getGuide()) : null; // Initialize guide if available

    }

    public TripDTO(String name, double price, Trip.TripCategory category, String startTime, String endTime, long longitude, long latitude) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.startTime = startTime;
        this.endTime = endTime;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripDTO tripDTO)) return false;
        return Double.compare(price, tripDTO.price) == 0 && longitude == tripDTO.longitude && latitude == tripDTO.latitude && Objects.equals(id, tripDTO.id) && Objects.equals(name, tripDTO.name) && category == tripDTO.category && Objects.equals(startTime, tripDTO.startTime) && Objects.equals(endTime, tripDTO.endTime) && Objects.equals(guide, tripDTO.guide) && Objects.equals(packingItems, tripDTO.packingItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, category, startTime, endTime, longitude, latitude, guide, packingItems);
    }
}