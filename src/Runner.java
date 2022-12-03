import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Runner {
    static MySQLAccess sql = new MySQLAccess();

    public static void main(String[] args) throws Exception {
        StdDraw.enableDoubleBuffering();
        StdDraw.setTitle("LolBase Connection");
        StdDraw.setScale(0, 100);
        StdDraw.show();
        Drawer();

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
                    System.out.println(Arrays.deepToString(sql.readDatabase(command + " " + input.toString())));
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


}
