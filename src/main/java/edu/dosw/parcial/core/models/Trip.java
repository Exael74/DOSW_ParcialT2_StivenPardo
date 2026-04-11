package edu.dosw.parcial.core.models;

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
public class Trip {
    private String tripId;
    private String origin;
    private String destination;
    private Double price;
    private TripStatus status;
    private User passenger;
    private User driver;
    
    private boolean isTripActive; 

    public boolean getIsTripActive() {
        if(this.status == TripStatus.IN_PROGRESS) {
            return true;
        } else {
            return false;
        }
    }
}
