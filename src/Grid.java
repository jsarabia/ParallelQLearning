import java.io.*;
import java.nio.charset.Charset;
import java.util.Vector;

/**
 * Created by Joe on 11/14/15.
 */
public class Grid {

    private Vector<Vector<Character>> tempWorld;
    private char[][] world;
    private int height;
    private int width;

    public Grid(){}

    public void readFile(String path){
        tempWorld = new Vector<>();
        String line;
        try {
            InputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                Vector<Character> temp = new Vector<>();
                char[] arrayOfChars = line.toCharArray();
                for(int i = 0; i < line.length(); i++){
                    temp.add(arrayOfChars[i]);
                }
                tempWorld.add(temp);
            }

            width = tempWorld.elementAt(0).size();
            height = tempWorld.size();
            world = new char[tempWorld.size()][tempWorld.elementAt(0).size()];
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    world[y][x] = tempWorld.elementAt(y).elementAt(x);
                }
            }
            System.out.println("size is " + height + "x" + width);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void printWorld(){
        for(char[] d : world){
            for(char i : d){
                System.out.print(i);
            }
            System.out.println();
        }
    }

    public char[][] getWorld(){
        return world;
    }
}
