package com.callicoder.goparking.interaction.commands;
/* 
Created by amit.chaurasia 
on 12/17/20 
*/

import com.callicoder.goparking.exceptions.InvalidParameterException;
import com.callicoder.goparking.handler.ParkingLotCommandHandler;

public class SlotNumberForRegistrationCommand implements Command {
    private ParkingLotCommandHandler parkingLotCommandHandler;

    public SlotNumberForRegistrationCommand(ParkingLotCommandHandler parkingLotCommandHandler) {
        this.parkingLotCommandHandler = parkingLotCommandHandler;
    }

    @Override
    public String helpText() {
        return "slot_number_for_registration_number <registrationNumber>";
    }

    @Override
    public void execute(String[] params) throws InvalidParameterException {
        if (params.length < 1) {
            throw new InvalidParameterException("Expected registration Number");
        }

        parkingLotCommandHandler.getSlotNumberByRegistrationNumber(params[0]);
    }
}
