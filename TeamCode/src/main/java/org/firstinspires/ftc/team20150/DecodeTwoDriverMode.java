package org.firstinspires.ftc.team20150;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "Decode 2 Driver TeleOp")
public class DecodeTwoDriverMode extends BaseTeleOpMode {

    private final String robotCaption = "Robot";
    private Shooter shooter;

    @Override
    public void init(){
        super.init();
        shooter = new Shooter(hardwareMap,telemetry);
        telemetry.addData(robotCaption,"Happy");
        telemetry.speak("Initialized");
    }

    @Override
    public void start() {
        super.start();
        shooter.nextFlywheelMode();
        telemetry.speak("Started");
    }

    @Override
    public void loop(){
        super.loop();

        //Emergency stop
        if(gamepad2.xWasPressed() || gamepad1.xWasPressed()){
            shooter.stop();
        }

        // Release artifact
        if(gamepad2.aWasPressed()){
            shooter.shootBall();
        }

        if(gamepad2.right_bumper) {
            // Control Shooter speed by increment
            if (gamepad2.dpadUpWasPressed()) {
                shooter.increaseFlywheelSpeed();
            }
            if (gamepad2.dpadDownWasPressed()) {
                shooter.decreaseFlywheelSpeed();
            }
        }else{
            //Control Shooter speed using presets
            if (gamepad2.dpadUpWasPressed()) {
                shooter.nextFlywheelMode();
            }
            if (gamepad2.dpadDownWasPressed()) {
                shooter.previousFlywheelMode();
            }
        }
        shooter.updateTelemetry();
        telemetry.update();
    }

    @Override
    public void stop() {
        super.stop();
        shooter.stop();
        telemetry.addData(robotCaption, "Stopped.");
    }
}
