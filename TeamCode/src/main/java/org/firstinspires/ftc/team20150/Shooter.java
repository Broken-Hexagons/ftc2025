package org.firstinspires.ftc.team20150;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Represents the shooter mechanism of the robot.
 * This class controls the flywheel for shooting and the gatekeeper servo for releasing the ball.
 * It includes different speed modes for various shooting distances.
 */
public class Shooter {
    /**
     * Defines the speed modes for the shooter's flywheel.
     * Each mode has a predefined speed for different shooting scenarios like dunking or a three-pointer.
     * FLEX mode allows for manual speed adjustments.
     */
    enum SpeedMode {
        FLEX(3, "FLEX", -1),
        DUNK(4, "Dunk mode", 0.45),
        FREETHROW(5, "Free throw mode", 0.55),
        THREEPOINTER(6, "Three pointer mode", 0.63);
        private int mode;
        private String name;
        private double speed;

        /**
         * Constructor for SpeedMode enum.
         * @param mode The integer representation of the mode.
         * @param name The descriptive name of the mode.
         * @param speed The flywheel power for this mode.
         */
        private SpeedMode(int mode, String name, double speed){
            this.mode = mode;
            this.name = name;
            this.speed = speed;
        }
    }
    private final String flywheelCaption = "Shooter.Flywheel";
    private final String gatekeeperCaption = "Shooter.Gatekeeper";
    private final DcMotor flyWheel;
    private final Servo gateKeeper;
    private final Telemetry telemetry;

    private double currentSpeed;
    private double newSpeed;

    private final double speedIncrement = 0.01;
    private SpeedMode currentMode;
    private boolean isGateOpened = false;

    /**
     * Initializes the Shooter hardware and sets initial values.
     * @param hardwareMap The hardware map from the robot's configuration.
     * @param telemetry The telemetry object for displaying data on the driver station.
     */
    public Shooter(HardwareMap hardwareMap, Telemetry telemetry){
        this.telemetry = telemetry;
        newSpeed = 0.0;
        currentSpeed = 0.0;
        currentMode = SpeedMode.FLEX;
        // setup hardware
        flyWheel = hardwareMap.get(DcMotor.class, "FLYWHEEL");
        flyWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flyWheel.setDirection(DcMotor.Direction.FORWARD);
        flyWheel.setPower(currentSpeed);
        telemetry.addData(flywheelCaption, "Happy");

        gateKeeper = hardwareMap.get(Servo.class,"GATEKEEPER");
        gateKeeper.setDirection(Servo.Direction.FORWARD);
        closeGatekeeper();

        telemetry.addData(gatekeeperCaption, "Happy");
    }

    /**
     * Updates the flywheel's power based on the current mode or manually set speed.
     */
    private void update(){
        if(currentMode == SpeedMode.FLEX){
            currentSpeed = newSpeed;
        }else{
            currentSpeed = currentMode.speed;
        }
        flyWheel.setPower(currentSpeed);
    }
    /**
     * Sets the shooter's flywheel speed manually and switches to FLEX mode.
     * @param speed The desired speed, which will be clamped between 0 and 0.8.
     */
    public void setCurrentSpeed(double speed){
        newSpeed = clampFlywheelSpeed(speed);
        currentMode = SpeedMode.FLEX;
        update();
    }
    /**
     * Increases flywheel speed by a specific increment and switches to FLEX mode.
     */
    public void increaseFlywheelSpeed(){
        newSpeed = clampFlywheelSpeed(currentSpeed + speedIncrement);
        currentMode = SpeedMode.FLEX;
        update();

    }
    /**
     * Decreases flywheel speed by a specific increment and switches to FLEX mode.
     */
    public void decreaseFlywheelSpeed(){
        newSpeed = clampFlywheelSpeed(currentSpeed - speedIncrement);
        currentMode = SpeedMode.FLEX;
        update();
    }


    /**
     * Cycles to the next more powerful flywheel speed mode.
     * If in FLEX mode, it will select a mode based on the current speed.
     */
    public void nextFlywheelMode(){
        switch (currentMode){
            case DUNK:
                currentMode = SpeedMode.FREETHROW;
                break;
            case FREETHROW:
            case THREEPOINTER:
                currentMode = SpeedMode.THREEPOINTER;
                break;
        }

        // Logic to switch from FLEX to a preset mode
        if(currentMode == SpeedMode.FLEX){
            if(currentSpeed <= SpeedMode.DUNK.speed){
                currentMode = SpeedMode.DUNK;
            }else if(currentSpeed <= SpeedMode.FREETHROW.speed){
                currentMode = SpeedMode.FREETHROW;
            }else if(currentSpeed <= SpeedMode.THREEPOINTER.speed){
                currentMode = SpeedMode.THREEPOINTER;
            }
        }
        telemetry.speak(currentMode.name);
        update();
    }

    /**
     * Cycles to the next less powerful flywheel speed mode.
     * If in FLEX mode, it will select a mode based on the current speed.
     * Includes a delay after changing modes.
     */
    public void previousFlywheelMode(){
        switch (currentMode){
            case THREEPOINTER:
                currentMode = SpeedMode.FREETHROW;
                flyWheel.setPower(0);
                break;
            case FREETHROW:
                currentMode = SpeedMode.DUNK;
                flyWheel.setPower(0);
                break;
            case DUNK:
                break;
        }

        // Logic to switch from FLEX to a preset mode
        if(currentMode == SpeedMode.FLEX){
            if(currentSpeed > SpeedMode.THREEPOINTER.speed){
                currentMode = SpeedMode.THREEPOINTER;
                flyWheel.setPower(0);
            }else if(currentSpeed > SpeedMode.FREETHROW.speed){
                currentMode = SpeedMode.FREETHROW;
                flyWheel.setPower(0);
            }else if(currentSpeed > SpeedMode.DUNK.speed){
                currentMode = SpeedMode.DUNK;
                flyWheel.setPower(0);
            }
        }

        try {
            // Pause to allow the flywheel to spin down slightly
            sleep(500);
        } catch (InterruptedException ignored) {}

        telemetry.speak(currentMode.name);
        update();
    }

    /**
     * Clamps the flywheel speed to a safe operating range.
     * @param speed The speed to clamp.
     * @return The clamped speed, between 0.0 and 0.8.
     */
    private double clampFlywheelSpeed(double speed){
        if(speed > 0.8)
            return 0.8;
        if(speed < 0)
            return 0;
        return speed;
    }

    /**
     * Executes the shooting sequence: opens and then closes the gatekeeper.
     */
    public void shootBall(){
        openGatekeeper();
        try {
            sleep(300); // Wait for the ball to pass through
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        closeGatekeeper();
    }
    /**
     * Opens the gatekeeper servo to release the artifacts for shooting.
     */
    private void openGatekeeper(){
        gateKeeper.setPosition(0.3);
        isGateOpened = true;
    }

    /**
     * Closes the gatekeeper servo to store or block artifacts.
     */
    private void closeGatekeeper(){
        gateKeeper.setPosition(1.0);
        isGateOpened = false;
    }

    /**
     * Updates telemetry data with the current state of the shooter.
     */
    public void updateTelemetry(){
        telemetry.addData(flywheelCaption+".Speed", currentSpeed);
        telemetry.addData(flywheelCaption+".Mode", currentMode);
        telemetry.addData(gatekeeperCaption, isGateOpened ? "Opened":"Closed");
    }

    /**
     * Stops the shooter mechanism and resets its state.
     */
    public void stop(){
        currentMode = SpeedMode.FLEX;
        newSpeed = 0;
        update(); // Sets flywheel power to 0
        closeGatekeeper();
    }
}
