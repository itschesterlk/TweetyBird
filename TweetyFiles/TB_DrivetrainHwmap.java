package org.firstinspires.ftc.teamcode.TweetyBird.TweetyFiles;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public class TB_DrivetrainHwmap {
    //Define Hardware Map
    HardwareMap hwMap = null;

    //Define Telemetry
    Telemetry telemetry;

    //Elapsed Time Timers
    ElapsedTime waitTimer = new ElapsedTime();

    //Define Motors
    public DcMotor frontLeft  = null;
    public DcMotor frontRight = null;
    public DcMotor backLeft   = null;
    public DcMotor backRight  = null;

    //Defining Dead Wheels
    public DcMotor leftEncoder, rightEncoder, backEncoder;

    //Define Servos
    public Servo podLeft         = null;
    public final double podLUp   = 0.35; //Heights for each pod
    public final double podLDown = 0.69;

    public Servo podRight        = null;
    public final double podRUp   = 0.58;
    public final double podRDown = 0.09;

    public Servo podBack         = null;
    public final double podBUp   = 0.6;
    public final double podBDown = 0.42;

    //IMU
    public BNO055IMU imu = null;


    public void init(OpMode opMode) {
        //Setting Hardware Map
        hwMap = opMode.hardwareMap;

        //Init Motors
        frontLeft = hwMap.get(DcMotor.class, "FL");
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);

        frontRight = hwMap.get(DcMotor.class, "FR");
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setDirection(DcMotor.Direction.FORWARD);

        backLeft = hwMap.get(DcMotor.class, "BL");
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        backRight = hwMap.get(DcMotor.class, "BR");
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        //Init Dead Wheels
        leftEncoder = hwMap.get(DcMotor.class, "FL");
        leftEncoder.setDirection(DcMotorSimple.Direction.FORWARD);

        rightEncoder = hwMap.get(DcMotor.class, "encoderRight");
        rightEncoder.setDirection(DcMotorSimple.Direction.FORWARD);

        backEncoder = hwMap.get(DcMotor.class, "FR");
        backEncoder.setDirection(DcMotorSimple.Direction.FORWARD);

        //Init Servos
        podLeft = hwMap.servo.get("podL");
        podRight = hwMap.servo.get("podR");
        podBack = hwMap.servo.get("podB");

        //Init IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }

    public void podsUp() {
        podLeft.setPosition(podLUp);
        podRight.setPosition(podRUp);
        podBack.setPosition(podBUp);
        waitSeconds(1);
    }

    public void podsDown() {
        podLeft.setPosition(podLDown);
        podRight.setPosition(podRDown);
        podBack.setPosition(podBDown);
    }

    //Simple hold program until time that is specified
    private void waitSeconds(double seconds) {
        //Resetting Timer
        waitTimer.reset();
        while (waitTimer.seconds() <= seconds);
    }

    //Return IMU Values
    public double getZ() {
        return (imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle);
    }
}
