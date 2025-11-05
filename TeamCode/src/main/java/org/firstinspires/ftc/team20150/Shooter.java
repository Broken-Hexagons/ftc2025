package org.firstinspires.ftc.team20150;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {
    enum SpeedMode {
        FLEX(3, "FLEX", -1),
        DUNK(4, "Dunk mode", 0.45),
        FREETHROW(5, "Free throw mode", 0.56),
        THREEPOINTER(6, "Three pointer mode", 0.63);
        private int mode;
        private String name;
        private double speed;
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

    private void update(){

        if(currentMode == SpeedMode.FLEX){
            currentSpeed = newSpeed;
        }else{
            currentSpeed = currentMode.speed;
        }
        flyWheel.setPower(currentSpeed);
    }
    /**
     * Sets the shooter's flywheel speed
     * @param speed - speed
     */
    public void setCurrentSpeed(double speed){
        newSpeed = clampFlywheelSpeed(speed);
        currentMode = SpeedMode.FLEX;
        update();
    }
    /**
     * Increases flywheel speed by specific increment.
     */
    public void increaseFlywheelSpeed(){
        newSpeed = clampFlywheelSpeed(currentSpeed + speedIncrement);
        currentMode = SpeedMode.FLEX;
        update();

    }
    /**
     * Decreases flywheel speed by specific increment.
     */
    public void decreaseFlywheelSpeed(){
        newSpeed = clampFlywheelSpeed(currentSpeed - speedIncrement);
        currentMode = SpeedMode.FLEX;
        update();
    }


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
            sleep(500);
        } catch (InterruptedException ignored) {}

        telemetry.speak(currentMode.name);
    }

    private double clampFlywheelSpeed(double speed){
        if(speed > 0.8)
            return 0.8;
        if(speed < 0)
            return 0;
        return speed;
    }

    public void shootBall(){
        openGatekeeper();
        try {
            sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        closeGatekeeper();
    }
    /**
     * Opens gate to release the artifacts for shooting.
     */
    private void openGatekeeper(){
        gateKeeper.setPosition(0.3);
    }

    /**
     * Closes gate to store Artifacts
     */
    private   void closeGatekeeper(){
        gateKeeper.setPosition(1.0);
    }

    public void updateTelemetry(){
        telemetry.addData(flywheelCaption+".Speed", currentSpeed);
        telemetry.addData(flywheelCaption+".Mode", currentMode);
        telemetry.addData(gatekeeperCaption, isGateOpened ? "Opened":"Closed");
    }

    /**
     * Stops the Shooter
     */
    public void stop(){
        currentMode = SpeedMode.FLEX;
        newSpeed = 0;
        closeGatekeeper();
    }
}
