package manhunt_app;

import static j2html.TagCreator.*;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Random;
import java.util.stream.Stream;

import j2html.TagCreator;
import j2html.tags.Tag;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Home {
	
	private static Connection main;
	private static List <String> log = new ArrayList ();

	public Home(Connection main) {
	    this.main = main;
	}

	  public void launch() {
	    get("/", (res, req) -> welcomePage());
	    post("/", (res, req) -> signedInPage(res, req));
	    post("/logged", (res, req) -> loggedInPage(res, req));
	    post("/loggedAfter", (res, req) -> loggedInPage2(res, req));
	    post("/loggedExit", (res, req) -> loggedInPage3(res, req));
	    post("/game/join", (res, req) -> joinGamePage(res, req));
	    post("/game/in", (res, req) -> inGamePage(res, req));
	    post("/game/create", (res, req) -> createGamePage(res, req));
	    post("/logOut", (res, req) -> logOutPage(res, req));
	  }

	  private String logOutPage(Request res, Response req) {
		  String s = res.body();
		  String t = res.body();
		  s = s.substring(s.indexOf("=") + 1);
		  s = s.substring(0, s.indexOf("&"));
		  t = t.substring(t.lastIndexOf("=") + 1);
		 log.remove("s");
		 log.remove("t");
		String a = body().with(
		        h1("Manhunt"),
		        form().withAction("/").withMethod("post").with(
		        	    chooseUserInput("Choose Username"),
		        		choosePasswordInput("Choose Password"),
		        	    submitButton("Sign up"))).render();
		    a += body().with(
		    	form().withAction("/logged").withMethod("post").with(
	        	    chooseUserInput("Your Username"),
	        		choosePasswordInput("Your Password"),
	        	    submitButton("Sign in"))).render();
		    return a;
	}

	private Object loggedInPage3(Request res, Response req) {
		  String a = body().with(
			        h1("Choose"),
			        form().withAction("/game/join").withMethod("post").with(
			        	    submitButton("Join game"))).render();
			    a += body().with(
			    	form().withAction("/game/create").withMethod("post").with(
		        	    submitButton("Create game"))).render();
			    a += body().with(
				    	form().withAction("/logOut").withMethod("post").with(
			        	    submitButton("Log Out"))).render();
			    return a;
	}

	private Object loggedInPage2(Request res, Response req) {
		  String s = res.body();
		  String t = res.body();
		  s = s.substring(s.indexOf("=") + 1);
		  s = s.substring(0, s.indexOf("&"));
		  t = t.substring(t.lastIndexOf("=") + 1);
		  try {
				Statement mySelect = main.createStatement();
				mySelect.executeUpdate("INSERT INTO  manhuntGames (name,number) VALUES ('"+s+"','"+t+"');");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		  String a = body().with(
			        h1("Choose"),
			        form().withAction("/game/join").withMethod("post").with(
			        	    submitButton("Join game"))).render();
			    a += body().with(
			    	form().withAction("/game/create").withMethod("post").with(
		        	    submitButton("Create game"))).render();
			    a += body().with(
				    	form().withAction("/logOut").withMethod("post").with(
			        	    submitButton("Log Out"))).render();
			    return a;
	}

	private Object createGamePage(Request res, Response req) {
		  String a = body().with(
			        h1("Manhunt"),
			        form().withAction("/loggedAfter").withMethod("post").with(
			        	    chooseUserInput("Choose Game Name"),
			        		chooseUserInput("Choose Number of Players"),
			        	    submitButton("Create"))).render();
		  a += body().with(
			    	form().withAction("/loggedExit").withMethod("post").with(
		        	    submitButton("Exit"))).render();
		  a += body().with(
			    	form().withAction("/logOut").withMethod("post").with(
		        	    submitButton("Log Out"))).render();
			    return a;
	}

	private Object inGamePage(Request res, Response req) {
		String file = "geolocation.html";
		StringBuilder toReturn=new StringBuilder();
		BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                toReturn.append(line+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
			for (String print:log){
				System.out.println(print);
			}
		  return toReturn;
	}

	private Object joinGamePage(Request res, Response req) {
		String a="";
		try {
			Statement mySelect = main.createStatement();
			mySelect.setMaxRows(10);
			ResultSet rs = mySelect.executeQuery("SELECT * FROM manhuntGames;");
			while (rs.next()){
				String b = rs.getString("name");
				a+=body().with(
						form().withAction("/game/in").withMethod("post").with(
								submitButton(b))).render();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		a += body().with(
		    	form().withAction("/loggedExit").withMethod("post").with(
	        	    submitButton("Exit"))).render();
		a += body().with(
		    	form().withAction("/logOut").withMethod("post").with(
	        	    submitButton("Log Out"))).render();
		return a;
	}

	private Object loggedInPage(Request res, Response req) {
		String s = res.body();
		  String t = res.body();
		  s = s.substring(s.indexOf("=") + 1);
		  s = s.substring(0, s.indexOf("&"));
		  t = t.substring(t.lastIndexOf("=") + 1);
		  Statement mySelect;
		try {
			mySelect = main.createStatement();
			ResultSet rs = mySelect.executeQuery("SELECT * FROM manhuntUsers WHERE (username, password) = ('"+s+"','"+t+"');");
			if (!rs.next()){
				return form().withAction("/").withMethod("get").with(
		        	    submitButton("Nope")).render();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		  log.add(s);
		  log.add(t);
		String a = body().with(
			        h1("Choose"),
			        form().withAction("/game/join").withMethod("post").with(
			        	    submitButton("Join game"))).render();
			    a += body().with(
			    	form().withAction("/game/create").withMethod("post").with(
		        	    submitButton("Create game"))).render();
			    a += body().with(
				    	form().withAction("/logOut").withMethod("post").with(
			        	    submitButton("Log Out"))).render();
			    return a;
	}

	private String signedInPage(Request res, Response req) {
			String s = res.body();
		  String t = res.body();
		  s = s.substring(s.indexOf("=") + 1);
		  s = s.substring(0, s.indexOf("&"));
		  t = t.substring(t.lastIndexOf("=") + 1);
		  log.add(s);
		  log.add(t);
		  try {
				Statement mySelect = main.createStatement();
				mySelect.executeUpdate("INSERT INTO  manhuntUsers (username,password) VALUES ('"+s+"','"+t+"');");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		  return logOutPage(res, req);
	  }

	  private String welcomePage() {
	    String a = body().with(
	        h1("Manhunt"),
	        form().withAction("/").withMethod("post").with(
	        	    chooseUserInput("Choose Username"),
	        		choosePasswordInput("Choose Password"),
	        	    submitButton("Sign up"))).render();
	    a += body().with(
	    	form().withAction("/logged").withMethod("post").with(
        	    chooseUserInput("Your Username"),
        		choosePasswordInput("Your Password"),
        	    submitButton("Sign in"))).render();
	    return a;
	  }

		public static Tag choosePasswordInput(String placeholder) {
			return passwordInput("choosePassword", placeholder);
		}
		
		public static Tag chooseUserInput(String placeholder) {
			/*try {
				Statement mySelect = main.createStatement();
				mySelect.executeUpdate("INSERT INTO manhuntUsers (username) " + "VALUES ('pete')");
			} catch (SQLException e) {
				e.printStackTrace();
			}*/
			return userInput("chooseUsername", placeholder);
		}

		public static Tag passwordInput(String identifier, String placeholder) {
		    return input()
		        .withType("password")
		        .withId(identifier)
		        .withName(identifier)
		        .withPlaceholder(placeholder)
		        .isRequired();
		}
		
		public static Tag userInput(String identifier, String placeholder) {
		    return input()
		        .withType("username")
		        .withId(identifier)
		        .withName(identifier)
		        .withPlaceholder(placeholder)
		        .isRequired();
		}

		public static Tag submitButton(String text) {
		    return button().withType("submit").withText(text);
		}
}
