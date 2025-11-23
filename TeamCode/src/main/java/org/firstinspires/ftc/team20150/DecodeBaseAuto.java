package org.firstinspires.ftc.team20150;

public abstract class DecodeBaseAuto extends BaseAutoOpMode {
    private Shooter shooter;

    protected abstract void leave(double speed, double distance);
    protected abstract void firstMove(double speed, double distance);
    protected abstract void turn(double speed, double degrees);

    @Override
    public void runOpMode() {
        super.runOpMode();
        shooter = new Shooter(hardwareMap,telemetry);

        waitForStart();
        shooter.setCurrentSpeed(0.45);

        firstMove(0.5,5);

        driveForwardInches(0.5, 55, 10);

        turn(0.5,45);

        for (int i = 0; i < 3; i++) {
            sleep(4000);
            shooter.shootBall();
        }
        sleep(1000);
        leave(0.5,15);

        shooter.stop();
    }
}
