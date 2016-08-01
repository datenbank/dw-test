package datenbank.view

import datenbank.engine.Executor
import datenbank.engine.Init
import datenbank.engine.ResultTester
import datenbank.model.Summary
import datenbank.model.TestCase

import java.util.Observer;




import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox;

class FxPrinter extends Application implements Observer {
	
	def tv
	def compare, exec
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		tv = new TableView()
		def box = new VBox()		

		
        def colFile = new TableColumn("Name")
		def colCompared = new TableColumn("Compared")
		def colSkipped = new TableColumn("Skipped")
		def colError = new TableColumn("Error")
		def colResultFlag = new TableColumn("Result flag")
		def colElapsed = new TableColumn("Elapsed")
		def colElapsedTest = new TableColumn("Elapsed compare")
		
        colFile.setCellValueFactory(new PropertyValueFactory("name"))
 	    colCompared.setCellValueFactory(new PropertyValueFactory("compared"))
		colSkipped.setCellValueFactory(new PropertyValueFactory("skipped"))
		colError.setCellValueFactory(new PropertyValueFactory("errors"))
		colResultFlag.setCellValueFactory(new PropertyValueFactory("resultFlag"))
		colElapsed.setCellValueFactory(new PropertyValueFactory("elapsed"))
		colElapsedTest.setCellValueFactory(new PropertyValueFactory("elapsedTest"))
		def init = new Init(ui: this)
		def summary = init.init()
		
		colFile.width = 200
		colElapsedTest.width = 200
		tv.getColumns().addAll(colFile, colCompared, colSkipped, colError, colResultFlag, colElapsed, colElapsedTest)

		def rt = new ResultTester()
		def ex = new Executor()
        compare = new Button("Compare...");
		compare.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				Thread.start {

					btnUpdate(true)
					rt.runAll(summary)
					btnUpdate(false)
				}
			}
		});
	
		exec = new Button("Execute...");
		exec.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				Thread.start {

					btnUpdate(true)
					ex.runAll(summary)
					btnUpdate(false)
				}
				
			}
		});
        
		def hbox = new HBox()
		hbox.getChildren().addAll(exec, compare)
        box.getChildren().addAll(tv, hbox);
        primaryStage.setScene(new Scene(box,800,400));
		primaryStage.setTitle("DW Test Toolkit")
        primaryStage.show();
		
	}
	
	def btnUpdate(bool) {
		Platform.runLater(new Runnable() {
			@Override public void run() {
				exec.setDisable(bool)
				compare.setDisable(bool)
			}
		});
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		println arg0
		if(arg0 instanceof Summary) {			
			Platform.runLater(new Runnable() {
				@Override public void run() {
					tv?.getItems().removeAll(arg0.testCases)
					tv?.setItems(FXCollections.observableArrayList(arg0.testCases))
				}
			});
			
			
		}


	}

}
