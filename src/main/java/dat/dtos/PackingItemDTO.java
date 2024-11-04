package dat.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackingItemDTO {
    private String name;
    private int weightInGrams;
    private int quantity;
    private String description;
    private String category;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime updatedAt;
    private List<BuyingOption> buyingOptions;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuyingOption {
        private String shopName;
        private String shopUrl;
        private double price;
    }
}