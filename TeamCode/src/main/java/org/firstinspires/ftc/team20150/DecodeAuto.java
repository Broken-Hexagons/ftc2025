package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Decode Autonomous", group = "Robot")
public class DecodeAuto extends BaseAutoOpMode {
    @Override
    public void runOpMode() {
        super.runOpMode();
        waitForStart();
        driveForwardInches(0.5, 50, 10);
    }
}
