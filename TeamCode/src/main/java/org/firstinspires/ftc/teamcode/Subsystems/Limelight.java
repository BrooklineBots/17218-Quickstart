package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Constants;

public class Limelight extends SubsystemBase {
  private final Limelight3A limelight;
  private final Telemetry telemetry;

  public Limelight(HardwareMap hardwareMap, Telemetry telemetry) {
    this.telemetry = telemetry;
    limelight = hardwareMap.get(Limelight3A.class, Constants.LimelightConstants.LIMELIGHT_NAME);

    limelight.start();

    // Default to AprilTag pipeline
    setPipeline(Constants.LimelightConstants.APRILTAG_PIPELINE);
  }

  public void setPipeline(int pipelineIndex) {
    limelight.pipelineSwitch(pipelineIndex);
  }

  public void setAprilTagPipeline() {
    setPipeline(Constants.LimelightConstants.APRILTAG_PIPELINE);
  }

  public void setObjectDetectionPipeline() {
    setPipeline(Constants.LimelightConstants.OBJECT_DETECTION_PIPELINE);
  }

  public LLResult getLatestResult() {
    return limelight.getLatestResult();
  }

  public boolean hasTarget() {
    LLResult result = getLatestResult();
    return result != null && result.isValid();
  }

  public Pose3D getBotPose() {
    LLResult result = getLatestResult();
    if (result != null && result.isValid()) {
      return result.getBotpose();
    }
    return null;
  }

  @Override
  public void periodic() {
    LLResult result = getLatestResult();
    if (result != null && result.isValid()) {
      telemetry.addData("Limelight Target Valid", true);
      telemetry.addData("Limelight Tx", result.getTx());
      telemetry.addData("Limelight Ty", result.getTy());

      Pose3D pose = result.getBotpose();
      if (pose != null) {
        telemetry.addData("Limelight Pose X", pose.getPosition().x);
        telemetry.addData("Limelight Pose Y", pose.getPosition().y);
      }
    } else {
      telemetry.addData("Limelight Target Valid", false);
    }
  }
}
