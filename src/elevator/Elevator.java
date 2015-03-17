/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package elevator;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author n-scott.1
 */
public class Elevator {
    
    //Variables
    private JLabel floors[];
    private boolean isOpen[];
    private boolean moving;
    private int floorCount;
    private int currFloor;
    private int direction;
    private int target;
    private final Timer movementTimer;
    private final Timer doorTimer; 
    
    public Elevator(){
        floors = new JLabel[3];
        floorCount = 0;
        currFloor = 0;
        direction = 1;
        moving = false;
        
        movementTimer = new Timer(2000, new ActionListener(){
           public void actionPerformed(ActionEvent e){
                moveElevator();
           } 
        });
        
        doorTimer = new Timer(1000, new ActionListener(){
           public void actionPerformed(ActionEvent e){
                toggleDoors();
                if(moving)
                    movementTimer.start();
           } 
        });
        doorTimer.setRepeats(false);
        
        isOpen = new boolean[]{false,false,false};
    }
    
    private void toggleDoors(){
        isOpen[currFloor] = !isOpen[currFloor];
        for (JLabel flr : floors){
            if(flr == floors[currFloor] && isOpen[currFloor]){
                flr.setText("]  [");
            }else{
                flr.setText("=][=");
            }
        }
        setActiveFloor(-1);
    }
    
    public void addFloor(JLabel floor){
        floors[floorCount] = floor;
        floorCount++;
    }
    
    public void addFloors(JLabel floor[]){
        for (JLabel newFloor : floor) {
            floors[floorCount] = newFloor;
            floorCount++;
        }
    }
    
    public int checkDistance(JLabel floor, int targetDirection){
        
        if(targetDirection == 0){
            targetDirection = direction;
        }
        
        int distance = 0;
        for(int i = 0; i < floors.length; i++){
            if(floors[i] == floor){
                target = i;
                break;
            }
        }
             
        distance = (target-currFloor) * direction;
        
        if(currFloor != 0 || currFloor != floorCount){
            if(distance < 0 || targetDirection != direction){ //Going in wrong direction
                if(direction == 1){ //Going Up
                    distance = (floorCount-1)-currFloor;
                    distance += (floorCount-1)-target;
                }else{ //Going Down
                    distance = currFloor;
                    distance += target;
                }
            }
        }
        
        System.out.println(distance);
        
        return distance;
    }
    
    public void changeFloor(JLabel floor, int targetDirection){
        if(!doorTimer.isRunning() && !movementTimer.isRunning()){
            if(targetDirection == 0){
                targetDirection = direction;
            }
        
            int start = currFloor;
            int distance = 0;
            for(int i = 0; i < floors.length; i++){
                if(floors[i] == floor){
                    target = i;
                    break;
                }
            }
            
            if(start == target && direction == targetDirection){ 
                    if(!isOpen[currFloor]){
                        moving = false;
                        doorTimer.start();
                    }
            }else if(isOpen[currFloor]){
                doorTimer.start();
                moving = true;
            }else{
                movementTimer.start();
            }
        }
    }
    
    private void moveElevator(){
        if(currFloor == 0)
            direction = 1;
        if(currFloor == floorCount-1){
            direction = -1;
        }
        
        setActiveFloor(currFloor+direction);
        
        if(currFloor == target){
            movementTimer.stop();
            moving = false;
            doorTimer.start();
        }
    }
    
    public void setActiveFloor(int floorNum){
        
        if(floorNum == -1)
            floorNum = currFloor;
        
        for(int i = 0; i < floorCount; i++){
            if(i == floorNum){
                if(isOpen[i])
                    floors[i].setBackground(Color.GREEN);
                else
                    floors[i].setBackground(Color.YELLOW);
            }else{
                floors[i].setBackground(Color.WHITE);
            }          
        }
        currFloor = floorNum;
        System.out.println("Floor:" + floorNum);
        
        
    }
    
}
