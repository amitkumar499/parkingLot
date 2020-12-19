package com.callicoder.goparking.interaction.commands;
/* 
Created by amit.chaurasia 
on 12/17/20 
*/

import com.callicoder.goparking.exceptions.InvalidParameterException;
import com.callicoder.goparking.handler.ParkingLotCommandHandler;

public class RegistrationNumberCarColorCommand implements Command {
    private ParkingLotCommandHandler parkingLotCommandHandler;

    public RegistrationNumberCarColorCommand(ParkingLotCommandHandler parkingLotCommandHandler) {
        this.parkingLotCommandHandler = parkingLotCommandHandler;
    }

    @Override
    public String helpText() {
        return "slot_numbers_for_cars_with_colour <color>";
    }

    @Override
    public void execute(String[] params) throws InvalidParameterException {
        if(params.length < 1) {
            throw new InvalidParameterException("Expected color");
        }

        parkingLotCommandHandler.getRegistrationNumbersByColor(params[0]);
    }
}
