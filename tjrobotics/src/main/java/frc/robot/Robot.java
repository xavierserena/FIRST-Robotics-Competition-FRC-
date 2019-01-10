/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.cameraserver.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
  //Motores//
  WPI_VictorSPX delIzq = new WPI_VictorSPX(1);
  WPI_VictorSPX antIzq = new WPI_VictorSPX(2);
  WPI_VictorSPX delDer = new WPI_VictorSPX(3);
  WPI_VictorSPX antDer = new WPI_VictorSPX(4);

  //Agrupacion//
  SpeedControllerGroup izquierda = new SpeedControllerGroup(delIzq, antIzq);
  SpeedControllerGroup derecha = new SpeedControllerGroup(delDer, antDer);	
  
  //Definicion de base//
  DifferentialDrive base = new DifferentialDrive(izquierda, derecha);
  
  //Control//
  Joystick control = new Joystick(0);
  double speedConstant = 0.75;
  boolean IsPressed, WasPressed, arcade, cheesy = false;
  int xLeft     = 0; int yLeft,  A = 1; int triggers, B = 2;
  int xRight, X = 3; int yRight, Y = 4;


  //Camara//
  CameraServer server = CameraServer.getInstance();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    server.startAutomaticCapture();
  }

  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
  }

  /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void teleopPeriodic() {
    if(arcade)
      base.arcadeDrive(-control.getRawAxis(xLeft), control.getRawAxis(yRight));
    else
      base.tankDrive(-control.getRawAxis(xLeft), -control.getRawAxis(yRight));
    
    IsPressed = control.getRawButton(6) || control.getRawButton(7);          // get button state

    if (IsPressed && !WasPressed) arcade = !arcade; // toggle if pressed

    WasPressed = IsPressed;                   // remember button state
  }

  public void toggleDrive(){
    IsPressed = control.getRawButton(6) || control.getRawButton(7); //get button state

    if(IsPressed && !WasPressed){     //toggle if pressed
      if(!arcade && !cheesy){           //first case - arcade
        arcade = !arcade;                 //make arcade true
      }
      else if(arcade && !cheesy){       //second case - cheesy
        arcade = !arcade;                 //make arcade false
        cheesy = !cheesy;                 //make cheesy true
      }
      else{                             //last and default - tank
        cheesy = !cheesy;                 //make cheesy false
      }
    }  

    WasPressed = IsPressed;           //remember button state
  }
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    if(arcade)
      base.arcadeDrive(-control.getRawAxis(xLeft), control.getRawAxis(yRight));
    else if(cheesy)
      base.arcadeDrive(control.getRawAxis(triggers), control.getRawAxis(xLeft));
    else
      base.tankDrive(-control.getRawAxis(xRight), -control.getRawAxis(yRight));  
    toggleDrive();
      
    
  }
}
