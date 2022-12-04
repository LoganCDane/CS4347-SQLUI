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
        //TourneySelector();
        StdDraw.clear();
        StdDraw.show();
        System.exit(0);
        //outputPrinter(sql.readDatabase("select * from team"));
    }

    private static void Drawer() throws Exception {

        ArrayList<Button> buttons = new ArrayList<>();
        int center = 50;
        int hh = 7;
        buttons.add(new Button(center, center+(hh*6), 25, hh, Color.BLUE, "SELECT"));
        buttons.add(new Button(center, center+(hh*3), 25, hh, Color.blue, "INSERT" ));
        buttons.add(new Button(center, center, 25, hh, Color.BLUE, "UPDATE"));
        buttons.add(new Button(center, center-(hh*3), 25, hh, Color.blue, "DELETE" ));
        buttons.add(new Button(center, center-(hh*6), 25, hh, Color.BLUE, "Prepared Statement"));
        buttons.add(new Button(5, 5, 5, 5, Color.red, "<-"));
        boolean prevClick = false;
        while (true) {

            for (int i = 0; i < buttons.size(); i++) {
                if(!StdDraw.isMousePressed()) prevClick = false;
                Button button = buttons.get(i);
                if (!prevClick && StdDraw.isMousePressed() && button.inBounds(StdDraw.mouseX(), StdDraw.mouseY())) {
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
                        preparedStatements();
                        prevClick = true;
                    }
                    else if(i ==5){
                        return;
                    }
                }
                button.draw();
            }


            StdDraw.show();
            StdDraw.clear();
        }
    }
    
    private static void CommandWindow(String command){
        boolean prevClick = true;
        StringBuilder input = new StringBuilder();
        Button confirm = new Button(50, 30, 10, 10, Color.BLUE, "CONFIRM");
        Button back = new Button(5, 5, 5, 5, Color.red, "<-");
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
            if(!input.isEmpty() && !prevClick && StdDraw.isMousePressed() && confirm.inBounds()){
                try {
                    if(command.equals("SELECT")) {
                        String[][] output = sql.readDatabase(command + " " + input);
                        System.out.println(Arrays.deepToString(output));
                        outputPrinter(output);
                    }
                    else {
                        System.out.println(sql.insertDatabase(command + " " + input));
                    }
                    prevClick = true;

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

    private static void preparedStatements() throws Exception {
        boolean prevClick = true;
        ArrayList<Button> statements = new ArrayList<>();
        statements.add(new Button(50, 85, 40, 5, Color.blue, "Show all Players Sorted By Win Rate"));
        statements.add(new Button(50, 70, 40, 5, Color.blue, "Show all Players Sorted by KDA Ratio"));
        statements.add(new Button(50, 55, 40, 5, Color.blue, "Show Stats of All Teams in Each Tournament"));
        statements.add(new Button(50, 40, 40, 5, Color.blue, "Show all Champions ordered by their pick rate"));
        statements.add(new Button(50, 25, 40, 5, Color.blue, "Show each team and the number of players on that team"));
        statements.add(new Button(50, 10, 40, 5, Color.blue, "Current Standing of a given tournament"));

        statements.add(new Button( 5, 5, 5 ,5, Color.red, "<-"));

        while(true){
            if(!StdDraw.isMousePressed()) prevClick = false;

            for (int i = 0; i < statements.size(); i++) {
                statements.get(i).draw();
                if(!prevClick && StdDraw.isMousePressed() && statements.get(i).inBounds()){
                    if( i == 0){
                        outputPrinter(sql.readDatabase("select * from player order by Win_Rate desc"));
                        prevClick = true;
                    }
                    else if( i == 1){
                        outputPrinter(sql.readDatabase("select Player_Name, KDA_Ratio FROM Player ORDER BY KDA_Ratio desc"));
                        prevClick = true;
                    }
                    else if(i == 2){
                        outputPrinter(sql.readDatabase("select * from (Team natural join ParticipatedInTourney) natural join Tournament"));
                        prevClick = true;
                    }
                    else if(i == 3){
                        outputPrinter(sql.readDatabase("select * from Champion order by Pick_Rate DESC"));
                        prevClick = true;
                    }
                    else if(i == 4){
                        outputPrinter(sql.readDatabase("select * from Teammates"));
                        prevClick = true;
                    }
                    else if(i == 5){
                        //TODO: make a new menu
                        TourneySelector();
                        prevClick = true;
                    }
                    else if(i == 6){
                        return;
                    }
                }
            }

            StdDraw.show();
            StdDraw.clear();
        }

    }

    private static void TourneySelector() throws Exception {
        boolean prevClick = true;
        Button back = new Button(5, 5, 5, 5, Color.red, "<-");
        String[][] output = sql.readDatabase("select Tournament_Name from Tournament");
        ArrayList<Button> buttons = new ArrayList<>();
        System.out.println(Arrays.deepToString(output));

        double boxWidth, boxHeight;
        boxWidth = ((xSize*.9)/output.length);
        boxHeight = ((ySize*.9)/(output[0].length-1));

        for (int i = 1; i < output[0].length; i++) {
            System.out.println(i);
            double x = xSize/2.0;
            double y = ySize - (((boxHeight * (i-1))+boxHeight/2)+5);
            buttons.add(new Button(x, y, boxWidth/2, boxHeight/2, Color.blue, output[0][i]));
        }
        System.out.println(buttons.size());
        while (true){
            for (int i = 0; i < buttons.size(); i++) {
                if(!StdDraw.isMousePressed()) prevClick = false;
                Button button = buttons.get(i);
                button.draw();
                if(!prevClick && StdDraw.isMousePressed() && button.inBounds()){
                    outputPrinter(sql.readDatabase("Select * from(" +
                            "(Select Winner as Team, Count(Winner) as Games_Won " +
                            "From Series " +
                            "Where Tournament_Name = '" + button.getText() +
                            "' Group by Team) " +
                            "Union " +
                            "(Select Team_Name, 0 " +
                            "From ParticipatedInTourney " +
                            "Where Tournament_Name = '" + button.getText() + "' AND Team_Name NOT IN " +
                            "(Select Winner FROM Series Where Tournament_Name = '" + button.getText() + "' ))) AS temp " +
                            "Order By Games_Won Desc;"));
                    prevClick = true;
                }

            }

            if(!prevClick && StdDraw.isMousePressed() && back.inBounds()){
                return;
            }

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
        System.out.println(Arrays.deepToString(output));
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
            if(!prevClick && StdDraw.isMousePressed() && button.inBounds()){
                return;
            }
            button.draw();
            StdDraw.show();
            StdDraw.clear();
        }
    }

}
