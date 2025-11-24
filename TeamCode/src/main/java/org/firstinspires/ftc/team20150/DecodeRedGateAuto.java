package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Decode Red Gate Auto", group = "Robot")
public class DecodeRedGateAuto extends DecodeGateAuto{
    @Override
    protected void leave(double speed, double distance) {
        strafeRightInches(speed, distance,10);
    }
}
