//12.13: Pixel dropper working, just edit numbers for movement. Less forward and more strafe

package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Blue Broken", group = "Robot")

public class BlueBackdropAutoOpMode extends BaseAutoOpMode {

    @Override
    public void runOpMode() {
        // initialize robot and capture "before randomization" photo
        super.runOpMode();
       // portal.saveNextFrameRaw("PhotoBeforeRandomization");
        //sleep(1000);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        //portal.saveNextFrameRaw("PhotoAfterRandomization");
        //sleep(1000);

        // capture "after randomization" photo and find spike
        // int spikeLocation = findSpikeLocation();
       // int spikeLocation = findSpikeLocation(); // 0=left 1=center 2=right
        
        
        //Drop pixel;
        //armServo.setPosition(purpleServoDropPosition);
        try{
            Thread.sleep(1000);
       }
        catch(Exception e){
             
        }
           // armServo.setPosition(purpleServoHoldPosition);
        
            driveForwardInches(DRIVE_SPEED, -12, 5);
            strafeRightInches(DRIVE_SPEED,-6,5);
            telemetry.addData("Path", "Complete");
            telemetry.update();
        
        
       
        
        
        sleep(1000);
        sleep(5000);

        // run

        // Step 1: Drive backward 27 inches
        //if prop is on left spike
        /*if(spikeLocation==1){
            driveForwardInches(DRIVE_SPEED, -17, 5);
            strafeRightInches(DRIVE_SPEED, 3, 5);
           // armServo.setPosition(purpleServoDropPosition);
            try{
                Thread.sleep(1000);
            }
            catch(Exception e){
             
            }
            //armServo.setPosition(purpleServoHoldPosition);
            driveForwardInches(DRIVE_SPEED, 5, 5);
            strafeRightInches(DRIVE_SPEED, 28, 5);
        }
        else if(spikeLocation==0){
            driveForwardInches(DRIVE_SPEED, -10, 5);
            strafeRightInches(DRIVE_SPEED, 5,5);
            //armServo.setPosition(purpleServoDropPosition);
            try{
                Thread.sleep(1000);
            }
            catch(Exception e){
             
            }
           // armServo.setPosition(purpleServoHoldPosition);
            driveForwardInches(DRIVE_SPEED, 5, 5);
            strafeRightInches(DRIVE_SPEED, 20, 5);
            
        }
        else {
            strafeRightInches(DRIVE_SPEED, 28,5);
           // armServo.setPosition(purpleServoDropPosition);
            try{
                Thread.sleep(1000);
            }
            catch(Exception e){
             
            }
            //armServo.setPosition(purpleServoHoldPosition);
            }
            */
      //driveForwardInches(DRIVE_SPEED, -20, 5);
    
        sleep(1000); // pause to display final telemetry message.

    }
}
