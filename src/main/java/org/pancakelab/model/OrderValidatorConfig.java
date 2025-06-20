package org.pancakelab.model;

/**
 * Configuration class for order validation.
 * Contains the valid ranges for building and room numbers.
 */
public class OrderValidatorConfig {
    private int minBuildingNumber;
    private int maxBuildingNumber;
    private int minRoomNumber;
    private int maxRoomNumber;

    private OrderValidatorConfig() {
    }

    public OrderValidatorConfig(int minBuildingNumber, int maxBuildingNumber, int minRoomNumber, int maxRoomNumber) {
        if (minBuildingNumber < 0 || maxBuildingNumber < 0 || minRoomNumber < 0 || maxRoomNumber < 0) {
            throw new IllegalArgumentException("Building and room numbers must be non-negative.");
        }
        this.minBuildingNumber = minBuildingNumber;
        this.maxBuildingNumber = maxBuildingNumber;
        this.minRoomNumber = minRoomNumber;
        this.maxRoomNumber = maxRoomNumber;
    }

    public int getMinBuildingNumber() {
        return minBuildingNumber;
    }

    public int getMaxBuildingNumber() {
        return maxBuildingNumber;
    }

    public int getMinRoomNumber() {
        return minRoomNumber;
    }

    public int getMaxRoomNumber() {
        return maxRoomNumber;
    }
}
