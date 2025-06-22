package org.pancakelab.model.orders;

import org.pancakelab.exception.ValidationException;

/**
 * Validates the order details such as building and room numbers.
 * Ensures that the provided building and room numbers are within the configured valid ranges.
 */
public class OrderValidator {

    private final OrderValidatorConfig config;


    public OrderValidator(OrderValidatorConfig config) {
        this.config = config;
    }

    private static final String INVALID_ROOM_NUMBER_MSG_TEMPLATE = "Room number must be between %d and %d.";
    private static final String INVALID_BUILDING_NUMBER_MSG_TEMPLATE = "Building number must be between %d and %d.";

    public void validate(int building, int room) {
        if (building < config.getMinBuildingNumber() || building > config.getMaxBuildingNumber()) {
            throw new ValidationException(String.format(INVALID_BUILDING_NUMBER_MSG_TEMPLATE,
                    config.getMinBuildingNumber(),
                    config.getMaxBuildingNumber()
            ));
        }
        if (room < config.getMinRoomNumber() || room > config.getMaxRoomNumber()) {
            throw new ValidationException(String.format(INVALID_ROOM_NUMBER_MSG_TEMPLATE,
                    config.getMinRoomNumber(),
                    config.getMaxRoomNumber()
            ));
        }
    }
}
