package org.firstinspires.ftc.teamcode.TweetyBird.TweetyFiles;

public class TB_Config {
    //Physical Robot Measurements
        //Dead Wheel Locations
        public static final double radiusToSideEncoder = 6.125; //Distance from center of rotation to side encoder in inches
        public static final double sideEncoderOffset = 1.52; //Offset (+forward -backward) off the center of the side in inches
        public static final double radiusToBackEncoder = 2; //Distance from center of rotation to back encoder in inches

        //Dead Wheels
        public static final int ticksPerEncoderRotation = 8192; //Amount of counts per encoder rotation
        public static final double encoderWheelRadius = 1; //Size from center of wheel to outer edge of wheel in inches
        public static final double ticksPerInch = (double)ticksPerEncoderRotation/((double)2*Math.PI*encoderWheelRadius); //!!Created automatically, no touch!!

    //TB_Master
    public static final double waypointIncrement = 5; //How frequently waypoints are created in inches, too long looses accuracy, too short causes robot to jerk, find sweet spot

    //TB_Mover
    public static final double minSpeed = 0.2;
    public static final double maxSpeed = 0.7;
}
