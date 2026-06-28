package org.firstinspires.ftc.teamcode;

import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.ftc.localization.localizers.PinpointLocalizer;
import com.pedropathingplus.pathing.NamedCommands;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.AutoChooser;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.B12;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.B3;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.BlueLeaveBigTri;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.BlueLeaveLittleTri;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.BlueTwelveArtifact;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.BlueTwelveArtifactFromObelisk;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.CommandTests;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.R3;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.RedLeaveBigTri;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.RedLeaveLittleTri;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.RedTwelveArtifact;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.RedTwelveArtifactFromObelisk;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.bluePotato;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.reg;
import org.firstinspires.ftc.teamcode.Commands.AutoCommands.zendayaHatTheory;
import org.firstinspires.ftc.teamcode.Commands.DriveCommand;
import org.firstinspires.ftc.teamcode.Commands.ExpelIntakeCommand;
import org.firstinspires.ftc.teamcode.Commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.Commands.IntakeOutCommand;
import org.firstinspires.ftc.teamcode.Commands.LowerEndgameCommand;
import org.firstinspires.ftc.teamcode.Commands.OuttakeCommand;
import org.firstinspires.ftc.teamcode.Commands.RaiseEndgameCommand;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Endgame;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Outtake;
import org.firstinspires.ftc.teamcode.Subsystems.PinballServos;

public class RobotContainer {
  // Subsystems
  private Drivetrain drive;
  private Drivetrain autoDrive;
  private Intake intake;
  private Outtake outtake;
  private PinpointLocalizer pinpoint;
  private PinballServos pinballs;

  private Endgame endgame;

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

  private AutoMode currentAuto;

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
    CommandScheduler.getInstance()
        .registerSubsystem(drive, autoDrive);
  }

  public void configureTeleOp() {
    currentGameMode = gameMode.TeleOp;
    initializeSubsystems();

    // Default commands
    drive.setDefaultCommand(new DriveCommand(drive, gamepad1));
    // Button bindings
    configureButtonBindings();
  }

  public void configureAuto() { // Note that I'm still working on this. It does not work yet.
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

    try {
      if (selectedAutoMode == AutoMode.DoNothingAuto) {
        CommandScheduler.getInstance().schedule(new InstantCommand());
      } else if (selectedAutoMode == AutoMode.reg) {
        CommandScheduler.getInstance().schedule(new reg(autoDrive, hardwareMap));
      } else if (selectedAutoMode == AutoMode.BlueTwelveArtifact) {
        CommandScheduler.getInstance().schedule(new BlueTwelveArtifact(autoDrive, intake, outtake));
      } else if (selectedAutoMode == AutoMode.RedTwelveArtifact) {
        CommandScheduler.getInstance().schedule(new RedTwelveArtifact(autoDrive, intake));
      } else if (selectedAutoMode == AutoMode.BlueTwelveArtifactFromObelisk) {
        CommandScheduler.getInstance()
            .schedule(new BlueTwelveArtifactFromObelisk(autoDrive, intake));
      } else if (selectedAutoMode == AutoMode.RedTwelveArtifactFromObelisk) {
        CommandScheduler.getInstance()
            .schedule(new RedTwelveArtifactFromObelisk(autoDrive, intake));
      } else if (selectedAutoMode == AutoMode.BlueLeaveLittleTri) {
        CommandScheduler.getInstance().schedule(new BlueLeaveLittleTri(autoDrive));
      } else if (selectedAutoMode == AutoMode.RedLeaveLittleTri) {
        CommandScheduler.getInstance().schedule(new RedLeaveLittleTri(autoDrive));
      } else if (selectedAutoMode == AutoMode.BlueLeaveBigTri) {
        CommandScheduler.getInstance().schedule(new BlueLeaveBigTri(autoDrive));
      } else if (selectedAutoMode == AutoMode.RedLeaveBigTri) {
        CommandScheduler.getInstance().schedule(new RedLeaveBigTri(autoDrive));
      } else if (selectedAutoMode == AutoMode.bluePotato) {
        CommandScheduler.getInstance().schedule(new bluePotato(autoDrive, hardwareMap, telemetry));
      } else if (selectedAutoMode == AutoMode.zendayaHatTheory) {
        CommandScheduler.getInstance()
            .schedule(new zendayaHatTheory(autoDrive, intake, outtake, hardwareMap, telemetry));
      } else if (selectedAutoMode == AutoMode.CommandTests) {
        CommandScheduler.getInstance()
            .schedule(new CommandTests(autoDrive, intake, hardwareMap, telemetry));
      } else if (selectedAutoMode == AutoMode.B12) {
        CommandScheduler.getInstance()
            .schedule(new B12(autoDrive, outtake, intake, hardwareMap, telemetry));
      } else if (selectedAutoMode == AutoMode.R3) {
        CommandScheduler.getInstance()
            .schedule(new R3(autoDrive, outtake, intake, hardwareMap, telemetry));
      } else if (selectedAutoMode == AutoMode.B3) {
        CommandScheduler.getInstance()
            .schedule(new B3(autoDrive, outtake, intake, hardwareMap, telemetry));
      } else {
        telemetry.addLine("No auto was selected! There was likely an error.");
        telemetry.update();
      }
    } catch (final IOException error) {
      telemetry.addLine("A critical IOException error has occurred. Doing nothing. ");
      telemetry.addLine(error.toString());
      telemetry.update();
      CommandScheduler.getInstance().schedule(new InstantCommand());
    }
  }

  private void registerNamedCommands() {

    // TODO: Register commands
 
  }

  public void run() {
    // telemetry`
    // telemetry.addData("Currently shooting", outtake.getPower());
    //    telemetry.update();

    if (currentGameMode == gameMode.TeleOp) {
      gamepad1.readButtons();
      gamepad2.readButtons();
    }
    if (currentGameMode == gameMode.Auto) {
      //      telemetry.addData("Pos x", autoDrive.getFollower().getPose().getX());
      //      telemetry.addData("Pos y", autoDrive.getFollower().getPose().getY());
      //      telemetry.addData("Heading: ", autoDrive.getFollower().getPose().getHeading());
    }
    //    if (currentGameMode == gameMode.Auto) {
    //      dashboardPoseTracker.update();
    //      Drawing.drawPoseHistory(dashboardPoseTracker, "#4CAF50");
    //      Drawing.drawRobot(robot.follower.getPose(), "#4CAF50");
    //      Drawing.sendPacket();
    //
    //      // DO NOT REMOVE! Removing this will return stale data since bulk caching is on Manual
    // mode
    //      // Also only clearing the control hub to decrease loop times
    //      // This means if we start reading both hubs (which we aren't) we need to clear both
    //      robot.ControlHub.clearBulkCache();
    //    }
  }
}
