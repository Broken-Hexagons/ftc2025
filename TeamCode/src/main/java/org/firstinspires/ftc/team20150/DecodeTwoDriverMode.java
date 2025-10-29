package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Decode 2 Driver TeleOp")
public class DecodeTwoDriverMode extends BaseTeleOpMode {

    private final String robotCaption = "Robot";
    private Shooter shooter;

    @Override
    public void init(){
        super.init();
        shooter = new Shooter();
        shooter.init(hardwareMap,telemetry);
        telemetry.addData(robotCaption,"Happy");
    }

    @Override
    public void loop(){
        super.loop();
        if(gamepad2.a){
            shooter.openGatekeeper();
        }else{
            shooter.closeGatekeeper();
        }

        if(gamepad2.dpadUpWasPressed()){
            shooter.increaseFlywheelSpeed();
        }
        if (gamepad2.dpadDownWasPressed()){
            shooter.decreaseFlywheelSpeed();
        }
    }

    @Override
    public void stop() {
        super.stop();
        shooter.stop();
        telemetry.addData(robotCaption, "Stopped.");
    }
}
