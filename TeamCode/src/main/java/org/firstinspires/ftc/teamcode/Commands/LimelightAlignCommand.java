package org.firstinspires.ftc.teamcode.Commands;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;
import org.firstinspires.ftc.teamcode.Utils.Utils;

public class LimelightAlignCommand extends CommandBase {
  private final Drivetrain drive;
  private final Limelight limelight;
  private final GamepadEx gamepad;

  private final double tolerance = 0.1;

  // PID constants for alignment // FIXME: you will likely need to tune these!
  private final double kP = 0.02;
  private final double minPower = 0.05; // Minimum power to overcome friction

  public LimelightAlignCommand(Drivetrain drive, Limelight limelight, GamepadEx gamepad) {
    this.drive = drive;
    this.limelight = limelight;
    this.gamepad = gamepad;
    addRequirements(drive); // Overrides the default DriveCommand
  }

  @Override
  public void initialize() {
    // In case the pipeline was object
    limelight.setAprilTagPipeline();
  }

  @Override
  public void execute() {
    double forward = -gamepad.getLeftY();
    double strafe = -gamepad.getLeftX();
    double rotate = 0.0;

    LLResult result = limelight.getLatestResult();
    if (result != null && result.isValid()) {
      double tx = result.getTx();

      // If tx is positive (target to the right), we want to turn right (negative power in this
      // drivetrain).
      rotate = -tx * kP;

      // Add minimum power to overcome friction when tracking
      if (rotate > 0) {
        rotate += minPower;
      } else if (rotate < 0) {
        rotate -= minPower;
      }

      // Clamp to prevent spinning too fast
      rotate = Math.max(-0.5, Math.min(0.5, rotate));
    }

    // Drive field-centric with manual translation but auto-rotation
    drive.driveFieldCentric(forward, strafe, rotate);
  }

  @Override
  public boolean isFinished() {
    return !Utils.isWithinTolerance(0, gamepad.getRightX(), tolerance);
  }

  @Override
  public void end(boolean interrupted) {
    drive.stopMotors();
  }
}
