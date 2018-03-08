// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc4505.Robot18.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc4505.Robot18.Robot;
import org.usfirst.frc4505.Robot18.RobotMap;

/**
 *
 */
public class ElevatorDownToNextLimit extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public ElevatorDownToNextLimit() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.elevator);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	Robot.elevator.updateElevatorConstants();
    	if (Robot.elevator.isElevatorMiddle()) {
    		Robot.elevator.state = 2;
    	} else if (Robot.elevator.isElevatorUp()) {
    		Robot.elevator.state = 4;
    	}
    	Robot.elevator.set(RobotMap.elDownSpd);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
    	if (Robot.elevator.isElevatorDown()) {
    		return true;
    	}
        if (Robot.elevator.state==2) {
        	return Robot.elevator.isElevatorDown();
        } else {
        	return Robot.elevator.isElevatorMiddle();
        }
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	Robot.elevator.stop();
    	Robot.elevator.state--;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	Robot.elevator.stop();
    }
}
