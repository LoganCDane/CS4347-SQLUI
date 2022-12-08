import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class MySQLAccess {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    private String user = "";
    private String password = "";
    
    public void readDataBase() throws Exception {
        try {
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/lolbase?"
                            + "user=" + user + "&password=" + password);

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement
                    .executeQuery("select * from champion");
            //System.out.println(resultSet.toString());
            System.out.println(resultSet.getMetaData().getColumnCount());
            System.out.println(resultSet.getMetaData().getColumnLabel(1));
            while(resultSet.next()) {
                System.out.println(resultSet.getString("Champion_Name"));
                System.out.println(resultSet.getDouble("KDA_Ratio"));
                System.out.println(resultSet.getDouble("Win_Rate"));
                System.out.println(resultSet.getDouble("Ban_Rate"));
                System.out.println(resultSet.getDouble("Pick_Rate"));
            }
            /*
            writeResultSet(resultSet);


            // PreparedStatements can use variables and are more efficient
            preparedStatement = connect
                    .prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            // Parameters start with 1
            preparedStatement.setString(1, "Test");
            preparedStatement.setString(2, "TestEmail");
            preparedStatement.setString(3, "TestWebpage");
            preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
            preparedStatement.setString(5, "TestSummary");
            preparedStatement.setString(6, "TestComment");
            preparedStatement.executeUpdate();

            preparedStatement = connect
                    .prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from feedback.comments");
            resultSet = preparedStatement.executeQuery();
            writeResultSet(resultSet);

            // Remove again the insert comment
            preparedStatement = connect
                    .prepareStatement("delete from feedback.comments where myuser= ? ; ");
            preparedStatement.setString(1, "Test");
            preparedStatement.executeUpdate();

            resultSet = statement
                    .executeQuery("select * from feedback.comments");
            writeMetaData(resultSet);

             */

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    public String[][] readDatabase2() throws Exception {
        return readDatabase("select * from champion");

    }

    public String[][] readDatabase(String command) throws Exception{
        connect = DriverManager
                .getConnection("jdbc:mysql://localhost/lolbase?"
                        + "user=" + user + "&password=" + password);

        // Statements allow to issue SQL queries to the database
        statement = connect.createStatement();
        // Result set get the result of the SQL query
        resultSet = statement
                .executeQuery(command);
        //System.out.println(resultSet.toString());

        ArrayList<ArrayList<String>> values = new ArrayList<>();
        int cols = resultSet.getMetaData().getColumnCount();
        int rows = 0;
        for (int i = 0; i < cols; i++) {
            values.add(new ArrayList<>());
            values.get(i).add(resultSet.getMetaData().getColumnName(i+1));
        }
        //System.out.println(Arrays.deepToString(values.toArray()));
        while(resultSet.next()) {
            rows++;
            for (int i = 0; i < cols; i++) {
                values.get(i).add(resultSet.getString(i+1));
            }
        }
        //System.out.println(Arrays.deepToString(values.toArray()));
        String[][] arr = ListToArray(values);
        return arr;
    }

    public boolean insertDatabase(String command)throws Exception{
        connect = DriverManager
                .getConnection("jdbc:mysql://localhost/lolbase?"
                        + "user=" + user + "&password=" + password);

        statement = connect.createStatement();
        boolean happened = statement.execute(command);
        return happened;
    }
    private void writeMetaData(ResultSet resultSet) throws SQLException {
        //  Now get some metadata from the database
        // Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
            System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
        }
    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            String user = resultSet.getString("myuser");
            String website = resultSet.getString("webpage");
            String summary = resultSet.getString("summary");
            Date date = resultSet.getDate("datum");
            String comment = resultSet.getString("comments");
            System.out.println("User: " + user);
            System.out.println("Website: " + website);
            System.out.println("summary: " + summary);
            System.out.println("Date: " + date);
            System.out.println("Comment: " + comment);
        }
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

    private String[][] ListToArray(ArrayList<ArrayList<String>> list){
        String[][] valuesArr = new String[list.size()][];
        for (int i = 0; i < valuesArr.length; i++) {
            valuesArr[i] = new String[list.get(i).size()];
            for (int j = 0; j < valuesArr[i].length; j++) {
                valuesArr[i][j] = list.get(i).get(j);
            }
        }
        return valuesArr;
    }

}
