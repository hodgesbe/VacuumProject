package vacuumagentproject;

import java.util.Random;

/**
 * Created by bHodges on 1/25/16.
 */
public class BehRandomAgent extends VacuumAgent {
    static final int ACTION_COUNT = VacuumAction.numOfMoves();
    int[] actionCounts = new int[VacuumAction.numOfMoves()];
    static VacuumAction[] actionList;

    public BehRandomAgent() {
        for (int i=0; i<ACTION_COUNT; i++)
            actionCounts[i] = 0;
        actionList = VacuumAction.values();
    }

    public VacuumAction getAction(VacuumLocPercept percept) {
        return getActionRandomReflex(percept);
    }

    private VacuumAction getActionRandomReflex(VacuumLocPercept percept) {
        if (percept.currentStatus == Status.DIRTY)
            return VacuumAction.SUCK;
        else {//choose a move randomly
            Random gen = new Random();

            int index = gen.nextInt(actionList.length); //randomly select an action
            while (!actionList[index].isAMove())//sequentially look for a move
                index = (index + 1) % actionList.length;
            return actionList[index];
        }//if current status is clean
    }
}
