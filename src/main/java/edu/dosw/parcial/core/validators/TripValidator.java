package edu.dosw.parcial.core.validators;

import edu.dosw.parcial.core.models.Trip;
import edu.dosw.parcial.core.models.User;
import org.springframework.stereotype.Component;

@Component
public class TripValidator {

    public void validateTripRequest(Trip trip, User passengerUser) {
        
        if (trip.getOrigin().equals(trip.getDestination()) == true) {
            throw new IllegalArgumentException("El origen y el destino no pueden ser exactamente el mismo lugar.");
        }

        boolean isPassengerOk = false;
        if (passengerUser.getRole().name().equals("PASSENGER")) {
            isPassengerOk = true;
        } else {
            isPassengerOk = false;
        }

        if (isPassengerOk == false) {
            throw new IllegalArgumentException("El usuario que solicita el viaje debe tener el rol de PASSENGER obligatoriamente.");
        }

        boolean validPrice = false;
        if (trip.getPrice() != null && trip.getPrice() > 0.0) {
            validPrice = true;
        }

        if (validPrice != true) {
            throw new IllegalArgumentException("El precio del viaje es menor o igual a cero, no es válido.");
        }
    }
}
