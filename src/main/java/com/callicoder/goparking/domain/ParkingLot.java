package com.callicoder.goparking.domain;

import com.callicoder.goparking.exceptions.ParkingLotFullException;
import com.callicoder.goparking.exceptions.SlotNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class ParkingLot {
    private final int numSlots;
    private final int numFloors;
    private SortedSet<ParkingSlot> availableSlots = new TreeSet<>();
    private Set<ParkingSlot> occupiedSlots = new HashSet<>();
    private Map<String, ParkingSlot> registrationNumberSlotMap;
    private Map<String, Set<ParkingSlot>> colorSlotMap;

    public ParkingLot(int numSlots) {
        if(numSlots <= 0) {
            throw new IllegalArgumentException("Number of slots in the Parking Lot must be greater than zero.");
        }

        // Assuming Single floor since only numSlots are specified in the input.
        this.numSlots = numSlots;
        this.numFloors = 1;

        for(int i = 0; i < numSlots; i++) {
            ParkingSlot parkingSlot = new ParkingSlot(i+1, 1);
            this.availableSlots.add(parkingSlot);
        }
    }

    public synchronized Ticket reserveSlot(Car car) {
        if(car == null) {
            throw new IllegalArgumentException("Car must not be null");
        }

        if(this.isFull()) {
            throw new ParkingLotFullException();
        }

        ParkingSlot nearestSlot = this.availableSlots.first();

        nearestSlot.reserve(car);
        this.availableSlots.remove(nearestSlot);
        this.occupiedSlots.add(nearestSlot);

        if (registrationNumberSlotMap == null)
            registrationNumberSlotMap = new HashMap<>();
        registrationNumberSlotMap.put(car.getRegistrationNumber(), nearestSlot);

        if (colorSlotMap == null)
            colorSlotMap = new HashMap<>();
        Set colorSet = colorSlotMap.getOrDefault(car.getColor(), new HashSet<>());
        colorSet.add(nearestSlot);
        colorSlotMap.put(car.getColor(), colorSet);

        return new Ticket(nearestSlot.getSlotNumber(), car.getRegistrationNumber(), car.getColor());
    }

    public ParkingSlot leaveSlot(int slotNumber) {
        ParkingSlot slotToBeDeleted=new ParkingSlot(slotNumber,1);

        if(!occupiedSlots.remove(slotToBeDeleted))
            throw new SlotNotFoundException(slotNumber);
        else {
            availableSlots.add(slotToBeDeleted);
            return slotToBeDeleted;
        }
    }

    public boolean isFull() {
        return this.availableSlots.isEmpty();
    }

    public List<String> getRegistrationNumbersByColor(String color) {
        List<String> list = new ArrayList<>();
        if (colorSlotMap != null && colorSlotMap.containsKey(color))
            colorSlotMap.get(color).stream().forEach(s -> list.add(s.getCar().getRegistrationNumber()));
        return list;
    }

    public List<Integer> getSlotNumbersByColor(String color) {
        List<Integer> list = new ArrayList<>();
        if (colorSlotMap != null && colorSlotMap.containsKey(color))
            colorSlotMap.get(color).stream().forEach(s -> list.add(s.getSlotNumber()));
        return list;
    }

    public Optional<Integer> getSlotNumberByRegistrationNumber(String registrationNumber) {
        ParkingSlot parkingSlot=registrationNumberSlotMap.get(registrationNumber);
        if(parkingSlot!=null)
            return Optional.ofNullable(parkingSlot.getSlotNumber());
        else
            return Optional.empty();
    }


    public int getNumSlots() {
        return numSlots;
    }

    public int getNumFloors() {
        return numFloors;
    }

    public SortedSet<ParkingSlot> getAvailableSlots() {
        return availableSlots;
    }

    public Set<ParkingSlot> getOccupiedSlots() {
        return occupiedSlots;
    }

    public Map<String, ParkingSlot> getRegistrationNumberSlotMap() {
        return registrationNumberSlotMap;
    }
}



