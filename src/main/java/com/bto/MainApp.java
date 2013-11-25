package com.bto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bto.model.entities.tournaments.DoubleTeam;
import com.bto.model.entities.tournaments.Player;
import com.bto.model.persistence.EntityManagerUtil;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
    	
    	EntityManagerUtil.beginTransaction();
    	
/*    	Player player1 = new Player();

    	Player player2 = new Player();
    	Player player3 = new Player();
    	Player player4 = new Player();
    	
    	DoubleTeam doubleTeam1 = new DoubleTeam();
    	DoubleTeam doubleTeam2 = new DoubleTeam();
    	
    	player1.getPlayer1OfADoubleteam().add(doubleTeam1);
    	player2.getPlayer2OfADoubleteam().add(doubleTeam1);
    	
    	player3.getPlayer1OfADoubleteam().add(doubleTeam2);
    	player1.getPlayer2OfADoubleteam().add(doubleTeam2);
    	    	
    	EntityManagerUtil.save(player2);
    	EntityManagerUtil.save(player1);
    	EntityManagerUtil.save(player3);
    	*/
    	
    	for(int i = 0; i< 1000; i++)
    	{
    		EntityManagerUtil.save(new Player());
    	}
    	
    	EntityManagerUtil.commit();
    	EntityManagerUtil.closeEntityManager();
    	EntityManagerUtil.beginTransaction();
    	DoubleTeam doubleTeamFromdb = EntityManagerUtil.getEntityManager().find(DoubleTeam.class, 1l);
    	System.out.println(doubleTeamFromdb.toString());
    	
    	EntityManagerUtil.commit();
    	EntityManagerUtil.closeEntityManager();

        log.info("Starting Hello JavaFX and Maven demonstration application");

        String fxmlFile = "/fxml/hello.fxml";
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

        log.debug("Showing JFX scene");
        Scene scene = new Scene(rootNode, 400, 200);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle("Hello JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }
}
