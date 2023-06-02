package org.firstinspires.ftc.teamcode.TweetyBird.TweetyFiles;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class TB_Master extends TB_Mover {

    //OpMode
    public static LinearOpMode opMode; //Will be set during constructor

    //Hardware Map
    public static TB_DrivetrainHwmap drivetrain = new TB_DrivetrainHwmap();

    //Constructor
    public TB_Master(LinearOpMode opMode) {
        //Setting OpMode Variable
        TB_Master.opMode = opMode;

        //Init Drivetrain
        drivetrain.init(opMode);

        //Lowering Encoder Pods
        drivetrain.podsDown();

        //Starting Threads
        TB_Mover moverThread = new TB_Mover();
        moverThread.start();

        TB_Odometer odometerThread = new TB_Odometer();
        odometerThread.start();
    }

    //Distance Formula
    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }

    //MoveTo
    public void moveTo(double targetY, double targetX, double targetZ) {
        //Getting position of the last waypoint in queue
        double currentX = super.getLastX();
        double currentY = super.getLastY();

        //Distances for the next part
        double overallDistance = distance(targetX,targetY,currentX,currentY);
        double xDistance = targetX-currentX;
        double yDistance = targetY-currentY;

        //This will be set in the next loop
        int numOfWaypoints = 0; //Zero is a placeholder

        //Determining how many waypoints can be created TODO: There must be a mathematical way to always round down simply
        for (double i = 0; i < overallDistance; i+=TB_Config.waypointIncrement) {
            numOfWaypoints+=1;
        }

        //Creating waypoints and sending them to TB_Mover
        for (int i = 0; i < numOfWaypoints; i++) { //removed =
            //Creates waypoints in a straight path to the end
            currentX += xDistance/numOfWaypoints;
            currentY += yDistance/numOfWaypoints;

            //Adding to list
            super.addSegment(currentX, currentY, targetZ);
        }

        //Adding the final waypoint, aka what you originally input into the param's
        super.addSegment(targetX, targetY, targetZ);

    }

    public boolean isBusy() {
        return TB_Mover.isBusy;
    }




}
