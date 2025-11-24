package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
@Autonomous(name = "Decode Blue Gate Auto")

public class DecodeBlueGateAuto extends DecodeGateAuto {
    @Override
    protected void leave(double speed, double distance) {
        strafeLeftInches(speed,distance,10);
    }
}
