package com.callicoder.goparking.handler;

import com.callicoder.goparking.exceptions.ParkingLotFullException;
import com.callicoder.goparking.exceptions.SlotNotFoundException;
import com.callicoder.goparking.domain.Car;
import com.callicoder.goparking.domain.ParkingLot;
import com.callicoder.goparking.domain.ParkingSlot;
import com.callicoder.goparking.domain.Ticket;
import com.callicoder.goparking.utils.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.callicoder.goparking.utils.MessageConstants.*;

public class ParkingLotCommandHandler {
    private ParkingLot parkingLot;

    public void createParkingLot(int numSlots) {
        if (isParkingLotCreated()) {
            System.out.println(PARKING_LOT_ALREADY_CREATED);
            return;
        }

        try {
            parkingLot = new ParkingLot(numSlots);
            System.out.println(String.format(PARKING_LOT_CREATED_MSG, parkingLot.getNumSlots()));
        } catch (IllegalArgumentException ex) {
            System.out.println("Bad input: " + ex.getMessage());
        }
    }

    public void park(String registrationNumber, String color) {
        if (!isParkingLotCreated()) {
            System.out.println(PARKING_LOT_NOT_CREATED);
            return;
        }

        if (parkingLot.getRegistrationNumberSlotMap() != null && parkingLot.getRegistrationNumberSlotMap().containsKey(registrationNumber)) {
            System.out.println(DUPLICATE_VEHICLE_MESSAGE);
            return;
        }

        try {
            Car car = new Car(registrationNumber, color);
            Ticket ticket = parkingLot.reserveSlot(car);
            System.out.println(String.format(PARKING_SLOT_ALLOCATED_MSG, ticket.getSlotNumber()));
        } catch (IllegalArgumentException ex) {
            System.out.println("Bad input: " + ex.getMessage());
        } catch (ParkingLotFullException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void status() {
        if (!isParkingLotCreated()) {
            System.out.println(PARKING_LOT_NOT_CREATED);
            return;
        }

        System.out.println(SLOT_NO + "    " + REGISTRATION_NO + "    " + Color);
        parkingLot.getOccupiedSlots().forEach(parkingSlot -> {
            System.out.println(
                    StringUtils.rightPadSpaces(Integer.toString(parkingSlot.getSlotNumber()), SLOT_NO.length()) + "    " +
                            StringUtils.rightPadSpaces(parkingSlot.getCar().getRegistrationNumber(), REGISTRATION_NO.length()) + "    " +
                            parkingSlot.getCar().getColor());
        });
    }

    public void leave(int slotNumber) {
        try {
            parkingLot.leaveSlot(slotNumber);
            System.out.println(String.format("Slot number %s is free", slotNumber));
        } catch (SlotNotFoundException snfe) {
            System.out.println("Slot number " + slotNumber + " not found!");
        }
    }

    public void getRegistrationNumbersByColor(String color) {
        System.out.println();
        parkingLot.getRegistrationNumbersByColor(color).forEach(s -> {
            System.out.print(s.trim() + ", ");
        });
    }

    public void getSlotNumbersByColor(String color) {
        System.out.println();
        parkingLot.getSlotNumbersByColor(color).forEach(s -> {
            System.out.print(s + ", ");
        });
        System.out.println();
    }

    public void getSlotNumberByRegistrationNumber(String registrationNumber) {
        Optional<Integer> optional = parkingLot.getSlotNumberByRegistrationNumber(registrationNumber);
        System.out.println(optional.isPresent() ? optional.get() : "Not found");
    }

    private boolean isParkingLotCreated() {
        if (parkingLot == null) {
            return false;
        }
        return true;
    }
}
