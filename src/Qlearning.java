import java.util.Random;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Joe on 11/14/15.
 */
public class Qlearning {
    private class ReturnPair{
        private String direction;
        private State state;
        public ReturnPair(String direction, State state){
            this.direction = direction;
            this.state = state;
        }
        public String getDirection(){
            return direction;
        }
        public State getState(){
            return state;
        }
    }

    Vector<Vector<State>> q_table;
    private double alpha;
    private double gamma;

    public Qlearning(int[] start, int[] goal, char[][] world){
        q_table = new Vector<>();
        qInit(q_table, world, goal);
        printQTable(q_table);
        printRewards(q_table);
        alpha = 1;
        gamma = .8;
        Random rand = new Random();
        for(int i =0; i < 1000; i++){
            int y = rand.nextInt(q_table.size());
            int x = rand.nextInt(q_table.elementAt(y).size());
            while(world[y][x] == 'x' || q_table.elementAt(y).elementAt(x).getReward() > 99.999){
                y = rand.nextInt(q_table.size());
                x = rand.nextInt(q_table.elementAt(y).size());
            }
            episode(world, q_table, q_table.elementAt(y).elementAt(x), 0, gamma, alpha);
        }
        printQTable(q_table);
        traverseGrid(start, goal, q_table, world);
    }

    private void episode(char[][] world, Vector<Vector<State>> qt, State state, int depth, double gamma, double alpha){
        if(depth>150){
            return;
        }
        int[] pos = state.getPosition();
        int x = pos[0];
        int y = pos[1];
        if(world[y][x] == 'x'){
            System.out.println("hit an obstacle");
            return;
        }
        if(qt.elementAt(y).elementAt(x).getReward() > 99.999){
            return;
        }
        Set<String> neighbors = state.getActions();
        ReturnPair pair = nextState(qt, neighbors, state);
        String direction = pair.getDirection();
        State next_state = pair.getState();
        state.takeAction(direction);
        double reward = state.getActionReward(direction) + alpha*(next_state.getReward() + gamma*maxActionReward(next_state) - state.getActionReward(direction));
        state.setActionReward(direction, Math.max(reward, state.getActionReward(direction)));
        episode(world, qt, next_state, depth+1, gamma, alpha);
    }

    public double maxActionReward(State state){
        double max = 0.0;
        Set<String> actions = state.getActions();
        for(String s : actions){
            if(state.getActionReward(s) > max){
                max = state.getActionReward(s);
            }
        }
        return max;
    }

    //this might have an issue
    public ReturnPair nextState(Vector<Vector<State>> qt, Set<String> neighbors, State current){
        String direction = null;
        Random rand = new Random();
        int index = rand.nextInt(neighbors.size());
        direction = (String)neighbors.toArray()[index];
        int[] pos = current.getPosition();
        int x = pos[0];
        int y = pos[1];
        State next;
        if(direction.equals("left")){
            next = qt.elementAt(y).elementAt(x-1);
        }
        else if(direction.equals("right")){
            next = qt.elementAt(y).elementAt(x+1);
        }
        else if(direction.equals("up")){
            next = qt.elementAt(y-1).elementAt(x);
        }
        else if(direction.equals("down")){
            next = qt.elementAt(y+1).elementAt(x);
        }
        else {
            next = null;
            System.out.println(direction);
        }

        ReturnPair rp = new ReturnPair(direction, next);
        return rp;
    }

    private void qInit(Vector<Vector<State>> qt, char[][] world, int[] goal){
        int n = world.length;
        int m = world[0].length;
        for(int y = 0; y < n; y++){
            Vector<State> temp = new Vector<>();
            for(int x = 0; x < m; x++){
                State ts = new State(x, y);
                if(x+1 < m && world[y][x+1] != 'x'){
                    ts.addAction("right", 0);
                }
                if(x-1 >= 0 && world[y][x-1] != 'x'){
                    ts.addAction("left", 0);
                }
                if(y+1 < n && world[y+1][x] != 'x'){
                    ts.addAction("down", 0);
                }
                if(y-1 >= 0 && world[y-1][x] != 'x'){
                    ts.addAction("up", 0);
                }
                if(y == goal[1] && x == goal[0]){
                    ts.setReward(100);
                }
                temp.add(ts);
            }
            qt.add(temp);
        }
    }

    public void printQTable(Vector<Vector<State>> qt){
        for(int y = 0; y < qt.size(); y++){
            for(int x = 0; x < qt.elementAt(y).size(); x++){
                int[] pos = qt.elementAt(y).elementAt(x).getPosition();
                int i = pos[0];
                int j = pos[1];
                Set<String> actions = qt.elementAt(y).elementAt(x).getActions();
                String list = "";
                for(String s : actions){
                    list += " " + s + "=" + qt.elementAt(y).elementAt(x).getActionReward(s);
                }
                System.out.println("State (" + i + "," + j + ")" + "'s Actions " + list);
            }
        }
    }

    public void printRewards(Vector<Vector<State>> qt){
        for(int y = 0; y < qt.size(); y++){
            String s = "";
            for(int x = 0; x < qt.elementAt(y).size(); x++){
                s += qt.elementAt(y).elementAt(x).getReward() + " ";
            }
            System.out.println(s);
        }
    }

    public void traverseGrid(int[] start, int[] goal, Vector<Vector<State>> qt, char[][] world){
        int xs = start[0];
        int ys = start[1];
        int xg = goal[0];
        int yg = goal[1];
        boolean reachedEnd = false;
        double maxReward = 0.0;
        int index=0;
        int i =0;
        while(!reachedEnd){
            if(xs == xg && ys == yg){
                reachedEnd = true;
                break;
            }
            else {
                Set<String> neighbors = qt.elementAt(ys).elementAt(xs).getActions();
                String direction = (String)neighbors.toArray()[0];
                for(String x : neighbors){
                    double reward = qt.elementAt(ys).elementAt(xs).getActionReward(x);
                    if(reward > maxReward){
                        maxReward = reward;
                        index = i;
                        direction = x;
                    }
                    i++;
                }
                System.out.println("Agent selects action: " + direction);
                State next = null;
                if(direction == "left")
                    next = qt.elementAt(ys).elementAt(xs-1);
                else if(direction == "right")
                    next = qt.elementAt(ys).elementAt(xs+1);
                else if(direction == "up")
                    next = qt.elementAt(ys-1).elementAt(xs);
                else if(direction == "down")
                    next = qt.elementAt(ys+1).elementAt(xs);
                int[] nextPos = next.getPosition();
                xs = nextPos[0];
                ys = nextPos[1];
                System.out.println("(" + xs + "," + ys + ")");
            }
        }


    }
}
