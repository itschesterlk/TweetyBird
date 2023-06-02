package org.firstinspires.ftc.teamcode.TweetyBird.TweetyFiles;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public class TB_Mover extends Thread {

    //IsBusy
    protected static boolean isBusy = false;

    //Waypoint List
    private static ArrayList<TB_Waypoint> waypoints = new ArrayList<TB_Waypoint>(); //Essentially a queue for waypoints, waypoint is deleted when loaded into current

    //Current Waypoint
    private static TB_Waypoint current = new TB_Waypoint(0,0,0); //Setting the first waypoint, (Start Pos)

    //The thread
    @Override
    public void run() {
        //Clearing Out Waypoint Table
        TB_Mover.waypoints.clear();
        current = new TB_Waypoint(0,0,0);

        //Locking Motors for INIT
        lockDrivetrain();

        //Creating Telemetry Reference (Not Necessary)
        Telemetry telemetry = TB_Master.opMode.telemetry;

        //Waiting for the robot to start
        TB_Master.opMode.waitForStart();

        //Unlock
        unlockDrivetrain();

        //Speed Controller Value
        double movementSpeed = 0; //Temporarily set to zero, will be set during every loop

        //Loop
        while (TB_Master.opMode.opModeIsActive()) { //Will run forever while the robot is running
            //Loading and Switching Waypoints
            if (TB_Mover.waypoints.size()>0 && getDistanceToWaypoint()<0.5) { //Loads when there are waypoints in queue and the current waypoint is almost done
                //Loading Waypoint
                TB_Mover.current = TB_Mover.waypoints.get(0); //Gets the first waypoint in queue into current
                //Removing Waypoint from List
                TB_Mover.waypoints.remove(0); //Removes the first waypoint in queue from the list
            }

            //Calculating Trajectory to Waypoint
            double disToWaypointX = TB_Mover.current.getX()- TB_Odometer.X; //Distance from the bot to the current waypoint
            double disToWaypointY = TB_Mover.current.getY()- TB_Odometer.Y;

            double targetHeading = Math.atan2(disToWaypointY,disToWaypointX); //Converting to radians
            double axial = Math.sin(targetHeading); //Y axis motor power
            double lateral = Math.cos(targetHeading)*1.3; //30% bias since it takes more power to strafe

            double yaw = Range.clip((TB_Mover.current.getZ() - Math.toDegrees(TB_Odometer.Z)) / 15, -TB_Config.maxSpeed, TB_Config.maxSpeed);

            movementSpeed = Range.clip(getDistanceToEnd()*0.1,TB_Config.minSpeed,TB_Config.maxSpeed);

            boolean movementBusy = false;

            if (getDistanceToEnd() > 0.3) {
                movementBusy = true;
            } else {
                axial = 0; lateral = 0;
            }

            boolean rotationBusy = false;

            if (getDegreesOff() >= 1) {
                rotationBusy = true;
            } else {
                yaw = 0;
            }


            movementPower(axial,lateral,yaw,movementSpeed);


            if (movementBusy || rotationBusy) {
                TB_Mover.isBusy = true;
                unlockDrivetrain();
            } else {
                TB_Mover.isBusy = false;
                lockDrivetrain();
            }


            /*/Telemetry TODO: Currently disabled, maybe another class can be used to control telemetry to avoid conflicts, and add a config option to enable/disable
            telemetry.addData("Current Position Y:X:Z","~"+Math.round(TB_Odometer.Y)+", ~"+Math.round(TB_Odometer.X)+", ~"+Math.round(Math.toDegrees(TB_Odometer.Z)));
            telemetry.addData("Current Waypoint Y:X:Z",""+ TB_Mover.current.getY()+", "+ TB_Mover.current.getX()+", "+TB_Mover.current.getZ());
            telemetry.addData("Distance To Waypoint",getDistanceToWaypoint());
            telemetry.addLine();
            telemetry.addData("Busy?",isBusy);
            telemetry.addData("Movement Busy?",movementBusy);
            telemetry.addData("Rotation Busy?",rotationBusy);
            telemetry.addLine();
            telemetry.addData("Waypoint In Queue", TB_Mover.waypoints.size());
            telemetry.addData("Distance Of Waypoints",getDistanceToEnd());
            telemetry.addLine();
            telemetry.addData("Target Heading","~"+Math.round(targetHeading));
            telemetry.addData("Axial",axial);
            telemetry.addData("Lateral",lateral);
            telemetry.addData("Yaw",yaw);
            telemetry.update();*/


        }
    }

    //Lock Motors
    private void lockDrivetrain() {
        TB_Master.drivetrain.backLeft.setPower(0);
        TB_Master.drivetrain.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        TB_Master.drivetrain.frontRight.setPower(0);
        TB_Master.drivetrain.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        TB_Master.drivetrain.backRight.setPower(-0);
        TB_Master.drivetrain.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        TB_Master.drivetrain.frontLeft.setPower(-0);
        TB_Master.drivetrain.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    //Unlock Motors
    private void unlockDrivetrain() {
        TB_Master.drivetrain.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        TB_Master.drivetrain.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        TB_Master.drivetrain.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        TB_Master.drivetrain.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    //Set Motor Powers
    private void movementPower(double axial, double lateral, double yaw, double speed) {
        //Creating Individual Power for Each Motor
        double frontLeftPower  = ((axial + lateral) * speed) + yaw;
        double frontRightPower = ((axial - lateral) * speed) - yaw;
        double backLeftPower   = ((axial - lateral) * speed) + yaw;
        double backRightPower  = ((axial + lateral) * speed) - yaw;

        //Set Motor Power
        TB_Master.drivetrain.frontLeft.setPower(frontLeftPower);
        TB_Master.drivetrain.frontRight.setPower(frontRightPower);
        TB_Master.drivetrain.backLeft.setPower(backLeftPower);
        TB_Master.drivetrain.backRight.setPower(backRightPower);
    }

    //Add to Waypoint List
    protected void addSegment(double x, double y, double z) {
        TB_Mover.waypoints.add(new TB_Waypoint(x,y,z));
    }

    //Get Length of Waypoints
    protected double getDistanceToEnd() {
        //Starts with the distance from bot to current waypoint
        double returnDouble = getDistanceToWaypoint();

        //Counts up for each waypoint in queue
        for (int i = 1; i < TB_Mover.waypoints.size(); i++) {
            returnDouble+=distanceForm(TB_Mover.waypoints.get(i-1).getX(), TB_Mover.waypoints.get(i-1).getY(), TB_Mover.waypoints.get(i).getX(), TB_Mover.waypoints.get(i).getY());
        }

        //Finally Returning Value
        return returnDouble;
    }

    //Get Distance from Bot to Current Waypoint
    private double getDistanceToWaypoint() {
        return distanceForm(TB_Odometer.X,TB_Odometer.Y, TB_Mover.current.getX(), TB_Mover.current.getY());
    }

    //Get Degrees Off Rotation
    private double getDegreesOff() {
        return Math.abs(Math.toDegrees(TB_Odometer.Z) - TB_Mover.current.getZ());
    }

    //Get last position from the list //TODO: Not sure if this is a benchmark bug, however it seems it isn't getting the final item in queue...
    protected double getLastX() {
        if (waypoints.size()>0) {
            return waypoints.get(waypoints.size()-1).getX();
        }
        return TB_Mover.current.getX();
    }
    protected double getLastY() {
        if (waypoints.size()>0) {
            return waypoints.get(waypoints.size()-1).getY();
        }
        return TB_Mover.current.getY();
    }
    
    //Distance Formula
    private double distanceForm(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }

}
