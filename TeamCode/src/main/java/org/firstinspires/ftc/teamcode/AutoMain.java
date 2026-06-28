package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.CommandScheduler;

@Autonomous(name = "Main Autonomous")
public class AutoMain extends CommandOpMode {
  private RobotContainer robotContainer;

  @Override
  public void initialize() {
    telemetry.addLine("AutoMain: Initializing...");
    telemetry.update();

    robotContainer = new RobotContainer(hardwareMap, gamepad1, gamepad2, telemetry, this);
    robotContainer.configureAuto();

    telemetry.addLine("AutoMain: Initialization complete");
    telemetry.update();
  }

  @Override
  public void initialize_loop() {
    CommandScheduler.getInstance().run();
    super.initialize_loop();
  }

  @Override
  public void run() {
    CommandScheduler.getInstance().run();
    robotContainer.run();

    // Debug: show scheduled commands
    //      telemetry.addData("Scheduled Commands", CommandScheduler.getInstance().);
    telemetry.update();
  }
}
