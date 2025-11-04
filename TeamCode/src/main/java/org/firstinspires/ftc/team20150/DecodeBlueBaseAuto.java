package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Decode Blue Base Auto", group = "Robot")
public class DecodeBlueBaseAuto extends BaseAutoOpMode {

    private Shooter shooter;

    @Override
    public void runOpMode() {
        super.runOpMode();
        shooter = new Shooter(hardwareMap,telemetry);

        waitForStart();
        shooter.setCurrentSpeed(0.5);

        driveForwardInches(1, 40, 10);
        turnLeftDegrees(0.5,45,3);
        sleep(4000);
        shooter.openGatekeeper();
        sleep(300);
        shooter.closeGatekeeper();

        sleep(3000);
        shooter.openGatekeeper();
        sleep(300);
        shooter.closeGatekeeper();

        sleep(3000);
        shooter.openGatekeeper();
        sleep(300);
        shooter.closeGatekeeper();

        shooter.stop();
    }
}
