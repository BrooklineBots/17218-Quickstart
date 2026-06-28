package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Utils.Utils;

public class DriveCommand extends CommandBase {
  private final Drivetrain drive;
  private final GamepadEx gamepad;

  private final double tolerance = 0.1; // Tolerance for joystick input

  public DriveCommand(final Drivetrain drive, final GamepadEx gamepad) {
    this.drive = drive;
    this.gamepad = gamepad;
    addRequirements(drive);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    if (!Utils.isWithinTolerance(0, gamepad.getLeftY(), tolerance)
        || !Utils.isWithinTolerance(0, gamepad.getLeftX(), tolerance)
        || !Utils.isWithinTolerance(0, gamepad.getRightX(), tolerance)) {
      drive.driveFieldCentric(gamepad);
    } else {
      drive.stopMotors();
    }
  }

  @Override
  public void end(final boolean interrupted) {
    if (interrupted || isFinished()) {
      drive.stopMotors();
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
