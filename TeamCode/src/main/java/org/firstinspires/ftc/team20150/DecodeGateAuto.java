package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Decode Gate Auto", group = "Robot")
public class DecodeGateAuto extends BaseAutoOpMode{
    private Shooter shooter;

    @Override
    public void runOpMode() {
        super.runOpMode();
        shooter =  new Shooter(hardwareMap,telemetry);

        waitForStart();
        shooter.setCurrentSpeed(0.6);

        driveForwardInches(1,-30,10);

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
