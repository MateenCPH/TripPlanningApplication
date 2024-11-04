package dat.entities;

import dat.dtos.TripDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Integer id;

    private String name;

    private double price;

    @Enumerated(EnumType.STRING)
    private TripCategory category;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    private long longitude;

    private long latitude;

    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Guide guide;

    public Trip(String name, double price, TripCategory category, String startTime, String endTime, long longitude, long latitude) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.startTime = startTime;
        this.endTime = endTime;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Trip(TripDTO tripDTO) {
        this.id = tripDTO.getId();
        this.name = tripDTO.getName();
        this.price = tripDTO.getPrice();
        this.category = tripDTO.getCategory();
        this.startTime = tripDTO.getStartTime();
        this.endTime = tripDTO.getEndTime();
        this.longitude = tripDTO.getLongitude();
        this.latitude = tripDTO.getLatitude();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trip trip)) return false;
        return Double.compare(price, trip.price) == 0 && longitude == trip.longitude && latitude == trip.latitude && Objects.equals(id, trip.id) && Objects.equals(name, trip.name) && category == trip.category && Objects.equals(startTime, trip.startTime) && Objects.equals(endTime, trip.endTime) && Objects.equals(guide, trip.guide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, category, startTime, endTime, longitude, latitude, guide);
    }

    public enum TripCategory {
        beach, city, forest, lake, sea, snow;
    }
}