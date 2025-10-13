package org.firstinspires.ftc.team20150;

import android.util.Size;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.File;
import java.util.Set;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import android.os.Environment;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
//import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Locale;

/*
 * This OpMode helps calibrate a webcam or RC phone camera, useful for AprilTag pose estimation
 * with the FTC VisionPortal.   It captures a camera frame (image) and stores it on the Robot Controller
 * (Control Hub or RC phone), with each press of the gamepad button X (or Square).
 * Full calibration instructions are here:
 *
 *  https://ftc-docs.firstinspires.org/camera-calibration
 *
 * In Android Studio, copy this class into your "teamcode" folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 *
 * In OnBot Java, use "Add File" to add this OpMode from the list of Samples.
 */

@TeleOp(name = "Capture Still Photo", group = "Iterative OpMode")

public class StillPhotoTest extends LinearOpMode
{
    /*
     * EDIT THESE PARAMETERS AS NEEDED
     */
    final boolean USING_WEBCAM = true;
    final BuiltinCameraDirection INTERNAL_CAM_DIR = BuiltinCameraDirection.BACK;
    final int RESOLUTION_WIDTH = 640;
    final int RESOLUTION_HEIGHT = 480;

    // Internal state
    boolean lastA;
    boolean lastB;
    int frameCount;
    long capReqTime;


    @Override
    public void runOpMode()
    {
        VisionPortal portal;

        if (USING_WEBCAM)
        {
            portal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, "WEBCAM"))
                    .setCameraResolution(new Size(RESOLUTION_WIDTH, RESOLUTION_HEIGHT))
                    .build();
        }
        else
        {
            portal = new VisionPortal.Builder()
                    .setCamera(INTERNAL_CAM_DIR)
                    .setCameraResolution(new Size(RESOLUTION_WIDTH, RESOLUTION_HEIGHT))
                    .build();
        }

        while (!isStopRequested())
        {
            boolean a_pressed = gamepad1.a;
            boolean b_pressed = gamepad1.b;

            if (a_pressed && !lastA)
            {
                //portal.saveNextFrameRaw(String.format(Locale.US, "CameraFrameCapture-%06d", frameCount++));
                portal.saveNextFrameRaw("blue_center_PhotoBeforeRandomization");
                capReqTime = System.currentTimeMillis();
                telemetry.addLine("Captured \"before\" image");
            }
            
            if (b_pressed && !lastB)
            {
                //portal.saveNextFrameRaw(String.format(Locale.US, "CameraFrameCapture-%06d", frameCount++));
                portal.saveNextFrameRaw("blue_left_PhotoAfterRandomization");
                capReqTime = System.currentTimeMillis();
                telemetry.addLine("Captured \"after\" image");
            }

            lastA = a_pressed;
            lastB = b_pressed;

            if (lastB)
            {
                //String path = "VisionPortal-PhotoBeforeRandomization.png";
                //File f = new File(path);
                //Boolean itExists = f.exists();
                //telemetry.addLine(itExists ? "yes":"no");
                
                String filePath = String.format("%s/VisionPortal-blue_center_PhotoAfterRandomization.png",
                    Environment.getExternalStorageDirectory().getAbsolutePath());
                File testFile = new File(filePath);

                Mat imgBefore = Imgcodecs.imread("VisionPortal-blue_center_PhotoBeforeRandomization.png");
                Mat imgAfter = Imgcodecs.imread(filePath);
                //telemetry.addLine("imgBefore: ", imgBefore.);
                
                telemetry.addLine(String.format(Locale.US, testFile.exists() ? "exists" : "does not exist"));
                telemetry.addLine(String.format(Locale.US, filePath));

                telemetry.addLine(String.format(Locale.US, "imgBefore: %d rows %d cols", imgBefore.rows(), imgBefore.cols()));
                telemetry.addLine(String.format(Locale.US, "imgAfter:  %d rows %d cols", imgAfter.rows(), imgAfter.cols()));
            }

/*
            telemetry.addLine("######## Camera Capture Utility ########");
            telemetry.addLine(String.format(Locale.US, " > Resolution: %dx%d", RESOLUTION_WIDTH, RESOLUTION_HEIGHT));
            telemetry.addLine(" > Press X (or Square) to capture a frame");
            telemetry.addData(" > Camera Status", portal.getCameraState());

            if (capReqTime != 0)
            {
                telemetry.addLine("\nCaptured Frame!");
            }

            if (capReqTime != 0 && System.currentTimeMillis() - capReqTime > 1000)
            {
                capReqTime = 0;
            }
*/

            telemetry.update();
        }
    }
}