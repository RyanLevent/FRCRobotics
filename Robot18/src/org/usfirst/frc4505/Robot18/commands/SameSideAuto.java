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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc4505.Robot18.Robot;
import org.usfirst.frc4505.Robot18.RobotMap;

/**
 *
 */
public class SameSideAuto extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	public double tStart, tState, angle;
	public int state;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public SameSideAuto() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	state = 0;
    	tStart = Timer.getFPGATimestamp();
    	Robot.drivetrain.resetSensors();
    	angle = Robot.drivetrain.getAngle();
    	Robot.drivetrain.sendByteToI2C(1);
    	tState = tStart;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	//if sees same color, drops in switch, otherwise drives to line
    	while((Timer.getFPGATimestamp()-tStart) <= 15.0 && state != -1 && !Robot.oi.A.get()) {
    		if (state == 0) {//drive 80 inches
    			Robot.drivetrain.driveStraight(-RobotMap.autoSpd, angle, Robot.drivetrain.getAngle(), 0.5);//check direction
    			if (Robot.drivetrain.goneInches(80.0-RobotMap.robotLen) || (Timer.getFPGATimestamp()-tState) > 8.0) {//if finished
    				Robot.drivetrain.stop();
    				tState = Timer.getFPGATimestamp();
    				state++;
    			}
    		} else if (state == 1) {//get reading from i2c
    			boolean b = Robot.drivetrain.readFromI2C(1);
    			if ((Timer.getFPGATimestamp()-tState) > 1.0) {
//    				state = 5;
    				state++;
    			} else if (!b) {
    				Robot.drivetrain.stop();
    				if ((RobotMap.readIn[0] == 1 && RobotMap.isBlue) || 
    						(RobotMap.readIn[0] == 2 && !RobotMap.isBlue)) {//check that 1 is blue, 2 is red
        				tState = Timer.getFPGATimestamp();
    					state++;
    				} else {
        				tState = Timer.getFPGATimestamp();
    					state = 5;
    				}
    			}
    		} else if (state == 2) {//drive up to switch
    			Robot.drivetrain.driveStraight(-RobotMap.autoSpd, angle, Robot.drivetrain.getAngle(), 0.5);//check direction
    			if (Robot.drivetrain.goneInches(140.0-RobotMap.robotLen) || (Timer.getFPGATimestamp()-tStart) > 14.0) {//if finished
    				tState = Timer.getFPGATimestamp();
    				Robot.drivetrain.stop();
    				state++;
    			}
    		} else if (state == 3) {//lift elevator
    			Robot.elevator.set(0.5);//check pow & dir
    			if (Robot.elevator.isElevatorMiddle() || (Timer.getFPGATimestamp()-tState) > 3.0) {
    				Robot.elevator.stop();
    				tState = Timer.getFPGATimestamp();
    				state++;
    			}
    		}  else if (state == 4) {//spit cube out
    			Robot.intakeWheels.set(-0.8);//check direction
    			if ((Timer.getFPGATimestamp()-tState) > 2.0) {//if finished
    				Robot.intakeWheels.stop();
    				state = -1;
    			}
    		}  else if (state == 5) {//drive up to line
    			Robot.drivetrain.driveStraight(-RobotMap.autoSpd, angle, Robot.drivetrain.getAngle(), 0.5);//check direction
    			if (Robot.drivetrain.goneInches(130.0-RobotMap.robotLen)) {//if finished
    				Robot.drivetrain.stop();
    				state = -1;
    			}
    		}
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return (Timer.getFPGATimestamp()-tStart) >= 15.0 || Robot.oi.A.get();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	Robot.drivetrain.stop();
    	Robot.elevator.stop();
    	Robot.intakeWheels.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	end();
    }
}
