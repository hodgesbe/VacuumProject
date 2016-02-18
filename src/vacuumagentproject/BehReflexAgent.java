package vacuumagentproject;


import java.util.ArrayList;

/**
 * Created by bHodges on 2/3/16.
 */
public class BehReflexAgent extends VacuumAgent{

    final int MAXDIM = 501;
    int[][] visitCount = new int[MAXDIM][MAXDIM];
    boolean[][] isObstacle = new boolean[MAXDIM][MAXDIM];
    ArrayList<VacuumAction> actionList = new ArrayList();
    //final int OFFSET = 1;
    int oldRow, oldCol, count;

    BehReflexAgent(){
        oldRow = 0;
        oldCol = 0;
        count = 0;
        initArrs();
    }//constructor

    private void initArrs(){
        for (int i = 0; i<MAXDIM; i++){
            for (int j = 0; j< MAXDIM; j++){
                visitCount[i][j] = 0;
                isObstacle[i][j] = false;
            }
        }
    }

    public VacuumAction getAction(VacuumPercept percept){
        if (percept instanceof VacuumLocPercept)
            return getActionLocReflex((VacuumLocPercept)percept);
        else
            return VacuumAction.STOP;
    }
    /**
     *
     * @param percept -- a location percept
     * @return VacuumAction.SUCK if current location is dirty else returns a random move
     */
    private VacuumAction getActionLocReflex(VacuumLocPercept percept) {
        VacuumAction action = null;
        //VacuumAction lastAction = null;
        int row = percept.getRow();
        int col = percept.getCol();
        if (percept.dirtSensor() == Status.DIRTY){
            System.out.println(count+"we found dirt");
            action = VacuumAction.SUCK;
        }else{
            if (row == oldRow && col == oldCol) {
                isObstacle[row][col] = true;
                action = actionPicker(row, col);
                System.out.println((count+"Obsticle at: " + action.getRowMove() + row) + "," + (action.getColMove() + col));
                visitCount[action.getRowMove() + row][action.getColMove() + col] += 500;
            }else
                action = actionPicker(row, col);
        }



        if (action != VacuumAction.SUCK){
            //actionList.add(action);
            oldRow = row;
            oldCol = col;
        }
        count++;
        visitCount[row][col]++;
        System.out.println(count+" success, going: "+action);

        if (actionTester(action, row, col)){
            System.out.println(count+"attempted to go to a blocked space, activating action switcher");
            actionSwitcher(action);
        }

        return action;
    }

    private VacuumAction actionPicker(int row, int col){
        //set default action
        VacuumAction action = VacuumAction.RIGHT;
        //set default lowCount
        //check to see if I am on a "0" boarder
        int lowCount = visitCount[row][col+1];

        //check to see if I am on a "0" boarder
        if (row == 0){
            action = VacuumAction.RIGHT;
        }else if (col == 0){
            action = VacuumAction.BACK;
        }else{
            //check surrounding squares for lowest
            if (visitCount[row+1][col]<lowCount){
                lowCount = visitCount[row+1][col];
                action = VacuumAction.BACK;
            }
            if (visitCount[row][col-1] < lowCount){
                lowCount = visitCount[row][col-1];
                action = VacuumAction.LEFT;
            }
            if (visitCount[row-1][col] < lowCount){
                lowCount = visitCount[row-1][col];
                action = VacuumAction.FORWARD;
            }
        }
//        if (actionTester(action, row, col)){
//            System.out.println(count+"attempted to go to a blocked space, activating action switcher");
//            actionSwitcher(action);
//        }

        return action;
    }

    private boolean actionTester(VacuumAction vAction, int cRow, int cCol){
        boolean check = false;
        if (cRow == 0 && vAction == VacuumAction.LEFT){
            check = true;
        }else if(cCol == 0 && vAction == VacuumAction.FORWARD){
            check = true;
        }else if(isObstacle[vAction.getRowMove()+cRow][vAction.getColMove()+cCol]==true){
            check = true;
        }
        return check;
    }

    private VacuumAction actionSwitcher(VacuumAction vAction){
        VacuumAction action = null;
        if (vAction == VacuumAction.FORWARD){
            action = VacuumAction.RIGHT;
        }else if (vAction == VacuumAction.RIGHT){
            action = VacuumAction.BACK;
        }else if (vAction == VacuumAction.BACK){
            action = VacuumAction.LEFT;
        }else if (vAction == VacuumAction.LEFT){
            action = VacuumAction.FORWARD;
        }
        System.out.println(count+"switching action from: "+vAction+"to: "+action);
        return action;
    }



}
