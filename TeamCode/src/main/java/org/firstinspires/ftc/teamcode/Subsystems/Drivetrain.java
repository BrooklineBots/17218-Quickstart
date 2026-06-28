package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.localization.localizers.PinpointLocalizer;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.hardware.RevIMU;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.pedroPathing.PedroConstants;

public class Drivetrain extends SubsystemBase {
  // Declare our motors
  private final Motor frontLeftMotor;
  private final Motor backLeftMotor;
  private final Motor frontRightMotor;
  private final Motor backRightMotor;

  private final Follower follower;

  private final RevIMU revIMU;

  private final PinpointLocalizer pinpoint;

  private final Telemetry telemetry;

  private double fieldHeadingOffset = 0.0; // in radians

  private final HardwareMap hwMap;

  private MecanumDrive drive = null;

  // Make sure your ID's match your configuration
  public Drivetrain(
      final HardwareMap hwMap,
      final Telemetry telemetry,
      final RobotContainer.gameMode gameMode,
      final PinpointLocalizer pinpoint) {
    this.hwMap = hwMap;
    this.telemetry = telemetry;
    this.pinpoint = pinpoint;

    frontLeftMotor = new Motor(hwMap, Constants.DriveConstants.FRONT_LEFT_MOTOR_ID);
    backLeftMotor = new Motor(hwMap, Constants.DriveConstants.BACK_LEFT_MOTOR_ID);
    frontRightMotor = new Motor(hwMap, Constants.DriveConstants.FRONT_RIGHT_MOTOR_ID);
    backRightMotor = new Motor(hwMap, Constants.DriveConstants.BACK_RIGHT_MOTOR_ID);

    frontLeftMotor.setInverted(false); // Invert this motor!
    backLeftMotor.setInverted(false); // Invert this motor!

    frontLeftMotor.setRunMode(
        Motor.RunMode
            .VelocityControl); // Set the run mode for the motors! Read the docs if you don't know
    // what this is or what to do here!
    frontRightMotor.setRunMode(Motor.RunMode.VelocityControl);
    backLeftMotor.setRunMode(Motor.RunMode.VelocityControl);
    backRightMotor.setRunMode(Motor.RunMode.VelocityControl);

    // Retrieve the IMU from the hardware map
    revIMU = new RevIMU(hwMap, Constants.DriveConstants.IMU_ID); // Constants.DriveConstants.IMU_ID

    revIMU.init(); // FIXME: The orientation is very likely wrong. Needs tested.

    /* Old parameters settings */
    // Adjust the orientation parameters to match your robot
    final IMU.Parameters parameters =
        new IMU.Parameters(
            new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT));

    drive =
        new MecanumDrive(
            frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor); // Read the docs
    if (gameMode == RobotContainer.gameMode.Auto) {
      follower = PedroConstants.createFollower(hwMap);
      telemetry.addData("Follower: ", "auto");
    } else {
      follower = null;
      telemetry.addData("Follower: ", gameMode);
      telemetry.addData("FrontL direction:", frontLeftMotor.getInverted());
    }
  }

  public void resetYaw() {
    revIMU.reset();
    setFieldHeadingOffset(0);
  }

  public double getBotHeading() {
    final double botHeading = revIMU.getHeading() - fieldHeadingOffset;
    return botHeading;
  }

  public void setFieldHeadingOffset(final double newOffset) {
    fieldHeadingOffset = newOffset;
  }

  public void setSpeeds(final double fl, final double bl, final double fr, final double br) {
    frontLeftMotor.set(fl);
    backLeftMotor.set(bl);
    frontRightMotor.set(fr);
    backRightMotor.set(br);
  }

  public void stopMotors() { // Stops all motors
    drive.stop();
  }

  public void driveRobotCentric(final GamepadEx Controller) {
    driveRobotCentric(-Controller.getLeftY(), Controller.getLeftX(), Controller.getRightX());
  }

  public void driveRobotCentric(final double forward, final double strafe, final double rotate) {
    drive.driveRobotCentric(strafe, forward, rotate);
  }

  public void driveFieldCentric(final GamepadEx Controller) {
    driveFieldCentric(
        -Controller.getLeftY(), -Controller.getLeftX(), -(Controller.getRightX() * 0.5));
  }

  public void driveFieldCentric(final double forward, final double strafe, final double rotate) {

    drive.driveFieldCentric(strafe, forward, rotate, revIMU.getRotation2d().getDegrees(), false);
  }

  public Follower getFollower() {
    return follower;
  }

  @Override
  public void periodic() {
    if (follower != null) follower.update();
  }
}
