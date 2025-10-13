package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Working Autonomous", group = "Robot")

public class RedAutoOpMode extends BaseAutoOpMode {
    
    public void retractSlide() {
        int currentSlide = slideMotor.getCurrentPosition();
        
        // true while NOT retracted
        int targetRetract = 0;
        while (slideSensor.getState()) {
            // Set the shoulder power and target position
            slideMotor.setPower(0.45); // 0.2 - 0.4 is good
            slideMotor.setTargetPosition(targetRetract);
            if (slideMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
               slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
        
            targetRetract -= 2;
        }
        
        telemetry.addData("slide", "Retracted");
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setTargetPosition(0);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    
    public void adjustArm(int targetArm, int targetSlide) {
        int currentArm = armMotor.getCurrentPosition();
        int currentSlide = slideMotor.getCurrentPosition();
        boolean isSlideRetracted = !slideSensor.getState();

        telemetry.addData("targetArm", "%d", targetArm);
        telemetry.addData("targetSlide", "%d", targetSlide);
        
        // Set the shoulder power and target position
        armMotor.setPower(1.0); // 0.2 - 0.4 is good WITHOUT INTAKE.  Needs 1.0 with intake
        armMotor.setTargetPosition(targetArm);
        if (armMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
           armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        // Set the shoulder power and target position
        slideMotor.setPower(0.85); // 0.2 - 0.4 is good
        slideMotor.setTargetPosition(targetSlide);
        if (slideMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
           slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        while (opModeIsActive() && currentArm < targetArm && targetSlide < currentSlide) {
            telemetry.addData("currentArm", "%d", currentArm);
            telemetry.addData("currentSlide", "%d", currentSlide);
            sleep(50);
            currentArm = armMotor.getCurrentPosition();
            currentSlide = slideMotor.getCurrentPosition();
        }

    }
    
    

    @Override
    public void runOpMode() {
        // initialize robot and capture "before randomization" photo
        super.runOpMode();

        retractSlide();
        
        adjustArm(0,0);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
      
        adjustArm(100, 100);
        // capture "after randomization" photo and find spike
        // int spikeLocation = findSpikeLocation();
        //int spikeLocation = findSpikeLocation(); // 0=left 1=center 2=right
        sleep(1000);

        // TEST the arm (shoulder,slide)

        // run

        // Step 1: Drive forward x inches
        strafeRightInches(DRIVE_SPEED*0.8,11,2);
        driveForwardInches(DRIVE_SPEED*1.2,5, 1.5);
        
      
      
        //turn 135 left
        //turnLeftDegrees(DRIVE_SPEED*2, 90, .9);
        //driveForwardInches(DRIVE_SPEED*2, 4,0.9);
        turnLeftDegrees(DRIVE_SPEED*1.2, 45, 0.45);
        //lift shoulder 
        //driveForwardInches(DRIVE_SPEED*2, 2.75,1);
        //turns hand out 
         wristServo.setPosition(0.0);
        //extends arm 
        
        //gear broke when new gear added make first value (armValue) = 3390
        adjustArm(3440, 300);
        sleep(700);
        adjustArm(3440, 2850);
        sleep(750);
        driveForwardInches(DRIVE_SPEED*1.2, 4.1, 1);


        //extend the slide
        //drop sample 
       
        sleep(250);
        intakeServo.setPosition(0.0);
        sleep(375);
        intakeServo.setPosition(1.0);
        sleep(375);
        intakeServo.setPosition(0.0);
        sleep(375);
        driveForwardInches(DRIVE_SPEED*1.25, -6,1.5);
        sleep(100);
        slideMotor.setPower(1);
        adjustArm(3440,0);
        sleep(500);
        armMotor.setPower(1.25);
        //driveForwardInches(DRIVE_SPEED, -8, 8);
        adjustArm(0,0);
        armMotor.setPower(1);
        turnRightDegrees(DRIVE_SPEED,45,0.5);
        //driveForwardInches(DRIVE_SPEED,6,3);
        //strafeRightInches(DRIVE_SPEED,6,3);
        
        

        telemetry.addData("Path", "Complete");
        telemetry.update();
        adjustArm(0,0);
        
        sleep(2000); // pause to display final telemetry message.
        
        //holdUpSlideServo.setPosition(holdUpLocked);
    }
}
