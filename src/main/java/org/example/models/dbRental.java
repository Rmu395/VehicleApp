package org.example.models;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class dbRental {
    private String id;
    private String vehicleId;
    private String userId;
    private String rentDate;
    private String returnDate;
}