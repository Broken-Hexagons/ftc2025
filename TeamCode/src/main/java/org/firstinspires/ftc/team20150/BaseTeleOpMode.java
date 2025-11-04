package org.firstinspires.ftc.team20150;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.opencv.core.Mat;

public class BaseTeleOpMode extends OpMode {

    // Declare OpMode members.
    protected DcMotor leftFrontDrive = null;
    protected DcMotor leftBackDrive = null;
    protected DcMotor rightFrontDrive = null;
    protected DcMotor rightBackDrive = null;
    protected int sectorCount = 4;

    public double calculateMse(Mat img1, Mat img2) {
        double diff = 0.0;
        final int width = img1.width();
        final int height = img1.height();
        for (int x = 0; x < width; x++){
            for (int y = 0;  y < height; y++) {
                for (int color = 0; color < 3; color++) {
                    byte[] pv1 = new byte[3];
                    byte[] pv2 = new byte[3];
                    
                    img1.get(y, x, pv1);
                    img2.get(y, x, pv2);
                    
                    if (pv1[color] != pv2[color]) {
                        diff += Math.abs(pv2[color] - pv1[color]);
                    }
                }
            }
        }
        return diff;
    }
    
    /*
     * Code to run ONCE when the driver hits INIT
     */
     
    @Override
    public void init() {
        //Initialize the hardware variables.
        //Initializing the wheels
        leftFrontDrive = hardwareMap.get(DcMotor.class,"FL");
        leftBackDrive = hardwareMap.get(DcMotor.class, "BL");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "FR");
        rightBackDrive = hardwareMap.get(DcMotor.class, "BR");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    //@Override

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    private double stickToDegrees(double x, double y) {
        return Math.toDegrees(Math.atan2(x, -y));
    }

    private double snapAngle(double angleDegrees, double sectorCount) {
        return Math.round(angleDegrees / (360.0 / sectorCount)) * (360.0 / sectorCount);
    }

    private double degreesToStickX(double angleDegrees, double magnitude) {
        return Math.sin(Math.toRadians(angleDegrees)) * magnitude;
    }

    private double degreesToStickY(double angleDegrees, double magnitude) {
        return -Math.cos(Math.toRadians(angleDegrees)) * magnitude;
    }

    private double stickToMagnitude(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    private double stickToSteeringRad(double stickX) {
        return stickX * Math.PI / 2.0;
    }

    private double[] steeringRadToWheelRad(double steeringRad, double width, double length) {
        final double tanPhi = Math.tan(steeringRad);
        final double rightAngle = Math.atan2(tanPhi, 1 + width / length * tanPhi);
        final double leftAngle = Math.atan2(tanPhi, 1 - width / length * tanPhi);

        double[] wheelAngles = new double[2];
        wheelAngles[0] = leftAngle;
        wheelAngles[1] = rightAngle;

        return wheelAngles;
    }

    private double steeringRadToInsideWheelScale(double steeringRad, double insideRad, double width, double length) {
        return (1.0 - (width / length) * Math.tan(steeringRad)) / Math.cos(insideRad);
    }

    private double steeringRadToOutsideWheelScale(double steeringRad, double outsideRad, double width, double length) {
        return (1.0 + (width / length) * Math.tan(steeringRad)) / Math.cos(outsideRad);
    }

    private double[] wheelRadsToAxialSpeeds(double leftWheelMag, double rightWheelMag, double wheelRadFL,
            double wheelRadFR) {
        double[] axialSpeeds = new double[4];
        axialSpeeds[0] = (Math.sin(wheelRadFL) + Math.cos(wheelRadFL)) * leftWheelMag; // FL
        axialSpeeds[1] = (Math.sin(wheelRadFR) - Math.cos(wheelRadFR)) * rightWheelMag; // FR
        axialSpeeds[2] = (Math.sin(-wheelRadFL) - Math.cos(-wheelRadFL)) * leftWheelMag; // BL
        axialSpeeds[3] = (Math.sin(-wheelRadFR) + Math.cos(-wheelRadFR)) * rightWheelMag; // BR

        return axialSpeeds;
    }
    
    public double computeDriveSpeed() {
        final double powerScale = 0.5; //normal speed
        final double fasterPowerScale = 0.75;//Fast speed
        final double slowerPowerScale = 0.25; //when left bumper is held
        double cPowerScale = gamepad1.right_bumper ? slowerPowerScale : gamepad1.left_bumper ? fasterPowerScale : powerScale;

        return cPowerScale;
    }
    
    @Override
    public void loop() {

        final double width = 0.3;
        final double length = 0.32;

        final double magnitude = stickToMagnitude(gamepad1.left_stick_x, gamepad1.left_stick_y);
        final double angleDegrees = stickToDegrees(gamepad1.left_stick_x, gamepad1.left_stick_y);
        final double snapDegrees = snapAngle(angleDegrees, sectorCount);
        final double snappedX = degreesToStickX(snapDegrees, magnitude);
        final double snappedY = degreesToStickY(snapDegrees, magnitude);

        final double steeringRad = stickToSteeringRad(gamepad1.right_stick_x);
        final double[] wheelRads = steeringRadToWheelRad(steeringRad, width, length);

        final double leftWheelScale = (steeringRad > 0.0)
                ? steeringRadToOutsideWheelScale(steeringRad, wheelRads[0], width, length)
                : steeringRadToInsideWheelScale(steeringRad, wheelRads[0], width, length);

        final double rightWheelScale = (steeringRad < 0.0)
                ? steeringRadToOutsideWheelScale(steeringRad, wheelRads[1], width, length)
                : steeringRadToInsideWheelScale(steeringRad, wheelRads[1], width, length);

        final double[] axialSpeeds = wheelRadsToAxialSpeeds(leftWheelScale * snappedY, rightWheelScale * snappedY,
                wheelRads[0], wheelRads[1]);

        // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double axial = -snappedY * (1 + 0.025 * Math.abs(gamepad1.right_stick_x));
        double lateral = snappedX;
        double yaw = gamepad1.right_stick_x;

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double leftFrontPower = axial + lateral + yaw;
        double rightFrontPower = axial - lateral - yaw;
        double leftBackPower = axial - lateral + yaw;
        double rightBackPower = axial + lateral - yaw;

        
        
        double cPowerScale = computeDriveSpeed();
        
        leftFrontPower *= cPowerScale;
        rightFrontPower *= cPowerScale;
        leftBackPower *= cPowerScale;
        rightBackPower *= cPowerScale;

        // Send calculated power to wheels
        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);

        // Show the elapsed game time and wheel power.
        //telemetry.addData("Status", "Run Time: " + runtime.toString());

        final double steeringDeg = Math.toDegrees(steeringRad);
        final double leftSteeringDeg = Math.toDegrees(wheelRads[0]);
        final double rightSteeringDeg = Math.toDegrees(wheelRads[1]);

//        telemetry.addData("Steering", "%4.2f/%4.2f/%4.2f", steeringDeg, leftSteeringDeg, rightSteeringDeg);
//        telemetry.addData("Wheel Scale", "%4.2f/%4.2f", leftWheelScale, rightWheelScale);
//        telemetry.addData("Axial Speeds FL/FR/BL/BR", "%4.2f/%4.2f/%4.2f/%4.2f", axialSpeeds[0], axialSpeeds[1],
//            axialSpeeds[2], axialSpeeds[3]);
//
//        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
