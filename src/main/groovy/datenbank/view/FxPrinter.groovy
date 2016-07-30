package datenbank.view

import datenbank.engine.Executor
import datenbank.engine.Init
import datenbank.engine.ResultTester
import datenbank.model.Summary
import datenbank.model.TestCase

import java.util.Observer;


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
		
		
        colFile.setCellValueFactory(new PropertyValueFactory("name"))
 	    colCompared.setCellValueFactory(new PropertyValueFactory("compared"))
		colSkipped.setCellValueFactory(new PropertyValueFactory("skipped"))
		colError.setCellValueFactory(new PropertyValueFactory("errors"))
		colResultFlag.setCellValueFactory(new PropertyValueFactory("resultFlag"))
		
		
		def init = new Init(ui: this)
		def summary = init.init()
		
		tv.getColumns().addAll(colFile, colCompared, colSkipped, colError, colResultFlag)

		def rt = new ResultTester()
		def ex = new Executor()
        compare = new Button("Compare...");
		compare.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				compare.setDisable(true)
				rt.runAll(summary)
			}
		});
	
		exec = new Button("Execute...");
		exec.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				exec.setDisable(true)
				ex.runAll(summary)
			}
		});
        
		def hbox = new HBox()
		hbox.getChildren().addAll(exec, compare)
        box.getChildren().addAll(tv, hbox);
        primaryStage.setScene(new Scene(box,400,400));
		primaryStage.setTitle("DW Test Toolkit")
        primaryStage.show();
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		println arg0
		if(arg0 instanceof Summary) {
			
				tv?.getItems().removeAll(arg0.testCases)
				tv?.setItems(FXCollections.observableArrayList(arg0.testCases))
				
			
		}

		compare?.setDisable(false)
		exec?.setDisable(false)

	}

}
