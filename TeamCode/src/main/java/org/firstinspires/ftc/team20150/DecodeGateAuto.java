package org.firstinspires.ftc.team20150;

/**
 * Abstract base class for an autonomous routine that involves decoding a gate,
 * shooting, and then leaving the area. This class provides the core logic
 * for shooting three times and then executing a "leave" maneuver. The specifics
 * of the "leave" maneuver are defined by subclasses.
 */
public abstract class DecodeGateAuto extends BaseAutoOpMode {
    // The shooter mechanism, used for launching artifacts.
    private Shooter shooter;

    /**
     * Abstract method to define how the robot leaves the starting area after shooting.
     * Subclasses must implement this to provide a specific path.
     * @param speed The speed at which the robot should move.
     * @param distance The distance the robot should travel.
     */
    protected abstract void leave(double speed, double distance);

    /**
     * The main execution method for the autonomous mode.
     * This method is called once when the "INIT" button is pressed.
     */
    @Override
    public void runOpMode() {
        // Initialize the hardware from the base class (e.g., drive motors) and the shooter.
        super.runOpMode();
        shooter = new Shooter(hardwareMap, telemetry);

        // Wait for the driver to press the "PLAY" button.
        waitForStart();

        // Set the shooter flywheel to a predefined speed (0.56, likely for free throws).
        shooter.setCurrentSpeed(0.47);

        // Drive backward 56.5 inches to position the robot for shooting.
        driveForwardInches(0.5, -30, 10); // 10-second timeout

        // Loop to shoot three balls.
        for (int i = 0; i < 3; i++) {
            // Wait for 4 seconds, likely for the flywheel to reach and stabilize its target speed.
            sleep(4000);
            // Execute the shooting sequence (open and close the gatekeeper servo).
            shooter.shootBall();
        }
        // Wait for a second after the last shot.
        sleep(1000);

        // Execute the abstract "leave" method, which is implemented by the concrete subclass.
        // In this case, it moves 35 units at 0.5 speed.
        leave(0.5, 35);

        // Stop the shooter's flywheel and reset its state before the op-mode ends.
        shooter.stop();
    }
}
