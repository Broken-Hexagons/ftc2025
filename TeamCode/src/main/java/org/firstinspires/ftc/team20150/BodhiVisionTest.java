package org.firstinspires.ftc.team20150;

import android.util.Size;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.File;
import java.util.Set;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.vision.VisionPortal;
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

@TeleOp(name = "Bodhi Vision Test", group = "Iterative OpMode")

public class BodhiVisionTest extends LinearOpMode
{
    int getHue(int red, int green, int blue) {
        int min = Math.min(Math.min(red, green), blue);
        int max = Math.max(Math.max(red, green), blue);
        
        if (min == max) {
            return 0;
        }
        
        double hue = 0f;
        if (max == red) {
            hue = (green - blue) / (max - min); 
        } else if (max == green) {
            hue = 2f + (blue - red) / (max - min);
        } else {
            hue = 4f + (red - green) / (max - min);
        }
        
        hue *= 60;
        if (hue < 0) {
            hue += 360;
        }
        
        return (int)Math.round(hue);
    }
    /*
     * EDIT THESE PARAMETERS AS NEEDED
     */
    /*
    final boolean USING_WEBCAM = true;
    final BuiltinCameraDirection INTERNAL_CAM_DIR = BuiltinCameraDirection.BACK;
    final int RESOLUTION_WIDTH = 640;
    final int RESOLUTION_HEIGHT = 480;
    */

    // Internal state
    boolean lastA;
    boolean lastB;
    int frameCount;
    long capReqTime;
    
    @Override
    public void runOpMode()
    {
        /*
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
        */
        while (!isStopRequested())
        {
            boolean a_pressed = gamepad1.a;
            boolean b_pressed = gamepad1.b;
            /*
            if (a_pressed && !lastA)
            {
                //portal.saveNextFrameRaw(String.format(Locale.US, "CameraFrameCapture-%06d", frameCount++));
                portal.saveNextFrameRaw("PhotoBeforeRandomization");
                capReqTime = System.currentTimeMillis();
                telemetry.addLine("Captured \"before\" image");
            }
            
            if (b_pressed && !lastB)
            {
                //portal.saveNextFrameRaw(String.format(Locale.US, "CameraFrameCapture-%06d", frameCount++));
                portal.saveNextFrameRaw("PhotoAfterRandomization");
                capReqTime = System.currentTimeMillis();
                telemetry.addLine("Captured \"after\" image");
            }
            */

            lastA = a_pressed;
            lastB = b_pressed;

            if (lastB)
            {
                String baseFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                
                String beforeFilePath = String.format("%s/blue_center_PhotoAfterRandomization.png",
                    baseFilePath);
                    
                String afterFilePath = String.format("%s/blue_center_PhotoBeforeRandomization.png",
                    baseFilePath);

                Mat imgBefore = Imgcodecs.imread(beforeFilePath);
                Mat imgAfter = Imgcodecs.imread(afterFilePath);
                String imgDiffFilePath = String.format("%s/ImgDiff.png", 
                    baseFilePath);
                
                Mat imgDiff = imgBefore.clone();
                
                
                final byte[] whitePixel = {(byte)255, (byte)255, (byte)255};
                final byte[] blackPixel = {(byte)0, (byte)0, (byte)0};
                
                final int imgWidth = imgDiff.width();
                final int imgHeight = imgDiff.height();
                
                byte[] beforePixel = new byte[3];
                byte[] afterPixel = new byte[3];
                
                Core.absdiff(imgBefore, imgAfter, imgDiff);
                
                Imgcodecs.imwrite(imgDiffFilePath, imgDiff);
                
                String imgThresholdFilePath = String.format("%s/ImgThreshold.png", 
                    baseFilePath);
                    
                Mat imgThreshold = imgDiff.clone();
                for (int x = 0; x < imgWidth; x++) {
                    for (int y = 0; y < imgHeight; y++) {
                        byte[] cPixel = new byte[3];
                        imgThreshold.get(y, x, cPixel);
                        
                        imgThreshold.put(y, x, (int)(cPixel[0] + cPixel[1] + cPixel[2]) / 3 >= 30 ? whitePixel : blackPixel);
                    }
                }
                
                Imgcodecs.imwrite(imgThresholdFilePath, imgThreshold);
                
                String imgTopLeftFilePath = String.format("%s/ImgTopLeft.png", 
                    baseFilePath);
                
                String imgTopRightFilePath = String.format("%s/ImgTopRight.png", 
                    baseFilePath);
                
                final int halfImgWidth = imgWidth / 2;
                final int halfImgHeight = imgHeight / 2;
                
                Mat topLeft = imgThreshold.submat(0, halfImgHeight, 0, halfImgWidth);
                Mat topRight = imgThreshold.submat(0, halfImgHeight, halfImgWidth, imgWidth);

                Imgcodecs.imwrite(imgTopLeftFilePath, topLeft);
                Imgcodecs.imwrite(imgTopRightFilePath, topRight);
                
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
                double topRightPercentage = topRightPixelCount / (halfImgWidth * halfImgHeight);
                boolean topRightChanged = topRightPercentage >= 0.1;
                
                String propPosition;
                
                if (!topLeftChanged) {
                    propPosition = "center";
                } else {
                    if (topRightChanged) {
                        propPosition = "right";
                    } else {
                        propPosition = "left";
                    }
                }
                
                telemetry.addLine(String.format(Locale.US, "topLeftPixelPercentage: %2f, topRightPixelPercentage: %2f", topLeftPercentage, topRightPercentage));
                telemetry.addLine(String.format(Locale.US, "topLeftChanged: %b, topRightChanged: %b", topLeftChanged, topRightChanged));
                telemetry.addLine(String.format(Locale.US, "propPosition: %s", propPosition));
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