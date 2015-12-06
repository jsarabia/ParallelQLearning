import java.util.Scanner;
import java.util.Vector;

/**
 * Created by Joe on 11/14/15.
 */
public class Main {

    public static void main(String[] args){
        Vector<Integer> ts = new Vector<>();
        ts.add(1);
        ts.add(2);
        ts.add(4);
        ts.add(10);
        int numToAverage = 100;
        int episodeCount = 1000000;
        System.out.println("Backoff Algorithm");
        for(Integer t : ts) {
            int sum = 0;
            for (int i = 0; i < numToAverage; i++) {
                Grid grid = new Grid();
                grid.readFile("data/complexWorld.txt");
                //grid.printWorld();
                char[][] world = grid.getWorld();
                //System.out.println("Enter the X coordinate of the Start State:");
                //Scanner sc = new Scanner(System.in);
                int xs = 0;//sc.nextInt();
                //System.out.println("Enter the Y coordinate of the Start State");
                int ys = 0;//sc.nextInt();
                //System.out.println("Enter the X coordinate of the Goal State");
                int gx = 8;//sc.nextInt();
                //System.out.println("Enter the Y cooridnate of the Goal State");
                int gy = 8;//sc.nextInt();

                int[] start = new int[2];
                start[0] = xs;
                start[1] = ys;
                int[] goal = new int[2];
                goal[0] = gx;
                goal[1] = gy;

                Qlearning ql = new Qlearning(start, goal, world, t, episodeCount);
                sum += ql.execTime();
            }
            sum /= numToAverage;
            System.out.print(sum + ",");
        }
    }
}
