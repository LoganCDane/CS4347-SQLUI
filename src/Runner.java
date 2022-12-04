import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Runner {
    static MySQLAccess sql = new MySQLAccess();
    static int xSize = 200;
    static int ySize = 100;

    static double backX = xSize*.05;
    static double backY = ySize*.05;

    public static void main(String[] args) throws Exception {
        StdDraw.enableDoubleBuffering();
        StdDraw.setTitle("LolBase Connection");
        StdDraw.setCanvasSize(1000,500);
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
        int xCenter = xSize/2;
        int yCenter = ySize/2;
        double hh = (ySize*0.07);
        double hw = xSize/4.0;
        buttons.add(new Button(xCenter, yCenter+(hh*6), hw, hh, Color.BLUE, "SELECT"));
        buttons.add(new Button(xCenter, yCenter+(hh*3), hw, hh, Color.blue, "INSERT" ));
        buttons.add(new Button(xCenter, yCenter, hw, hh, Color.BLUE, "UPDATE"));
        buttons.add(new Button(xCenter, yCenter-(hh*3), hw, hh, Color.blue, "DELETE" ));
        buttons.add(new Button(xCenter, yCenter-(hh*6), hw, hh, Color.BLUE, "Prepared Statement"));
        buttons.add(new Button(backX, backY, backX, backY, Color.red, "<-"));
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
        Button confirm = new Button(xSize*.5, ySize*.3, xSize*.1, ySize*.1, Color.BLUE, "CONFIRM");
        Button back = new Button(backX, backY, backX, backY, Color.red, "<-");
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
                    if(!input.toString().contains(";")) {
                        if (command.equals("SELECT")) {
                            String[][] output = sql.readDatabase(command + " " + input);
                            System.out.println(Arrays.deepToString(output));
                            outputPrinter(output);
                        } else {
                            System.out.println(sql.insertDatabase(command + " " + input));
                        }
                    }
                    else{
                        String[] commands = input.toString().split(";");
                        commands[0] = command + " " + commands[0];
                        for (int i = 0; i < commands.length; i++) {
                            System.out.println(commands[i].toLowerCase());
                            if(commands[i].toLowerCase().contains("select")){
                                String[][] output = sql.readDatabase(commands[i]);
                                System.out.println(Arrays.deepToString(output));
                                outputPrinter(output);
                            }
                            else{
                                System.out.println(sql.insertDatabase(commands[i]));
                            }
                        }
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
            double textX = xSize*.1;
            double textY = ySize*.50;
            StdDraw.rectangle(textX,textY,xSize*.1, ySize*0.025);
            StdDraw.text(textX, textY, command);
            StdDraw.textLeft(xSize*.2, textY, input.toString());

            confirm.draw();
            back.draw();

            StdDraw.show();
            StdDraw.clear();
        }
    }

    private static void preparedStatements() throws Exception {
        boolean prevClick = true;
        ArrayList<Button> statements = new ArrayList<>();
        double xCenter = xSize*.5;
        double xWidth = xSize*.40;
        double yHeight = ySize*.05;
        statements.add(new Button(xCenter, ySize*.85, xWidth, yHeight, Color.blue, "Show all Players Sorted By Win Rate"));
        statements.add(new Button(xCenter, ySize*.70, xWidth, yHeight, Color.blue, "Show all Players Sorted by KDA Ratio"));
        statements.add(new Button(xCenter, ySize*.55, xWidth, yHeight, Color.blue, "Show Stats of All Teams in Each Tournament"));
        statements.add(new Button(xCenter, ySize*.40, xWidth, yHeight, Color.blue, "Show all Champions ordered by their pick rate"));
        statements.add(new Button(xCenter, ySize*.25, xWidth, yHeight, Color.blue, "Show each team and the number of players on that team"));
        statements.add(new Button(xCenter, ySize*.10, xWidth, yHeight, Color.blue, "Current Standing of a given tournament"));

        statements.add(new Button( backX, backY, backX ,backY, Color.red, "<-"));

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
        Button back = new Button(backX, backY, backX, backY, Color.red, "<-");
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
            for (Button value : buttons) {
                if (!StdDraw.isMousePressed()) prevClick = false;
                value.draw();
                if (!prevClick && StdDraw.isMousePressed() && value.inBounds()) {
                    outputPrinter(sql.readDatabase("Select * from(" +
                            "(Select Winner as Team, Count(Winner) as Games_Won " +
                            "From Series " +
                            "Where Tournament_Name = '" + value.getText() +
                            "' Group by Team) " +
                            "Union " +
                            "(Select Team_Name, 0 " +
                            "From ParticipatedInTourney " +
                            "Where Tournament_Name = '" + value.getText() + "' AND Team_Name NOT IN " +
                            "(Select Winner FROM Series Where Tournament_Name = '" + value.getText() + "' ))) AS temp " +
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
        Button button = new Button(backX, backY, backX, backY, Color.red, "<-");

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
                    double x = ((boxWidth * i))+(boxWidth/2)+(xSize*.05);
                    double y = ySize - (((boxHeight * j)+boxHeight/2)+(ySize*.05));
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
