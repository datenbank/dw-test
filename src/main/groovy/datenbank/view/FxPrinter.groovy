package datenbank.view

import datenbank.engine.Executor
import datenbank.engine.Init
import datenbank.engine.ResultTester
import datenbank.model.Summary
import datenbank.model.TestCase
import datenbank.model.Variables

import java.util.Observer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu
import javafx.scene.control.Control
import javafx.scene.control.MenuItem
import javafx.scene.control.Callback
import javafx.scene.control.Alert
import javafx.scene.control.Menu
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox;
import javafx.util.Callback
import javafx.scene.paint.Color;



class FxPrinter extends Application implements Observer {

	def tv
	def compare, exec
	def menu
	def itemExec, itemComp, itemOpenTgt, itemOpenSrc, itemOpenBefore, itemOpenAfter, itemResultTgt, itemResultSrc, itemResult, itemSettings, itemSettingsLoad
	@Override
	public void start(Stage primaryStage) throws Exception {

		tv = new TableView()
		def box = new VBox()

		def colFile = new TableColumn("Name")
		def colSkipped = new TableColumn("Skipped")
		def colError = new TableColumn("Status")
		def colResultFlag = new TableColumn("Result flag")
		def colElapsed = new TableColumn("Elapsed")
		def colElapsedTest = new TableColumn("Elapsed compare")

		colError.setCellFactory(new Callback<TableColumn<TestCase, Integer>, TableCell<TestCase, Integer>>() {
					@Override
					public TableCell<TestCase, Integer> call(TableColumn<TestCase, Integer> param) {
						return new TableCell<TestCase, Integer>() {

									@Override
									protected void updateItem(Integer item, boolean empty) {
										super.updateItem(item, empty);
										if(item == -1) {
											setTextFill(Color.BLACK);
											setText("")
										}
										if(item == 1) {
											setTextFill(Color.RED);
											setText("FAILURE Execute")
										}
										if(item == 2) {
											setTextFill(Color.RED);
											setText("FAILURE Compare")
										}

										if(item == 3) {
											setTextFill(Color.GREEN);
											setText("SUCCESS Execute")
										}

										if(item == 4) {
											setTextFill(Color.GREEN);
											setText("SUCCESS Compare")
										}
									}
								};
					}
				});


		colFile.setCellValueFactory(new PropertyValueFactory("name"))
		colSkipped.setCellValueFactory(new PropertyValueFactory("skipped"))
		colError.setCellValueFactory(new PropertyValueFactory("errors"))
		colResultFlag.setCellValueFactory(new PropertyValueFactory("resultFlag"))
		colElapsed.setCellValueFactory(new PropertyValueFactory("elapsed"))
		colElapsedTest.setCellValueFactory(new PropertyValueFactory("elapsedTest"))
		def init = new Init(ui: this)
		def summary = init.init()

		colFile.width = 200
		colError.width = 200
		colElapsedTest.width = 150
		tv.getColumns().addAll(colFile, colSkipped, colError, colResultFlag, colElapsed, colElapsedTest)

		def rt = new ResultTester()
		def ex = new Executor()
		compare = new Button("Compare All");
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

		exec = new Button("Execute All");
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


		menu = new ContextMenu();
		itemExec = new MenuItem("Execute");
		itemComp = new MenuItem("Compare");
		itemOpenTgt = new MenuItem("Open/Create target SQL");
		itemOpenSrc = new MenuItem("Open/Create source SQL");
		menu.getItems().add(itemExec);
		menu.getItems().add(itemComp);



		Menu codeGrp = new Menu("Code");
		Menu callbackGrp = new Menu("Callback");
		Menu resultGrp = new Menu("Result");
		Menu settingsGrp = new Menu("Settings");

		itemOpenBefore = new MenuItem("Open/Create before (.bat)");
		itemOpenAfter = new MenuItem("Open/Create after (.bat)");

		callbackGrp.getItems().add(itemOpenBefore);
		callbackGrp.getItems().add(itemOpenAfter);

		codeGrp.getItems().add(itemOpenTgt);
		codeGrp.getItems().add(itemOpenSrc);
		codeGrp.getItems().add(callbackGrp);

		menu.getItems().add(codeGrp);

		itemResultTgt = new MenuItem("Open target data set");
		itemResultSrc = new MenuItem("Open source data set");
		itemResult = new MenuItem("Open difference..");
		resultGrp.getItems().add(itemResultTgt);
		resultGrp.getItems().add(itemResultSrc);
		resultGrp.getItems().add(itemResult);

		menu.getItems().add(resultGrp);
		SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
		menu.getItems().add(separatorMenuItem);

		itemSettings = new MenuItem("Open file");
		itemSettingsLoad = new MenuItem("Reload");

		settingsGrp.getItems().add(itemSettings);
		settingsGrp.getItems().add(itemSettingsLoad);
		menu.getItems().add(settingsGrp);

		tv.setContextMenu(menu);

		itemExec.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();
						Thread.start {

							btnUpdate(true)
							ex.runOne(testCase)
							btnUpdate(false)
						}
					}
				});

		itemComp.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();
						Thread.start {

							btnUpdate(true)
							rt.runOne(testCase)
							btnUpdate(false)
						}
					}
				});




		itemOpenTgt.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();

						btnUpdate(true)

						Thread.start {

							btnUpdate(true)
							try {
								"notepad ${Variables.path}Target/${testCase.name}.sql".execute()
							} catch(all) {
								alert("Open file error", "Couldn't open file. Please check that it exists!")
							}

							btnUpdate(false)
						}
					}
				});


		itemOpenSrc.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();

						btnUpdate(true)

						Thread.start {

							btnUpdate(true)
							try {
								"notepad ${Variables.path}Source/${testCase.name}.sql".execute()
							} catch(all) {
								alert("Open file error", "Couldn't open file. Please check that it exists!")
							}

							btnUpdate(false)
						}
					}
				});

		itemOpenBefore.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();

						btnUpdate(true)

						Thread.start {

							btnUpdate(true)
							try {
								"notepad ${Variables.path}Target/${testCase.name}_Before.bat".execute()
							} catch(all) {
								alert("Open file error", "Couldn't open file. Please check that it exists!")
							}

							btnUpdate(false)
						}
					}
				});
		itemOpenAfter.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();

						btnUpdate(true)

						Thread.start {

							btnUpdate(true)
							try {
								"notepad ${Variables.path}Target/${testCase.name}_After.bat".execute()
							} catch(all) {
								alert("Open file error", "Couldn't open file. Please check that it exists!")
							}

							btnUpdate(false)
						}
					}
				});

		itemResultTgt.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();

						Thread.start {
							btnUpdate(true)
							if(new File("${Variables.path}Target/Result/${testCase.name}.csv").exists()) {
								"notepad ${Variables.path}Target/Result/${testCase.name}.csv".execute()
							} else {
								alert("Open file error", "Couldn't open file. Please check that it exists!\n${Variables.path}Target/Result/${testCase.name}.csv")
							}

							btnUpdate(false)
						}
					}
				});
		itemResultSrc.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();

						Thread.start {
							btnUpdate(true)
							if(new File("${Variables.path}Source/Result/${testCase.name}.csv").exists()) {
								"notepad ${Variables.path}Source/Result/${testCase.name}.csv".execute()
							} else {
								alert("Open file error", "Couldn't open file. Please check that it exists!\n${Variables.path}Source/Result/${testCase.name}.csv")
							}

							btnUpdate(false)
						}
					}
				});


		itemResult.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();

						Thread.start {
							btnUpdate(true)
							if(new File("${Variables.path}Report/${testCase.name}.csv").exists()) {
								"notepad ${Variables.path}Report/${testCase.name}.csv".execute()
							} else {
								alert("Open file error", "Couldn't open file. Please check that it exists!\n${Variables.path}Report/${testCase.name}.csv")
							}

							btnUpdate(false)
						}
					}
				});

		itemSettings.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();

						Thread.start {
							if(new File("${Variables.path}conf.txt").exists()) {
								"notepad ${Variables.path}conf.txt".execute()
							} else {
								alert("Open file error", "Couldn't open file. Please check that it exists!\n${Variables.path}conf.txt")
							}

						}
					}
				});

		itemSettingsLoad.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Variables.load()
					}
				});
		def hbox = new HBox()
		hbox.getChildren().addAll(exec, compare)
		box.getChildren().addAll(tv, hbox);
		primaryStage.setScene(new Scene(box,800,400));
		primaryStage.setResizable(false)
		primaryStage.setTitle("DW Test Toolkit")
		primaryStage.show();
	}

	def btnUpdate(bool) {
		Platform.runLater(new Runnable() {
					@Override public void run() {
						itemComp.setDisable(bool)
						itemExec.setDisable(bool)
						itemOpenSrc.setDisable(bool)
						itemOpenTgt.setDisable(bool)

						itemOpenBefore.setDisable(bool)
						itemOpenAfter.setDisable(bool)
						itemResultTgt.setDisable(bool)
						itemResultSrc.setDisable(bool)
						itemResult.setDisable(bool)

						exec.setDisable(bool)
						compare.setDisable(bool)
					}
				});
	}


	def alert(header, msg) {
		Platform.runLater(new Runnable() {
					@Override public void run() {
						def alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Error Dialog");
						alert.setHeaderText(header);
						alert.setContentText(msg);
						alert.showAndWait();
					}
				})
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		if(arg0 instanceof Summary) {
			Platform.runLater(new Runnable() {
						@Override public void run() {
							tv?.getItems().removeAll(arg0.testCases)
							tv?.setItems(FXCollections.observableArrayList(arg0.testCases))
						}
					});
		}

		if(arg0 instanceof TestCase) {
			Platform.runLater(new Runnable() {
						@Override public void run() {

							def items = []
							tv?.getItems().each {testCase -> items << testCase }

							tv?.getItems().removeAll(items)
							tv?.setItems(FXCollections.observableArrayList(items))
						}
					});
		}
	}
}
