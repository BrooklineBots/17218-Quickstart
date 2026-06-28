package org.firstinspires.ftc.teamcode.Commands.AutoCommands;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.RobotContainer;
import org.firstinspires.ftc.teamcode.RobotContainer.AutoMode;

public class AutoChooser extends CommandBase {
  private final AutoMode[] modes = AutoMode.values();
  private final Telemetry telemetry;
  private final GamepadEx gamepad;
  private int selectedIndex = 0;
  private AutoMode selected;
  private long lastButtonPressTime = 0;
  private final RobotContainer robotContainer;

  public AutoChooser(
      final RobotContainer robotContainer, final GamepadEx gamepad, final Telemetry telemetry) {
    this.robotContainer = robotContainer;
    this.gamepad = gamepad;
    this.telemetry = telemetry;
    this.selected = modes[selectedIndex]; // Initialize with first mode
  }

  @Override
  public void initialize() {
    telemetry.addLine("Auto Selection Active");
    telemetry.addLine("Use D-Pad Left/Right to choose mode");
    telemetry.addLine("Gamepad will start selected auto");
    updateTelemetry();
  }

  @Override
  public void execute() {
    // Debounce button presses
    long currentTime = System.currentTimeMillis();
    if (currentTime - lastButtonPressTime > 300) { // 300ms debounce
      if (gamepad.isDown(GamepadKeys.Button.DPAD_RIGHT)) {
        selectedIndex = (selectedIndex + 1) % modes.length;
        selected = modes[selectedIndex];
        lastButtonPressTime = currentTime;
        updateTelemetry();
      } else if (gamepad.isDown(GamepadKeys.Button.DPAD_LEFT)) {
        selectedIndex = (selectedIndex - 1 + modes.length) % modes.length;
        selected = modes[selectedIndex];
        lastButtonPressTime = currentTime;
        updateTelemetry();
      }
    }

    // Update telemetry periodically to ensure it's visible
    if (currentTime % 1000 < 50) { // Update roughly once per second
      updateTelemetry();
    }
  }

  private void updateTelemetry() {
    telemetry.clear();
    telemetry.addLine("=== AUTONOMOUS SELECTION ===");
    telemetry.addLine("Use D-Pad Left/Right to choose");
    telemetry.addLine("");

    for (int i = 0; i < modes.length; i++) {
      if (i == selectedIndex) {
        telemetry.addLine(">>> " + modes[i].name() + " <<<");
      } else {
        telemetry.addLine("    " + modes[i].name());
      }
    }

    telemetry.addLine("");
    telemetry.update();
  }

  @Override
  public void end(final boolean interrupted) {
    telemetry.clear();
    telemetry.addData("Selected Auto Mode", selected);
    telemetry.addLine("Starting autonomous...");
    telemetry.update();

    // Schedule the selected autonomous commands
    robotContainer.scheduleAutoCommands(selected);
  }

  public AutoMode getSelectedMode() {
    return selected;
  }

  @Override
  public boolean isFinished() {
    return robotContainer.getJavaBot().isStarted() || robotContainer.getJavaBot().isStopRequested();
  }
}
