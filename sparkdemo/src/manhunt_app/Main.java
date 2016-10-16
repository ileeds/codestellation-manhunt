package manhunt_app;
import static spark.Spark.*;
import java.sql.*;
import java.util.Properties;

// https://sparktutorials.github.io/2015/08/24/spark-heroku.html
// https://sparktutorials.github.io/2015/06/01/spark-freemarker.html
// https://commons.apache.org/proper/commons-csv/

public class Main {

  public static void main(String[] args) {
    port(getHerokuAssignedPort());
    /*StudentDirectory sd = new StudentDirectory();
    sd.readStudentInfoFile();
    StudentChooserServer sc = new StudentChooserServer(sd);*/
    VerticaConnect a = new VerticaConnect();
    Connection main = a.start();
    Statement mySelect;
	try {
		main.createStatement().executeUpdate("set search_path to " + "team3_schema");
		mySelect = main.createStatement();
		mySelect.executeUpdate("CREATE TABLE IF NOT EXISTS manhuntUsers(username varchar primary key, password varchar, game varchar, status int, wins int, losses int);");
		mySelect.executeUpdate("CREATE TABLE IF NOT EXISTS manhuntGames(name varchar primary key, number int);");
		mySelect.executeUpdate("CREATE TABLE IF NOT EXISTS manhuntSeekers(game_id varchar REFERENCES manhuntGames (name), user_id varchar REFERENCES manhuntUsers (username));");
		mySelect.executeUpdate("CREATE TABLE IF NOT EXISTS manhuntHiders(game_id varchar REFERENCES manhuntGames (name), user_id varchar REFERENCES manhuntUsers (username));");
		Home h = new Home(main);
		h.launch();
	} catch (SQLException e) {
		e.printStackTrace();
	}
    //sc.launch();
    //a.close();
  }

  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567; // return default port if heroku-port isn't set (i.e. on
                 // localhost)
  }

}
