package org.firstinspires.ftc.teamcode;

public class Constants {

  public static class RobotConstants {
    public static final double ROBOT_MASS = 8.5; // in kilograms
    // pinpoint constants
    public static final double FORWARD_POD_OFFSET = 3.125;
    public static final double STRAFE_POD_OFFSET = -1.625;
  }

  public static class DriveConstants {
    public static final String FRONT_LEFT_MOTOR_ID = "frontLeftMotor";
    public static final String FRONT_RIGHT_MOTOR_ID = "frontRightMotor";
    public static final String BACK_LEFT_MOTOR_ID = "backLeftMotor";
    public static final String BACK_RIGHT_MOTOR_ID = "backRightMotor";

    public static final String IMU_ID = "imu";
  }

  public static class IntakeConstants {
    public static final String INTAKE_ID = "intakeMotor";

    public static final double INTAKE_OUT_VELOCITY = 7000;
    public static final double INTAKE_VELOCITY = 1500;
    public static final double EXPEL_VELOCITY = 1500;
    public static final double INTAKE_MAX_VELOCITY = 7000; // in RPM, 312
  }

  public static class OuttakeConstants {

    private static double rpmToRadPerSec(double rpm) {
      return rpm * (2 * Math.PI / 60);
    }

    public static final String OUTTAKE_ID = "outtakeMotor";

    public static final double OUTTAKE_MAX_VELOCITY = rpmToRadPerSec(12000);
    public static final double OUTTAKE_MOVEMENT_SPEED = 1500;
  }

  public static class BombshellServoConstants {
    public static final String BOMBSHELL_SERVO_ID = "bombshellServo";
    public static final long secondsToPush = 817;
  }

  public static class PinballServosConstants {
    public static final String PINBALL_RIGHT_ID = "pinballRight";
    public static final String PINBALL_LEFT_ID = "pinballLeft";

    public static final double rightClosed = 1;
    public static final double rightOpen = 0.5;
    public static final double leftClosed = 0.55;
    public static final double leftOpen = 1;
  }

  public static class EndgameConstants {
    public static final String RIGHT_ENDGAME_ID = "rightEndgameMotor";
    public static final String LEFT_ENDGAME_ID = "leftEndgameMotor";

    public static final long ENDGAME_TIME =
        1000; // TODO: change; (milliseconds it takes to move up)
    public static final double ENDGAME_VELOCITY = 500;
  }
}
