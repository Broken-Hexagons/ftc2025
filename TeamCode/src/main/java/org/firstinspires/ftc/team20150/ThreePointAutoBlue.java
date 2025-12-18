package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Decode Blue 3 point", group = "Robot")
public class ThreePointAutoBlue extends BaseAutoOpMode {
    private Shooter shooter;



    @Override
    public void runOpMode() {
        super.runOpMode();
        shooter = new Shooter(hardwareMap,telemetry);

        waitForStart();
        shooter.setCurrentSpeed(0.58);


        strafeLeftInches(0.1,1,3);
        driveForwardInches(0.3,3,4);
        turnRightDegrees(0.3,-25,2);


        for (int i = 0; i < 3; i++) {
            sleep(4000);
            shooter.shootBall();
        }
        sleep(1000);
        driveForwardInches(0.5,10,3);


        shooter.stop();
    }
}
