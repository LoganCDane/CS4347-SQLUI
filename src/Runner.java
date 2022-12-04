import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Runner {
    static MySQLAccess sql = new MySQLAccess();
    static int xSize = 100;
    static int ySize = 100;
    public static void main(String[] args) throws Exception {
        StdDraw.enableDoubleBuffering();
        StdDraw.setTitle("LolBase Connection");
        StdDraw.setXscale(0, xSize);
        StdDraw.setYscale(0, ySize);
        StdDraw.show();
        Drawer();
        //outputPrinter(sql.readDatabase("select * from team"));
    }

    private static void Drawer() throws Exception {

        ArrayList<Button> buttons = new ArrayList<Button>();
        int center = 50;
        int hh = 7;
        buttons.add(new Button(center, center+(hh*6), 25, hh, Color.BLUE, "SELECT"));
        buttons.add(new Button(center, center+(hh*3), 25, hh, Color.blue, "INSERT" ));
        buttons.add(new Button(center, center, 25, hh, Color.BLUE, "UPDATE"));
        buttons.add(new Button(center, center-(hh*3), 25, hh, Color.blue, "DELETE" ));
        buttons.add(new Button(center, center-(hh*6), 25, hh, Color.BLUE, "Prepared Statement"));
        boolean prevClick = false;
        while (true) {

            for (int i = 0; i < buttons.size(); i++) {
                if(!StdDraw.isMousePressed()) prevClick = false;
                Button button = buttons.get(i);
                if (!prevClick && StdDraw.isMousePressed() && button.inBounds(StdDraw.mouseX(), StdDraw.mouseY())) {
                    //System.out.println(Arrays.deepToString(sql.readDatabase2()));
                    if(i == 0){
                        CommandWindow("SELECT");
                        prevClick = true;
                    }
                    else if(i == 1){
                        CommandWindow("INSERT into");
                        prevClick = true;
                    }
                    else if (i == 2){
                        CommandWindow("UPDATE");
                        prevClick = true;
                    }
                    else if (i == 3){
                        CommandWindow("DELETE from");
                        prevClick = true;
                    }
                    else if(i == 4){
                        //TODO: Add Prepared statements
                        prevClick = true;
                    }
                }
                button.draw();
            }


            StdDraw.show();
            StdDraw.clear();
        }
    }
    
    private static void CommandWindow(String command){
        Boolean prevClick = true;
        StringBuilder input = new StringBuilder();
        Button confirm = new Button(50, 30, 10, 10, Color.BLUE, "CONFIRM");
        Button back = new Button(30, 30, 5, 5, Color.red, "<-");
        while (true) {
            if(!StdDraw.isMousePressed()) prevClick = false;
            while (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                System.out.print(key);
                if(key == 8){
                    if(input.length() != 0) input.deleteCharAt(input.length()-1);
                }
                else {
                    input.append(key);
                }
            }
            if(!prevClick && StdDraw.isMousePressed() && confirm.inBounds()){
                try {
                    String[][] output = sql.readDatabase(command + " " + input.toString());
                    System.out.println(Arrays.deepToString(output));
                    outputPrinter(output);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if(!prevClick && StdDraw.isMousePressed() && back.inBounds()){
                return;
            }

            StdDraw.setPenColor(Color.black);
            int textX = 7;
            int textY = 50;
            StdDraw.rectangle(textX,textY,7, 2.5);
            StdDraw.text(textX, textY, command);
            StdDraw.textLeft(16, textY, input.toString());

            confirm.draw();
            back.draw();

            StdDraw.show();
            StdDraw.clear();
        }
    }


    private static void outputPrinter(String[][] output){
        Button button = new Button(5, 5, 2.5, 2.5, Color.red, "<-");

        boolean prevClick = true;
        double boxWidth, boxHeight;
        boxWidth = ((xSize*.9)/output.length);
        boxHeight = ((ySize*.9)/output[0].length);
        System.out.println(output.length);
        System.out.println(output[0].length);
        System.out.println("BoxWidth: " + boxWidth + ", BoxHeight: " + boxHeight);
        while (true) {
            StdDraw.setPenColor(Color.black);
            if(!StdDraw.isMousePressed()) prevClick = false;
            for (int i = 0; i < output.length; i++) {
                for (int j = 0; j < output[i].length; j++) {
                    double x = ((boxWidth * i))+(boxWidth/2)+5;
                    double y = ySize - (((boxHeight * j)+boxHeight/2)+5);
                    //System.out.println(x + " " + y);
                    StdDraw.rectangle(x, y, boxWidth/2, boxHeight/2);
                    //StdDraw.text(x, y, ((int)x) + ", " + ((int)y));
                    StdDraw.text(x,y,output[i][j]);
                }
            }
            if(StdDraw.isMousePressed() && button.inBounds()){
                return;
            }
            button.draw();
            StdDraw.show();
            StdDraw.clear();
        }
    }

}
