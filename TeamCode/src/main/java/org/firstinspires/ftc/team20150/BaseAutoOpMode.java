package org.firstinspires.ftc.team20150;

import java.io.File;
import java.util.Locale;
import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

import android.os.Environment;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

public class BaseAutoOpMode extends LinearOpMode {

    private final int RESOLUTION_WIDTH = 640;
    private final int RESOLUTION_HEIGHT = 480;

    //Declare OpMode members.
    protected DcMotor leftFrontDrive = null;
    protected DcMotor leftBackDrive = null;
    protected DcMotor rightFrontDrive = null;
    protected DcMotor rightBackDrive = null;



    protected ElapsedTime runtime = new ElapsedTime();
    

    static final double COUNTS_PER_MOTOR_REV = 751.8; // eg: TETRIX motor encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0 / 1.0; // No external gearing
    static final double WHEEL_DIAMETER_INCHES = 3.75; // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 1;
    static final double TURN_SPEED = 0.5;
    static final double TURN_INCHES_PER_DEGREE = 0.194;

    @Override
    public void runOpMode() {

        // Initialize the drive system variables.
        leftFrontDrive = hardwareMap.get(DcMotor.class, "FL");
        leftBackDrive = hardwareMap.get(DcMotor.class, "BL");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "FR");
        rightBackDrive = hardwareMap.get(DcMotor.class, "BR");

        
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
}


    public void encoderDrive(
            double speed,
            double leftFront,
            double rightFront,
            double leftBack,
            double rightBack,
            double timeoutSec) {
        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newBackLeftTarget;
        int newBackRightTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newFrontLeftTarget = leftFrontDrive.getCurrentPosition() + (int) (leftFront * COUNTS_PER_INCH);
            newFrontRightTarget = rightFrontDrive.getCurrentPosition() + (int) (rightFront * COUNTS_PER_INCH);
            newBackLeftTarget = leftBackDrive.getCurrentPosition() + (int) (leftBack * COUNTS_PER_INCH);
            newBackRightTarget = rightBackDrive.getCurrentPosition() + (int) (rightBack * COUNTS_PER_INCH);
            leftFrontDrive.setTargetPosition(newFrontLeftTarget);
            rightFrontDrive.setTargetPosition(newFrontRightTarget);
            leftBackDrive.setTargetPosition(newBackLeftTarget);
            rightBackDrive.setTargetPosition(newBackRightTarget);

            // Turn On RUN_TO_POSITION
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            leftFrontDrive.setPower(Math.abs(speed));
            rightFrontDrive.setPower(Math.abs(speed));
            leftBackDrive.setPower(Math.abs(speed));
            rightBackDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both
            // motors are running. Note: We use (isBusy() && isBusy()) in the loop test,
            // which means that when EITHER motor hits its target position, the motion will
            // stop. This is "safer" in the event that the robot will always end the motion
            // as soon as possible. However, if you require that BOTH motors have finished
            // their moves before the robot continues onto the next step, use (isBusy() ||
            // isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutSec) &&
                    (leftFrontDrive.isBusy() && rightFrontDrive.isBusy() &&
                            leftBackDrive.isBusy() && rightBackDrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to", " %7d %7d %7d %7d", newFrontLeftTarget, newFrontRightTarget,
                        newBackLeftTarget, newBackRightTarget);

                telemetry.addData("power", "%4.2f %4.2f %4.2f %4.2f", leftFrontDrive.getPower(), rightFrontDrive.getPower(),
                        leftBackDrive.getPower(), rightBackDrive.getPower());
                telemetry.update();
            }

            // Stop all motion;
            leftFrontDrive.setPower(0);
            rightFrontDrive.setPower(0);
            leftBackDrive.setPower(0);
            rightBackDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(1000); // Intentional pause after each move.
        }
    }

    public void driveForwardInches(double speed, double distanceInches, double timeoutSec) {
        final double leftFront = distanceInches;
        final double rightFront = distanceInches;
        final double leftBack = distanceInches;
        final double rightBack = distanceInches;

        encoderDrive(speed, leftFront, rightFront, leftBack, rightBack, timeoutSec);
    }
    
    public void strafeLeftInches(double speed, double distanceInches, double timeoutSec) {
        final double leftFront = -distanceInches;
        final double rightFront = distanceInches;
        final double leftBack = distanceInches;
        final double rightBack = -distanceInches;

        encoderDrive(speed, leftFront, rightFront, leftBack, rightBack, timeoutSec);
    }

    public void strafeRightInches(double speed, double distanceInches, double timeoutSec) {
        final double leftFront = distanceInches;
        final double rightFront = -distanceInches;
        final double leftBack = -distanceInches;
        final double rightBack = distanceInches;

        encoderDrive(speed, leftFront, rightFront, leftBack, rightBack, timeoutSec);
    }

    public void turnLeftDegrees(double speed, double angleDeg, double timeoutSec) {
        final double leftFront = -angleDeg * TURN_INCHES_PER_DEGREE;
        final double rightFront = angleDeg * TURN_INCHES_PER_DEGREE;
        final double leftBack = -angleDeg * TURN_INCHES_PER_DEGREE;
        final double rightBack = angleDeg * TURN_INCHES_PER_DEGREE;

        encoderDrive(speed, leftFront, rightFront, leftBack, rightBack, timeoutSec);
    }

    public void turnRightDegrees(double speed, double angleDeg, double timeoutSec) {
        final double leftFront = angleDeg * TURN_INCHES_PER_DEGREE;
        final double rightFront = -angleDeg * TURN_INCHES_PER_DEGREE;
        final double leftBack = angleDeg * TURN_INCHES_PER_DEGREE;
        final double rightBack = -angleDeg * TURN_INCHES_PER_DEGREE;

        encoderDrive(speed, leftFront, rightFront, leftBack, rightBack, timeoutSec);
    }
}
