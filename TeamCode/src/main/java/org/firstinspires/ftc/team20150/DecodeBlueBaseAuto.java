package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Decode Blue Base Auto", group = "Robot")
public class DecodeBlueBaseAuto extends DecodeBaseAuto {

    private Shooter shooter;

    @Override
    protected void leave(double speed, double distance) {
        strafeLeftInches(speed,distance,10);
    }

    @Override
    protected void firstMove(double speed, double distance) {
        strafeLeftInches(speed,distance,4);
    }

    @Override
    protected void turn(double speed, double degrees) {
        turnLeftDegrees(speed,degrees,3);
    }
}
