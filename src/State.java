import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Joe on 11/14/15.
 */
public class State {

    private int[] position;
    private double reward = 0.0;
    private int visited = 0;
    private HashMap<String,double[]> actions;
    public AtomicBoolean lock;


    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public State(int x, int y){
        position = new int[2];
        position[0] = x;
        position[1] = y;
        actions = new HashMap<>();
        lock = new AtomicBoolean(false);
    }

    public void addAction(String direction, double value){
        double[] valuePlusTaken = new double[2];
        valuePlusTaken[0] = value;
        valuePlusTaken[1] = 0;
        actions.put(direction,valuePlusTaken);
    }
    public void setActionReward(String direction, double value){
        double[] old = actions.get(direction);
        double[] temp = new double[2];
        temp[0] = value;
        temp[1] = old[1];
        actions.put(direction, temp);
    }

    public double getActionReward(String direction){
        return actions.get(direction)[0];
    }

    public void takeAction(String direction){
        if(actions.containsKey(direction)){
            double[] old = actions.get(direction);
            double[] temp = new double[2];
            temp[0] = old[0];
            temp[1] = old[1]+1;
            actions.put(direction, temp);
        }
        else {
            System.out.println("error, could not take action");
        }
    }

    public int[] getPosition(){
        return position;
    }

    public Set<String> getActions(){
        return actions.keySet();
    }

    public HashMap<String,double[]> getActionDict(){
        return actions;
    }

    public int numTaken(String direction){
        if(actions.containsKey(direction)){
            return (int)actions.get(direction)[1];
        }
        else {
            System.out.println("Invalid direction " + direction);
            return -1;
        }
    }
}
