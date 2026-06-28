package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;

@TeleOp(name = "Main TeleeOp")
public class TeleOpMain extends CommandOpMode {
  private RobotContainer robotContainer;

  @Override
  public void initialize() {
    robotContainer = new RobotContainer(hardwareMap, gamepad1, gamepad2, telemetry, this);
    robotContainer.configureTeleOp();
  }

  @Override
  public void run() {
    CommandScheduler.getInstance().run();
    robotContainer.run();
  }
}
