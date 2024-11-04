package dat.dtos;

import dat.entities.Guide;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class GuideDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private int yearsOfExperience;

    public GuideDTO(Guide guide) {
        this.id = guide.getId();
        this.firstName = guide.getFirstName();
        this.lastName = guide.getLastName();
        this.email = guide.getEmail();
        this.phoneNumber = guide.getPhoneNumber();
        this.yearsOfExperience = guide.getYearsOfExperience();
    }

    public GuideDTO(String firstName, String lastName, String email, String phoneNumber, int yearsOfExperience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuideDTO guideDTO)) return false;
        return yearsOfExperience == guideDTO.yearsOfExperience && Objects.equals(id, guideDTO.id) && Objects.equals(firstName, guideDTO.firstName) && Objects.equals(lastName, guideDTO.lastName) && Objects.equals(email, guideDTO.email) && Objects.equals(phoneNumber, guideDTO.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phoneNumber, yearsOfExperience);
    }
}