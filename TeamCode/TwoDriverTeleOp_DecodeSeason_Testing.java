package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "2 Driver Decode TeleOp", group = "Iterative OpMode")

public class TwoDriverTeleOp_DecodeSeason_Testing extends BaseTeleOpMode {
    
    //box servo control
    //double minBoxPosition = 0.25;
    //protected double minBoxPosition = 0.3;
    //protected double maxBoxPosition = 1.5;
    // Only positive n
    protected double minBoxPosition = 0.05;
    protected double maxBoxPosition = 1.25;

    // Arm starts in middle position, with wrist touching ground
    // -
    protected int lowestArmPosition = -300;
    protected int initialArmPosition = 0;
    protected int highestArmPosition = 450;
    
    // Position state
    //  1 - initial/tuck
    //  2 - small lift (above parked)
    //  3 - arm still up, scoop near
    //  4 - scoop mid
    //  5 - scoop far and arm flat (effectively no tension)
    //  6 - arm flat, elbow up (meant to lift over)
    /*
    protected int armState = 0;
    protected boolean changingState = false;
    final int armStateMin = 1;
    final int armStateMax = 9;
    
    int targetArm = 0;
    int targetSlide = 0;
    
    int armNudgeAmount = 0;
    
    double targetIntake = 0.0;
    */
    
    // When in 4-6 then allow slight servo movement
    /*

    public void checkEmergencyStop() {
        if (gamepad2.left_bumper && gamepad2.right_bumper && gamepad2.b) {
            
            // FLOAT: free-fall
            // BRAKE: stay as-is
            armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            armMotor.setPower(0);

            slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            slideMotor.setPower(0);
            
            intakeServo.setPosition(0.5); // STOP at 0.5
        }
    }
    
    public void checkRetract() {
        if (gamepad2.left_trigger != 0.0 && gamepad2.right_trigger != 0.0) {
            retractSlide();
        }
    }
    
    public void retractSlide() {
        int currentSlide = slideMotor.getCurrentPosition();
        
        // true while NOT retracted
        int targetRetract = 0;
        while (slideSensor.getState()) {
            // Set the shoulder power and target position
            slideMotor.setPower(0.55); // 0.2 - 0.4 is good
            slideMotor.setTargetPosition(targetRetract);
            if (slideMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
               slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
        
            targetRetract -= 2;
        }
        
        telemetry.addData("slide", "Retracted");
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        targetSlide = 0;
        slideMotor.setTargetPosition(0);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    
    public void checkResetShoulderToZeroAndStop() {
        if (gamepad2.right_bumper && gamepad2.x) {
            armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        }
    }
    
    
    public void doPresetPositions() {
        if (armState == 0){
            retractSlide();
        }
        
        int currentArm = armMotor.getCurrentPosition();
        int currentSlide = slideMotor.getCurrentPosition();
        boolean isSlideRetracted = !slideSensor.getState();

        telemetry.addData("currentArm", "%d", currentArm);
        telemetry.addData("currentSlide", "%d", currentSlide);
        telemetry.addData("slideSensor", "%b", isSlideRetracted);

        if (changingState) {
            // Maybe check the target vs actual position
            if (currentSlide > targetSlide && ((currentSlide - targetSlide) > 300)) {
                telemetry.addData("Still retracting arm, diff=", "%d", (currentSlide - targetSlide));
                return;
            }
            
            // Make sure the buttons aren't down any more
            if (!gamepad2.dpad_left && !gamepad2.dpad_right) {
                changingState = false;
            }
            return;
        }
        
        if (gamepad2.dpad_left) {
            if (armState > armStateMin) {
                --armState;
                changingState = true;
            }
        }
        else if (gamepad2.dpad_right) {
            if (armState < armStateMax) {
                ++armState;
                changingState = true;
            }   
        }
        
        // Now set the target position based on the armState
        if (changingState) {
            armNudgeAmount = 0; // Reset the nudge to use the preset values below
            
            if (armState == 1) {
                targetArm = 100;
                targetSlide = 0;
            }
            else if (armState == 2) { // Low Arm, Slide retracted
                targetArm = 120;
                targetSlide = 0;
            }
            else if (armState == 3) { // Low Arm, Slide partial
                targetArm = 200;
                targetSlide = 250;
            }
            else if (armState == 4) {  // Medium extension...  freely move now
                targetArm = 700;
                targetSlide = 350;
            }
            else if (armState == 5) { //retracting slide before lift
                //targetArm = 30;
                targetSlide = 0;  // RETRACT BEFORE UP
            }
            else if (armState == 6) { // RAISE THE ARM
                targetArm = 1500;
                targetSlide = 0;  // RETRACT BEFORE DOWN
               //no change
            }
            else if (armState == 7) { // RAISE THE ARM
                targetArm = 2600;
                targetSlide = 0;  // RETRACT BEFORE DOWN
               //no change
            }
            else if (armState == 8) { // RAISE THE ARM
                targetArm = 3440; // make it 2900 when gear comes
                targetSlide = 0;  // RETRACT BEFORE DOWN
               //no change
            }
            else if (armState == 9) {
                targetArm = 3440;
                // no slide change
            }
            
            if (targetSlide > 0 || armState > 0) {
                //holdUpSlideServo.setPosition(holdUpUnlocked);
                while (holdUpSlideServo.getPosition() > holdUpUnlocked) {
                    //sleep(1);
                }
            }


            telemetry.addData("targetArm", "%d", targetArm);
            telemetry.addData("targetElbow", "%d", targetSlide);

            // Set the shoulder power and target position
            armMotor.setPower(1.0); // 0.2 - 0.4 is good WITHOUT INTAKE.  Needs 1.0 with intake
            armMotor.setTargetPosition(targetArm);
            if (armMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
               armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }

            // Set the shoulder power and target position
            slideMotor.setPower(0.8); // 0.2 - 0.4 is good
            slideMotor.setTargetPosition(targetSlide);
            if (slideMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION){
               slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }

        }
        
    }
    
    public void doSafeSlideMoves() {
        
            if (changingState) {
                return; // Don't move while changing state
            }
            
            int maxSlideForCurrentArm = 0;
        
            if (armState > 1 && armState <= 2) {
                // LOW ARM.. safe moves
                maxSlideForCurrentArm = 915;
            }
            else if (armState > 2 && armState <= 3) {
                // LOW ARM.. safe moves
                maxSlideForCurrentArm = 1100;
            }
             else if (armState > 3 && armState < 5) {
                // LOW ARM.. safe moves
                maxSlideForCurrentArm = 2250;
            }     
             else if (armState > 3 && armState < 5) {
                // MEDIUM ARM.. try to get low basket
                maxSlideForCurrentArm = 1400;
            }     
            else if (armState >= 8) {
                // HIGH ARM.. safe to move
                maxSlideForCurrentArm = 2850;
            }
            else {
                return; // not safe to move arm
            }
            
            int currentSlide = slideMotor.getCurrentPosition();

            int newTarget = targetSlide - (int)(gamepad2.right_stick_y * 100.0);
            
            if (newTarget < 100) {
                newTarget = 100;
            }
            else if (newTarget > maxSlideForCurrentArm) {
                newTarget = maxSlideForCurrentArm;
            }
            targetSlide = newTarget;
            telemetry.addData("Arm state", "%d", armState);
            telemetry.addData("smallSlide-current", "%d", currentSlide);
            telemetry.addData("smallSlide-target ", "%d", targetSlide);
            slideMotor.setTargetPosition(targetSlide);
           
    }

    
    public int computeShoulderPosition() {
        //updateArmFixNudge();
        
        // arm control is LEFT STICK
        double stickPos = gamepad2.left_stick_y;
        
        double armMaxStep = 4.0;
        double armStep = -stickPos * armMaxStep;
        armTargetPosition += (int)Math.round(armStep);

        // Limit the range
        if (armTargetPosition < lowestArmPosition)
            armTargetPosition = lowestArmPosition;
        if (armTargetPosition > highestArmPosition)
            armTargetPosition = highestArmPosition;
            
        armTargetPosition += armFix;
            
        //telemetry.addData("stickPos","%f",stickPos);
        telemetry.addData("setPostion","%d", armTargetPosition);

        return armTargetPosition;
    }*/

    @Override
    public double computeDriveSpeed() {
        /*double slideSafetyFactor = 1;//If 
        final double powerScale = 0.5;//normal speed
        final double fasterPowerScale = 0.75;//Fast speed
        final double slowerPowerScale = 0.25; //when left bumper is held
        double cPowerScale = gamepad1.right_bumper ? slowerPowerScale : gamepad1.left_bumper ? fasterPowerScale : powerScale;
        
        int currentSlide = slideMotor.getCurrentPosition();
        if (currentSlide > 300) {
            slideSafetyFactor = 0.65;
        }
        return cPowerScale * slideSafetyFactor; */  
        return 0;
    }
    /*
    public void handleWristServo() {
        if (gamepad2.x){
            wristServo.setPosition(0.0);//out
            
        }
        if (gamepad2.y) {
            wristServo.setPosition(0.5);//in
        }

        if (gamepad2.a ) {
            intakeServo.setPosition(0); // OUT
        }
        else if (gamepad2.b) {
            intakeServo.setPosition(1.0); // IN
        }
        else {
            intakeServo.setPosition(0.5); // stopped
        }
    }
    
    public void handleDemoWristServo() {

        if (gamepad2.a ) {
            intakeServo.setPosition(0); // OUT
        }
        else if (gamepad2.b) {
            intakeServo.setPosition(1.0); // IN
        }
        else {
            intakeServo.setPosition(0.5); // stopped
        }
    }
    */
    
    @Override
    public void init() {
        super.init();
        //retractSlide();
    }

    @Override
    public void loop() {
    /*
        checkEmergencyStop();
        checkRetract();
       */ 
        // boolean isArmUpright = !uprightSensor.getState();        
        
        
        // if (isArmUpright || isDemoModeActive)
        // {
        //     isDemoModeActive = true;
        //     telemetry.addData("demoMode", "%b", isDemoModeActive);
            
            // If the arm is all the way up, then don't burn up the motors
            /*slideMotor.setPower(0);
            slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            armMotor.setPower(0);
            
            handleDemoWristServo();*/
        // }
        // else
        // {
           /* doPresetPositions();
            
            handleWristServo();x
    
            doSafeSlideMoves();*/
        

        super.loop();
    }
}













