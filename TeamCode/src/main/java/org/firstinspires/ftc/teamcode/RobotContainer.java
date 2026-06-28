package org.firstinspires.ftc.teamcode;

import com.pedropathing.ftc.localization.localizers.PinpointLocalizer;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.AutoChooser;
import org.firstinspires.ftc.teamcode.Commands.DriveCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;

public class RobotContainer {
  // Subsystems
  private Drivetrain drive;
  private Drivetrain autoDrive;
  private PinpointLocalizer pinpoint;

  // Dependencies
  private final HardwareMap hardwareMap;
  private final Telemetry telemetry;
  private final GamepadEx gamepad1;
  private final GamepadEx gamepad2;
  private final CommandOpMode JavaBot;

  public enum gameMode {
    Auto,
    TeleOp
  }

  private gameMode currentGameMode = null;

  public enum AutoMode { // Enum of all valid autonomous modes
    DoNothingAuto
  }

  public RobotContainer(
      final HardwareMap hardwareMap,
      final Gamepad gamepad1,
      final Gamepad gamepad2,
      final Telemetry telemetry,
      final CommandOpMode JavaBot) {
    this.hardwareMap = hardwareMap;
    this.gamepad1 = new GamepadEx(gamepad1);
    this.gamepad2 = new GamepadEx(gamepad2);
    this.telemetry = telemetry;
    this.JavaBot = JavaBot;
  }

  public CommandOpMode getJavaBot() {
    return JavaBot;
  }

  public void initializeSubsystems() {
    drive = new Drivetrain(hardwareMap, telemetry, currentGameMode, pinpoint);
    autoDrive = new Drivetrain(hardwareMap, telemetry, currentGameMode, pinpoint);
    // Register subsystems with scheduler
    CommandScheduler.getInstance().registerSubsystem(drive, autoDrive);
  }

  public void configureTeleOp() {
    currentGameMode = gameMode.TeleOp;
    initializeSubsystems();

    // Default commands
    drive.setDefaultCommand(new DriveCommand(drive, gamepad1));
    // Button bindings
    configureButtonBindings();
  }

  public void configureAuto() {
    currentGameMode = gameMode.Auto;
    initializeSubsystems();
    registerNamedCommands();

    // Schedule Auto Chooser
    CommandScheduler.getInstance().schedule(new AutoChooser(this, gamepad1, telemetry));
  }

  private void configureButtonBindings() {
    // TODO: Gamepad 1 buttons

    // TODO: gamepad2

  }

  public void scheduleAutoCommands(final AutoMode selectedAutoMode) {
    telemetry.addData("Starting Auto Mode", selectedAutoMode);
    telemetry.update();

    if (selectedAutoMode == AutoMode.DoNothingAuto) {
      CommandScheduler.getInstance().schedule(new InstantCommand());
    } else {
      telemetry.addLine("No auto was selected! There was likely an error.");
      telemetry.update();
    }
  }

  private void registerNamedCommands() {

    // TODO: Register commands

  }

  public void run() {
    if (currentGameMode == gameMode.TeleOp) {
      gamepad1.readButtons();
      gamepad2.readButtons();
    }

    // Smart Telemetry Logging
    telemetry.addData("Current Mode", currentGameMode);

    if (currentGameMode == gameMode.Auto && autoDrive.getFollower() != null) {
      telemetry.addData("Auto Pose X", autoDrive.getFollower().getPose().getX());
      telemetry.addData("Auto Pose Y", autoDrive.getFollower().getPose().getY());
      telemetry.addData("Auto Heading", Math.toDegrees(autoDrive.getFollower().getPose().getHeading()));
    } else if (currentGameMode == gameMode.TeleOp) {
      telemetry.addData("Robot Heading", drive.getBotHeading());
    }

    // Add any dashboard drawing or bulk cache clearing here in the future
    
    telemetry.update();
  }
}
