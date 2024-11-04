package dat.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guide_id")
    private Integer id;
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "years_of_experience")
    private int yearsOfExperience;

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> trips;

    public Guide(String firstName, String lastName, String email, String phoneNumber, int yearsOfExperience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guide guide)) return false;
        return yearsOfExperience == guide.yearsOfExperience && Objects.equals(id, guide.id) && Objects.equals(firstName, guide.firstName) && Objects.equals(lastName, guide.lastName) && Objects.equals(email, guide.email) && Objects.equals(phoneNumber, guide.phoneNumber) && Objects.equals(trips, guide.trips);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phoneNumber, yearsOfExperience, trips);
    }
}