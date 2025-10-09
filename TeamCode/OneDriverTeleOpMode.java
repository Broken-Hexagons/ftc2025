package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "1 Driver TeleOp", group = "Iterative OpMode")

public class OneDriverTeleOpMode extends BaseTeleOpMode {

    @Override
    public void loop() {
        /*
        /intake
        if (gamepad1.dpad_up) {
            intakeMotor.setPower(1.0);
        } else if (gamepad1.dpad_down) {
            intakeMotor.setPower(-1.0);
        } else {
            intakeMotor.setPower(0.0);
        }
        */

        //purple pixel dropper
        if(gamepad1.dpad_right){
            //telemetry.addData("dpad right on");
           // purpleServo.setPosition(1.0);
        }
        else{
            //telemetry.addData("dpad right off");
           // purpleServo.setPosition(0);
        }

        // Save CPU resources; can resume streaming when needed.
        if (gamepad1.dpad_down) {
            //visionPortal.stopStreaming();
        } else if (gamepad1.dpad_up) {
            //visionPortal.resumeStreaming();
        }
        
        // arm control
        // if(gamepad1.dpad_right) {
        //     armMotor.setPower(0.5);
        // }
        // else if(gamepad1.dpad_left) {
        //     armMotor.setPower(-0.5);
        // }
        // else{
        //     armMotor.setPower(0);
        // }

        //airplane launcher
        //if(gamepad1.left_bumper) {
            
           // boxServo.setPosition(1);
        //}
        //else {
           // planeServo.setPosition(0);
       //}
        
        //box servo
        //if(gamepad1.left_bumper) {
         //   intakeServo.setPosition(1);
        //}
        //else {
        //    intakeServo.setPosition(0);
        //}

        super.loop();
    }
}
