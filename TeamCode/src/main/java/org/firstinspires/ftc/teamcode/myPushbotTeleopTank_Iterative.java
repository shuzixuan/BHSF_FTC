/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Pushbot: Teleop Tank", group="Pushbot")
//@Disabled
public class myPushbotTeleopTank_Iterative extends OpMode{

    /* Declare OpMode members. */
    final private double obsThreshold =0.15;

    private HardwarePushbot robot       = new HardwarePushbot(); // use the class created to define a Pushbot's hardware

    private shootThread shoot;
    private collector collect;
    private shoot_servo reloader;
    private releaseLadder dropper = new releaseLadder(robot);
                                                         // could also use HardwarePushbotMatrix class.
//    double          clawOffset  = 0.0 ;                  // Servo mid position
//    final double    CLAW_SPEED  = 0.02 ;                 // sets rate to move servo


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        telemetry.addData("x",  "%.2f", gamepad1.left_stick_x);
        telemetry.addData("y",  "%.2f", gamepad1.left_stick_y);
        robot.pushGamepad(gamepad1.left_stick_x, gamepad1.left_stick_y);

        if( gamepad1.a && (shoot == null || !shoot.isAlive()) ){
            shoot = new shootThread(robot);
            shoot.start();
        }
        if( gamepad1.b && (collect == null || !collect.isAlive()) ) {
            collect = new collector(robot, gamepad1);
            collect.start();
        }

        if(gamepad1.x||((robot.eye.getLightDetected() >= obsThreshold || robot.isReadytoLoad) && !robot.isLoaded && (reloader == null || !reloader.isAlive())) )        {
            reloader = new shoot_servo(robot);
            reloader.start();
        }

        if(gamepad1.dpad_up || gamepad1.dpad_down)
        {
            if(!robot.isReleased && !dropper.isAlive()){
                dropper.start();
            }
            if(!dropper.isAlive()) {
                if(gamepad1.dpad_up)    robot.Mladder.setPower(-1.0);
                if(gamepad1.dpad_down)  robot.Mladder.setPower(1.0);
            }
        }
        if(!dropper.isAlive() && !gamepad1.dpad_down && !gamepad1.dpad_up)
            robot.Mladder.setPower(0.0);

        telemetry.addData("servo", "%.2f", robot.wrench.getPosition());
        telemetry.addData("ODS", "%.2f", robot.eye.getLightDetected());
        telemetry.addData("left",  "%.2f", robot.powerl);
        telemetry.addData("right",  "%.2f", robot.powerr);
        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
//        left = -gamepad1.left_stick_y;
//        right = -gamepad1.right_stick_y;
//        robot.leftMotor.setPower(left);
//        robot.rightMotor.setPower(right);

        // Use gamepad left & right Bumpers to open and close the claw
//        if (gamepad1.right_bumper)
//            clawOffset += CLAW_SPEED;
//        else if (gamepad1.left_bumper)
//            clawOffset -= CLAW_SPEED;

        // Move both servos to new position.  Assume servos are mirror image of each other.
//        clawOffset = Range.clip(clawOffset, -0.5, 0.5);
//        robot.leftClaw.setPosition(robot.MID_SERVO + clawOffset);
//        robot.rightClaw.setPosition(robot.MID_SERVO - clawOffset);

        // Use gamepad buttons to move the arm up (Y) and down (A)
//        if (gamepad1.y)
//            robot.armMotor.setPower(robot.ARM_UP_POWER);
//        else if (gamepad1.a)
//            robot.armMotor.setPower(robot.ARM_DOWN_POWER);
//        else
//            robot.armMotor.setPower(0.0);

        // Send telemetry message to signify robot running;
//        telemetry.addData("claw",  "Offset = %.2f", clawOffset);
//        telemetry.addData("left",  "%.2f", left);
//        telemetry.addData("right", "%.2f", right);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

        robot.stop();
    }

}
