package org.firstinspires.ftc.teamcode;

/**
 * Created by Administrator on 2017/2/22.
 */

public class shoot_servo extends Thread {
    public HardwarePushbot robot;
    public shoot_servo(HardwarePushbot rob){
        robot=rob;
    }
    @Override
    public void run() {
        robot.wrench.setPosition(0);
        try {
            Thread.sleep(250);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.wrench.setPosition(1);
        robot.isLoaded=true;
    }
}
