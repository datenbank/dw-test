package datenbank.view

import datenbank.model.TestCase

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

class FxPrinter extends Application implements Observer {
	def tv
	static main(String[] args) {
		launch(FxPrinter.class, args)
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		def box = new VBox();
		
		tv = new TableView();
		
		def file = ""
		
		def compared = 0
		def skipped = 0
		def errors = 0
		def total = 0
		def elapsed, start
		def linesNotInSource = 0
		def linesNotInTarget = 0
		def resultFlag = 0
		
		
        def colFile = new TableColumn("File")
		def colCompared = new TableColumn("Compared")
		def colSkipped = new TableColumn("Skipped")
		def colError = new TableColumn("Error")
		def colResultFlag = new TableColumn("Result flag")
		
		
        colFile.setCellValueFactory(new PropertyValueFactory("file"))
 	    colCompared.setCellValueFactory(new PropertyValueFactory("compared"))
		colSkipped.setCellValueFactory(new PropertyValueFactory("skipped"))
		colError.setCellValueFactory(new PropertyValueFactory("errors"))
		colResultFlag.setCellValueFactory(new PropertyValueFactory("resultFlag"))
		
		
		tv.getColumns().addAll(colFile, colCompared, colSkipped, colError, colResultFlag)

		def data = []
		def i = new TestCase(file: "test.sql", compared:-1)

		data << i
		
		i = new TestCase(file: "test2.sql", compared:-1)
		
		data << i
		
		tv.setItems(FXCollections.observableArrayList(data))
		
		
        def run = new Button("Run...");
        
        box.getChildren().addAll(tv, run);
        primaryStage.setScene(new Scene(box,400,400));
        primaryStage.show();
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		
	}

}
