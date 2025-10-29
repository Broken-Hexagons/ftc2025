package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {
    private final String flywheelCaption = "Shooter.Flywheel";
    private final String gatekeeperCaption = "Shooter.Gatekeeper";
    private DcMotor flyWheel;
    private Servo gateKeeper;
    private double flywheelSpeed;

    private final double speedIncrement = 0.1;
    private final double maxSpeed = 0.8;
    private final double minSpeed = 0.1;


    private Telemetry telemetry;

    public void init(HardwareMap hardwareMap, Telemetry telemetry){
        // setup telemetry for logging.
        this.telemetry = telemetry;
        flywheelSpeed = 0.0;
        // setup hardware
        flyWheel = hardwareMap.get(DcMotor.class, "FLYWHEEL");
        //TODO Check direction
        flyWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flyWheel.setDirection(DcMotor.Direction.FORWARD);
        flyWheel.setPower(flywheelSpeed);
        telemetry.addData(flywheelCaption, "Happy");

        gateKeeper = hardwareMap.get(Servo.class,"GATEKEEPER");
        //TODO check direction and position
        gateKeeper.setDirection(Servo.Direction.FORWARD);
        gateKeeper.setPosition(0.0);
      //  gateKeeper.scaleRange(0.0,0.5);
        telemetry.addData(gatekeeperCaption, "Happy");
    }

    /**
     * Sets the shooter's flywheel speed
     * @param speed - speed
     */
    public void setFlywheelSpeed(double speed){
        flywheelSpeed = speed;
        flyWheel.setPower(flywheelSpeed);
        telemetry.addData(flywheelCaption,"Speed:%4.2f", flywheelSpeed);
    }

    /**
     * Increases flywheel speed by specific increment.
     */
    public void increaseFlywheelSpeed(){
        flywheelSpeed += speedIncrement;
        if(flywheelSpeed > maxSpeed)
            flywheelSpeed = maxSpeed;
        flyWheel.setPower(flywheelSpeed);
        telemetry.addData(flywheelCaption,"Speed:%4.2f", flywheelSpeed);
    }

    /**
     * Decreases flywheel speed by specific increment.
     */
    public void decreaseFlywheelSpeed(){
        flywheelSpeed -= speedIncrement;
        if(flywheelSpeed < minSpeed)
            flywheelSpeed = minSpeed;
        flyWheel.setPower(flywheelSpeed);
        telemetry.addData(flywheelCaption,"Speed:%4.2f", flywheelSpeed);
    }
    /**
     * Opens gate to release the artifacts for shooting.
     */
    public void openGatekeeper(){
        //TODO check position
        gateKeeper.setPosition(1.0);
        telemetry.addData(gatekeeperCaption,"Opened");
    }

    /**
     * Closes gate to store Artifacts
     */
    public  void closeGatekeeper(){
        //TODO check position
        gateKeeper.setPosition(0.0);
        telemetry.addData(gatekeeperCaption,"Closed");
    }

    /**
     * Stops the Shooter
     */
    public void stop(){
        flywheelSpeed = 0.0;
        flyWheel.setPower(flywheelSpeed);
        telemetry.addData(flywheelCaption,"Stopped");
    }
}
