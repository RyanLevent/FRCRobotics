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
public class BothSidesAuto extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	public double tStart, tState, angle;
	public int state;
	public boolean correctColor;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public BothSidesAuto() {

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
    	correctColor = true;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	while((Timer.getFPGATimestamp()-tStart) <= 15.0 && state != -1 && !Robot.oi.A.get()) {
    		if (state == 0) {//drive 80 inches
    			Robot.drivetrain.driveStraight(-RobotMap.autoSpd, angle, Robot.drivetrain.getAngle(), 0.5);//check direction
    			if (Robot.drivetrain.goneInches(80.0-RobotMap.robotLen) || (Timer.getFPGATimestamp()-tState) > 5.0) {
    				Robot.drivetrain.stop();
    				tState = Timer.getFPGATimestamp();
    				state++;
    			}
    		} else if (state == 1) {//get reading from i2c
    			boolean b = Robot.drivetrain.readFromI2C(1);
    			if ((Timer.getFPGATimestamp()-tState) > 1.0) {
    				state = 8;
    				//change to drive up to line
    			} else if (!b) {
    				Robot.drivetrain.stop();
    				tState = Timer.getFPGATimestamp();
    				state++;
    				if ((RobotMap.readIn[0] == 1 && RobotMap.isBlue) || 
    						(RobotMap.readIn[0] == 2 && !RobotMap.isBlue)) {//check that 1 is blue, 2 is red
    					correctColor = true;
    					state = 5;
    				} else {
    					correctColor = false;
    					state++;
    				}
    			}
    		} else if (state == 2) {//turn 90 degrees ccw
    			Robot.drivetrain.drive(0, -RobotMap.autoSpd);//check dir
    			if ((Robot.drivetrain.getAngle()-angle) <= -90.0 || (Timer.getFPGATimestamp()-tState) > 3.0) {
    				Robot.drivetrain.stop();
    				tState = Timer.getFPGATimestamp();
    				Robot.drivetrain.resetAllTalons();
    				state++;
    			}
    		} else if (state == 3) {//drive 108 inches to left side
    			Robot.drivetrain.driveStraight(-RobotMap.autoSpd, angle, Robot.drivetrain.getAngle(), 0.5);//check dir
    			if (Robot.drivetrain.goneInches(108.0) || (Timer.getFPGATimestamp()-tState) > 5.0) {
    				Robot.drivetrain.stop();
    				tState = Timer.getFPGATimestamp();
    				state++;
    			}
    		} else if (state == 4) {//turn 90 degrees cw
    			Robot.drivetrain.drive(0, RobotMap.autoSpd);//check dir
    			if ((Robot.drivetrain.getAngle()-angle) >= 0 || (Timer.getFPGATimestamp()-tState) > 3.0) {
    				Robot.drivetrain.stop();
    				tState = Timer.getFPGATimestamp();
    				Robot.drivetrain.resetAllTalons();
    				state++;
    			}
    		} else if (state == 5) {//drive 60 (CHECK AND SUBTRACT LATER) inches up to switch
    			Robot.drivetrain.driveStraight(-RobotMap.autoSpd, angle, Robot.drivetrain.getAngle(), 0.5);//check dir
    			if (Robot.drivetrain.goneInches(60.0-RobotMap.robotLen) || (Timer.getFPGATimestamp()-tState) > 5.0) {
    				Robot.drivetrain.stop();
    				tState = Timer.getFPGATimestamp();
    				state++;
    			}
    		} else if (state == 6) {//lift elevator
    			Robot.elevator.set(RobotMap.elUpSpd);//check pow & dir
    			if (Robot.elevator.isElevatorMiddle() || (Timer.getFPGATimestamp()-tState) > 3.0 || (Timer.getFPGATimestamp()-tStart) > 14.0) {
    				Robot.elevator.stop();
    				tState = Timer.getFPGATimestamp();
    				state++;
    			}
    		} else if (state == 7) {//spit out cube
    			Robot.intakeWheels.set(-1.0);//check direction
    			if ((Timer.getFPGATimestamp()-tState) > 2.0) {//if finished
    				Robot.intakeWheels.stop();
    				state = -1;
    			}
    		} else if (state == 8) {//pixy not working, drive up to line
    			Robot.drivetrain.driveStraight(-RobotMap.autoSpd, angle, Robot.drivetrain.getAngle(), 0.5);
    			if (Robot.drivetrain.goneInches(120.0)) {
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
