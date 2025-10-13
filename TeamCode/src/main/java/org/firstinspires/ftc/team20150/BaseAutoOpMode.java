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
    
    protected Servo wristServo = null;
    protected Servo intakeServo = null;
    protected DcMotor armMotor = null;
    protected DcMotor slideMotor = null;
    
    protected Servo holdUpSlideServo = null;
    protected double holdUpLocked = 0.5;
    protected double holdUpUnlocked = 0.2;

    protected DigitalChannel slideSensor = null;

    // arm limits
    protected int armMinPosition = 0;  //reset on init
    protected int armMaxPosition = 0;  //reset on init to min + armMaxOffset
    protected int armTargetPosition = 0;
    //slide limits
    protected int slideMinPosition = 0;  //reset on init
    protected int slideMaxPosition = 0;  //reset on init to min + slideMaxOffset
    protected int slideTargetPosition = 0;
    //Maxium offsets
    protected int armMaxOffset = 800;
    protected int slideMaxOffset = 500;

    VisionPortal portal;

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
        

        slideMotor = hardwareMap.get(DcMotor.class, "SLIDE");
        armMotor = hardwareMap.get(DcMotor.class, "ARM");
        intakeServo = hardwareMap.get(Servo.class, "INTAKE");
        wristServo = hardwareMap.get(Servo.class, "WRIST");
        
        holdUpSlideServo = hardwareMap.get(Servo.class,"LOCK");
        //holdUpSlideServo.setPosition(holdUpLocked);

        slideSensor = hardwareMap.get(DigitalChannel.class,"MAGNET");
        slideSensor.setMode(DigitalChannel.Mode.INPUT);
        
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


        // Copied from BaseTeleOpMode
        slideMotor.setDirection(DcMotor.Direction.FORWARD);
        armMotor.setDirection(DcMotor.Direction.FORWARD);
        
        //setting up the shoulder motor 
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMinPosition = armMotor.getCurrentPosition();
        armMaxPosition = armMinPosition + armMaxOffset;
        //change armMaxPosition ofset
        telemetry.addData("armMinPosition:", "%d", armMinPosition);
        telemetry.addData("armMaxPosition:", "%d", armMaxPosition);
        //setting up slide motor
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMinPosition = slideMotor.getCurrentPosition();
        slideMaxPosition = slideMinPosition + slideMaxOffset;
        wristServo.setPosition(0.64);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setPower(0);
        
        
        //change armMaxPosition ofset
        
        

        // capture "before randomization" photo here
       /* portal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "WEBCAM"))
                .setCameraResolution(new Size(RESOLUTION_WIDTH, RESOLUTION_HEIGHT))
                .build();

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData(
                "Starting at", "%7d %7d %7d %7d",
                leftFrontDrive.getCurrentPosition(),
                rightFrontDrive.getCurrentPosition(),
                leftBackDrive.getCurrentPosition(),
                rightBackDrive.getCurrentPosition());
        telemetry.update();
    */}

    /*public int findSpikeLocation() {
       String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String beforePath = String.format("%s/VisionPortal-PhotoBeforeRandomization.png", dirPath);
        String afterPath = String.format("%s/VisionPortal-PhotoAfterRandomization.png", dirPath);

        Mat imgBefore = Imgcodecs.imread(beforePath);
        Mat imgAfter = Imgcodecs.imread(afterPath);
        telemetry.addData("imgBefore:", "%d x %d", imgBefore.cols(), imgBefore.rows());
        telemetry.addData("imgAfter:", "%d x %d", imgAfter.cols(), imgAfter.rows());
        telemetry.update();

        // find difference between before and after images
        Mat imgDiff = imgBefore.clone();
        Core.absdiff(imgBefore, imgAfter, imgDiff);

        // convert to black and white threshold image
        final byte[] whitePixel = {(byte)255, (byte)255, (byte)255};
        final byte[] blackPixel = {(byte)0, (byte)0, (byte)0};

        final int imgWidth = imgDiff.width();
        final int imgHeight = imgDiff.height();
        
        int halfImgWidth = imgBefore.cols() / 2;
        int halfImgHeight = imgBefore.rows() / 2;

        Mat imgThreshold = imgDiff.clone();
        for (int x = 0; x < imgWidth; x++) {
            for (int y = 0; y < imgHeight; y++) {
                byte[] cPixel = new byte[3];
                imgThreshold.get(y, x, cPixel);
                        
                imgThreshold.put(y, x, (int)(cPixel[0] + cPixel[1] + cPixel[2]) / 3 >= 30 ? whitePixel : blackPixel);
            }
        }

        // Create images of top-left region of full image
        Mat topLeft = imgThreshold.submat(0, halfImgHeight, 0, halfImgWidth);
        Mat topRight = imgThreshold.submat(0, halfImgHeight, halfImgWidth, imgWidth);

        // see if the top left corner changed
        double topLeftPixelCount = 0.0;
        for (int x = 0; x < halfImgWidth; x++) {
            for (int y = 0; y < halfImgHeight; y++) {
                byte[] cPixel = new byte[3];
                topLeft.get(y, x, cPixel);
                if (cPixel[0] == (byte)255) {
                    topLeftPixelCount++;
                }
            }
        }
        double topLeftPercentage = topLeftPixelCount / (halfImgWidth * halfImgHeight);
        boolean topLeftChanged = topLeftPercentage >= 0.1;
                
        // see if the top left corner changed
        double topRightPixelCount = 0.0;
        for (int x = 0; x < halfImgWidth; x++) {
            for (int y = 0; y < halfImgHeight; y++) {
                byte[] cPixel = new byte[3];
                topRight.get(y, x, cPixel);
                if (cPixel[0] == (byte)255) {
                    topRightPixelCount++;
                }
            }
        }
       
        
    }
    */

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
