package vacuumagentproject;

import java.util.Arrays;

/**
 * Created by bHodges on 1/25/16.
 */

public class BehReflexModelAgent extends VacuumAgent {

    int[][] visitedCount = new int[500][500];
    boolean[][] isObsticle = new boolean[500][500];
    boolean check;
    int oldR, oldC, currentRow, currentCol;
    VacuumAction oldAction;

    public BehReflexModelAgent() {
        fillArrays();
        check = false;
        oldR = 0;
        oldC = 0;
        currentRow = 0;
        currentCol = 0;
        oldAction = null;
        System.out.println("calling from my agent");

    }

    private void fillArrays(){
        for (int i = 0; i<visitedCount.length; i++){
            for (int j = 0; j<visitedCount.length; j++){
                visitedCount[i][j] = 0;
                isObsticle[i][j] = false;
            }
        }
    }

    private VacuumAction prioritize(int row, int col){
        VacuumAction action = VacuumAction.FORWARD;
        int leastVisited = visitedCount[row+1][col];
        if (leastVisited > visitedCount[row][col+1]){
            leastVisited = visitedCount[row][col+1];
            action = VacuumAction.RIGHT;
        }else if (leastVisited > visitedCount[row-1][col]){
            leastVisited = visitedCount[row-1][col];
            action = VacuumAction.BACK;
        }else if (leastVisited > visitedCount[row][col-1]) {
            leastVisited = visitedCount[row][col-1];
            action = VacuumAction.LEFT;
        }


        return action;
    }

    private VacuumAction checkSurroundings(int row, int col){
        VacuumAction action = null;
        if (isObsticle[row+1][col]){
            visitedCount[row+1][col]+=1000;
            action = prioritize(row, col);
        }else if (isObsticle[row][col+1]){
            visitedCount[row][col+1]+=1000;
            action = prioritize(row, col);
        }else if (isObsticle[row-1][col]){
            visitedCount[row-1][col]+=1000;
            action = prioritize(row,col);
        }else if (isObsticle[row][col-1]){
            visitedCount[row][col-1]+=1000;
            action = prioritize(row,col);
            }

        return action;
    }
    
//    public VacuumAction getAction(VacuumLocPercept percept) {
//        VacuumAction action = null;
//        oldRow = currentRow;
//        oldCol = currentCol;
//        while (action != VacuumAction.STOP){
//            currentRow = percept.getRow();
//            currentCol = percept.getCol();
//            visitedCount[currentRow][currentCol]++;
//            if (percept.dirtSensor() == Status.DIRTY){
//                action = VacuumAction.SUCK;
//            }else{
//                action = VacuumAction.RIGHT;
//            }
//            if ((oldRow == currentRow) && (oldCol == currentCol)){
//                if(prevAction == VacuumAction.FORWARD){
//                    isObsticle[oldRow+1][oldCol] = true;
//                }else if (prevAction == VacuumAction.RIGHT){
//                    isObsticle[oldRow][oldCol+1] = true;
//                }else if (prevAction == VacuumAction.BACK){
//                    isObsticle[oldRow-1][oldCol] = true;
//                }else if (prevAction == VacuumAction.LEFT){
//                    isObsticle[oldRow][oldCol-1]=true;
//                }
//                action = checkSurroundings(currentRow,currentCol);
//            }
//
//        }
//        prevAction = action;
//        return action;
//    }

    public VacuumAction getActionLocReflex(VacuumLocPercept percept){
        int count = 0;
        VacuumAction action = VacuumAction.STOP;
        while (count < 100) {
            boolean noMove = false;
            if (oldR == percept.getRow() && oldC == percept.getCol()) {
                noMove = true;
            }

            if (!noMove) {
                if (percept.dirtSensor() == Status.DIRTY) {
                    action = VacuumAction.SUCK;
                } else {
                    action = oldAction;
                }
            } else {
                action = checkSurroundings(percept.getRow(), percept.getCol());
            }


            oldR = percept.getRow();
            oldC = percept.getCol();
            oldAction = action;
            count++;
        }
        return action;
    }

    public VacuumAction getAction(VacuumPercept percept){
        VacuumAction action = null;
        if (percept instanceof VacuumLocPercept){
            action = getActionLocReflex((VacuumLocPercept)percept);
        }else {
            System.out.println("Please provide location percept.");
            action = null;
        }
        return action;
    }


}
