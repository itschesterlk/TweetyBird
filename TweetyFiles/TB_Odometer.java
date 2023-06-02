package org.firstinspires.ftc.teamcode.TweetyBird.TweetyFiles;

import com.qualcomm.robotcore.hardware.DcMotor;

public class TB_Odometer extends Thread {

    //Positions
    public static double X = 0;
    public static double Y = 0;
    public static double Z = 0;

    //The thread
    @Override
    public void run() {
        //Clearing Values
        TB_Odometer.X = 0;
        TB_Odometer.Y = 0;
        TB_Odometer.Z = 0;

        //Waiting for Start
        TB_Master.opMode.waitForStart();

        //Resting Encoder Positions
        TB_Master.drivetrain.leftEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TB_Master.drivetrain.rightEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TB_Master.drivetrain.backEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Re-enabling Motors since they are linked to the actual motors
        TB_Master.drivetrain.leftEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        TB_Master.drivetrain.rightEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        TB_Master.drivetrain.backEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //Storage
        int[] prevEncoderPos = {0,0,0}; //Used to see how far the robot moved

        //Loop until robot is stopped
        while (TB_Master.opMode.opModeIsActive()) {

            //Attempting to Prevent Crashing when Stopped as opModeIsActive seeming didn't work?? Extra precaution
            if (!TB_Master.opMode.opModeIsActive()) {
                break;
            }

            //Getting Encoder Positions
            int[] rawEncoderPos = {TB_Master.drivetrain.leftEncoder.getCurrentPosition(),
                    TB_Master.drivetrain.rightEncoder.getCurrentPosition(),
                    TB_Master.drivetrain.backEncoder.getCurrentPosition()};

            //Getting the Amount Each Encoder Moved Since the Last Cycle
            int[] movedPositions = {rawEncoderPos[0]-prevEncoderPos[0],
                    rawEncoderPos[1]-prevEncoderPos[1],
                    rawEncoderPos[2]-prevEncoderPos[2]};

            //Saving Current Position for Next Run
            prevEncoderPos = rawEncoderPos;

            //Getting Directions Moved TODO: Check to see if these calculations are accurate, they have been here since v1 and I didn't understand the math
            double rawLateral = (((double)movedPositions[2]/ TB_Config.ticksPerInch) -
                    (TB_Config.radiusToBackEncoder*((((double)movedPositions[1]/ TB_Config.ticksPerInch) -
                            ((double)movedPositions[0]/ TB_Config.ticksPerInch))/(2* TB_Config.radiusToSideEncoder))));

            double rawAxial = ((((double)movedPositions[0]/ TB_Config.ticksPerInch) +
                    ((double)movedPositions[1]/ TB_Config.ticksPerInch))/2);

           /* double rawYaw = (Math.toRadians((((((double)movedPositions[1]) - TODO: Currently disabled as the calculations are inaccurate, replaced with imu for the time being
                    ((double)movedPositions[0]))/(2* TB_Config.radiusToSideEncoder))/((TB_Config.ticksPerEncoderRotation/360) +
                    TB_Config.sideEncoderOffset))*-1));*/

            //Updating Values as Static
            TB_Odometer.X = TB_Odometer.X+rawLateral;
            TB_Odometer.Y = TB_Odometer.Y+rawAxial;
            TB_Odometer.Z = -TB_Master.drivetrain.getZ(); //TB_Odometer.Z+rawYaw; TODO: Inaccurate, replaced with imu for the time being
        }
    }

}
