package org.firstinspires.ftc.teamcode.robonauts;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ClawArm {
    DcMotorEx armMotor = null;
    private final double zeroOffset = 30;

    private PIDController pidController=null;
    public static  double kp=0.001;//0.77;
    public static  double ki=0.00001;//0.003;
    public static  double kd=0.00001;//0.0001;

    public static double kf=0.0001;//0.03;


    //public static int targetDeg=0;
    private final double ticksPerDegree=1425.1 / 360;

    public ClawArm(HardwareMap hardwareMap) {
        armMotor = hardwareMap.get(DcMotorEx.class, "TestMotor");
        pidController = new PIDController(kp, ki,kd);
        armMotor.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void loop(int targetExtendPosition, Telemetry telemetry) {
        int armPos =  armMotor.getCurrentPosition();
        //double targetPositionTicks = ((targetDeg-zeroOffset)*ticksPerDegree)/armPos;
        double pid = pidController.calculate(armPos, targetExtendPosition);
        telemetry.addData("PID", pid);
        // double feedforward = Math.cos(Math.toRadians(targetMotorPosition / ticksPerDegree)) * kf;
        double angel = armPos / ticksPerDegree + zeroOffset;
        double feedforward = Math.sin(Math.toRadians(angel)) * kf;
        telemetry.addData("feed forward", feedforward);

        double armPositionInDeg = armPos/ticksPerDegree + zeroOffset;
        double power = pid + feedforward;
        telemetry.addData("power", power);
        telemetry.addData("targetExtendPosition", targetExtendPosition);

        armMotor.setTargetPosition(targetExtendPosition);
        armMotor.setPower(power);
    }

    public void extend() {
        //moveTo(-400);
    }

    public void release() {
        //moveTo(0);
    }

    public double getCurrentPosition() {
        return armMotor.getCurrentPosition();
    }
}
