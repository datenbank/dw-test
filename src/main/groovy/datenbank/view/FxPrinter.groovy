package datenbank.view

import datenbank.engine.Init
import datenbank.engine.ResultTester
import datenbank.model.Summary
import datenbank.model.TestCase

import java.util.Observer;

import org.hamcrest.core.IsNull;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

class FxPrinter extends Application implements Observer {
	
	def tv
	def run
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		tv = new TableView()
		def box = new VBox()		

		
        def colFile = new TableColumn("Name")
		def colCompared = new TableColumn("Compared")
		def colSkipped = new TableColumn("Skipped")
		def colError = new TableColumn("Error")
		def colResultFlag = new TableColumn("Result flag")
		
		
        colFile.setCellValueFactory(new PropertyValueFactory("name"))
 	    colCompared.setCellValueFactory(new PropertyValueFactory("compared"))
		colSkipped.setCellValueFactory(new PropertyValueFactory("skipped"))
		colError.setCellValueFactory(new PropertyValueFactory("errors"))
		colResultFlag.setCellValueFactory(new PropertyValueFactory("resultFlag"))
		
		
		def init = new Init(ui: this)
		def summary = init.init()
		
		tv.getColumns().addAll(colFile, colCompared, colSkipped, colError, colResultFlag)

		def rt = new ResultTester()
		
        run = new Button("Run...");
		run.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				println "action.."
				run.setDisable(true)
				rt.runAll(summary)
			}
		});
        
        box.getChildren().addAll(tv, run);
        primaryStage.setScene(new Scene(box,400,400));
        primaryStage.show();
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		println arg0
		if(arg0 instanceof Summary) {
			
				tv?.getItems().removeAll(arg0.testCases)
				tv?.setItems(FXCollections.observableArrayList(arg0.testCases))
				
			
		}

		run?.setDisable(false)

	}

}
