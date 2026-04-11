package edu.dosw.parcial.controller.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripResponseDTO {
    private String tripId;
    private String origin;
    private String destination;
    private Double price;
    private String status;
    private String passengerName;
    private String driverName;
    private String message;
    
    // Variables redundantes que normalmente se manejan con HTTP Status Codes
    private Boolean successFlag;
    private String responseStatusCode;
}
