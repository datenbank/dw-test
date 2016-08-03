package datenbank.view

import datenbank.engine.Executor
import datenbank.engine.Init
import datenbank.engine.ResultTester
import datenbank.model.Model
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
	def compare, exec, both
	def menu
	def itemExec, itemComp, itemOpenTgt, itemOpenSrc, itemOpenBefore, itemOpenAfter, itemResultTgt, itemResultSrc, itemResult, itemSettings, itemSettingsLoad


	def init
	def summary


	@Override
	public void start(Stage primaryStage) throws Exception {

		tv = new TableView()
		def box = new VBox()

		def colFile = new TableColumn("Name")
		def colError = new TableColumn("Status")
		def colResultFlag = new TableColumn("Description")
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

		colResultFlag.setCellFactory(new Callback<TableColumn<TestCase, Integer>, TableCell<TestCase, Integer>>() {
					@Override
					public TableCell<TestCase, Integer> call(TableColumn<TestCase, Integer> param) {
						return new TableCell<TestCase, Integer>() {

									@Override
									protected void updateItem(Integer item, boolean empty) {
										super.updateItem(item, empty);
										setText("")
										if(item == -1) {
											setTextFill(Color.BLACK);
											setText("Couldn't compare results!")
										}
										if(item == 1) {
											setTextFill(Color.BLACK);
											setText("Missing rows in source")
										}
										if(item == 2) {
											setTextFill(Color.BLACK);
											setText("Missing rows in target")
										}
										if(item == 3) {
											setTextFill(Color.BLACK);
											setText("Missing rows in both")
										}
									}
								};
					}
				});
		colFile.setCellValueFactory(new PropertyValueFactory("name"))
		colError.setCellValueFactory(new PropertyValueFactory("errors"))
		colResultFlag.setCellValueFactory(new PropertyValueFactory("resultFlag"))
		colElapsed.setCellValueFactory(new PropertyValueFactory("elapsed"))
		colElapsedTest.setCellValueFactory(new PropertyValueFactory("elapsedTest"))

		init = new Init(ui: this)
		summary = init.init()

		colFile.width = 200
		colError.width = 150
		colResultFlag.width = 200
		colElapsedTest.width = 150
		tv.getColumns().addAll(colFile, colError, colResultFlag, colElapsed, colElapsedTest)

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
		both = new Button("Execute and Compare All");
		both.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {

						Thread.start {

							btnUpdate(true)

							summary.testCases.each { testCase ->
								ex.runOne(testCase)
								rt.runOne(testCase)
							}


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
		Menu scriptsGrp = new Menu("Scripts");

		itemOpenBefore = new MenuItem("Open/Create before (.bat)");
		itemOpenAfter = new MenuItem("Open/Create after (.bat)");

		callbackGrp.getItems().add(itemOpenBefore);
		callbackGrp.getItems().add(itemOpenAfter);

		def dir = new File("${Variables.path}Scripts")
		dir.eachFile() { file ->
			def script = new MenuItem("Open $file.name");
			scriptsGrp.getItems().add(script);

			script.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							codeEditor(file, "Java")
						}
					});
		}

		SeparatorMenuItem separatorMenuItemScript = new SeparatorMenuItem();
		scriptsGrp.getItems().add(separatorMenuItemScript);
		dir.eachFile() { file ->
			def script = new MenuItem("Run $file.name");
			scriptsGrp.getItems().add(script);

			script.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							//TODO move to controller!
							def m = new Model()
							m.loadModelFromFile()
							Binding binding = new Binding();
							binding.setVariable("model", m);
							binding.setVariable("path", Variables.path);
							GroovyShell shell = new GroovyShell(binding);
							try {
								shell.evaluate(file.text);
								Variables.load()
								summary = init.init()
							} catch(all) {
								alert("Open file error", "Couldn't open file. Please check that it exists!")
							}

						}
					});
		}



		codeGrp.getItems().add(itemOpenTgt);
		codeGrp.getItems().add(itemOpenSrc);
		codeGrp.getItems().add(callbackGrp);
		codeGrp.getItems().add(scriptsGrp);

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
						if(testCase) {
							Thread.start {

								btnUpdate(true)
								ex.runOne(testCase)
								btnUpdate(false)
							}
						}
					}
				});

		itemComp.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();
						if(testCase) {
							Thread.start {

								btnUpdate(true)
								rt.runOne(testCase)
								btnUpdate(false)
							}
						}
					}
				});




		itemOpenTgt.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();
						if(testCase) {
							def file = new File("${Variables.path}Target/${testCase.name}.sql")
							codeEditor(file, "SQL")
						}
					}
				});


		itemOpenSrc.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();
						if(testCase) {
							def file = new File("${Variables.path}Source/${testCase.name}.sql")
							codeEditor(file, "SQL")
						}
					}
				});

		itemOpenBefore.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();
						if(testCase) {
							def file = new File("${Variables.path}Target/${testCase.name}_Before.bat")
							codeEditor(file, "BAT")
						}
					}
				});
		itemOpenAfter.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();
						if(testCase) {
							
							def file = new File("${Variables.path}Target/${testCase.name}_After.bat")
							codeEditor(file, "BAT")

						}
					}
				});

		itemResultTgt.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();
						if(testCase) {
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
					}
				});
		itemResultSrc.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();
						if(testCase) {
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
					}
				});


		itemResult.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();
						if(testCase) {
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
					}
				});

		itemSettings.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def file = new File("${Variables.path}conf.txt")
						codeEditor(file, "BAT")

					}
				});

		itemSettingsLoad.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Variables.load()
						summary = init.init()

					}
				});
			
		def hbox = new HBox()
		hbox.getChildren().addAll(exec, compare, both)
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
						both.setDisable(bool)
					}
				});
	}


	def alert(header, msg) {
		Platform.runLater(new Runnable() {
					@Override public void run() {
						println msg
						def alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Error Dialog");
						alert.setHeaderText(header);
						alert.setContentText(msg);
						alert.showAndWait();
					}
				})
	}

	def confirm(header, msg) {
		Platform.runLater(new Runnable() {
					@Override public void run() {
						println msg
						def alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Confirm Dialog");
						alert.setHeaderText(header);
						alert.setContentText(msg);
						alert.showAndWait();
					}
				})
	}


	def codeEditor(file, type) {
		
		if(!file.exists()) {
			file << ""

		}
		

		CodeEditor editor = new CodeEditor(file.text, type);
		Button editorBtn = new Button("Save")
		VBox editorBox = new VBox()
		editorBox.getChildren().addAll(editor, editorBtn);

		Stage stage = new Stage();
		stage.setTitle(file.name);
		stage.setScene(new Scene(editorBox, 800, 400));
		stage.setResizable(false)
		stage.show();

		editorBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent eventButton) {
						editorBtn.setDisable(true)
						file.newWriter().withWriter { w ->
							w << editor.getCodeAndSnapshot()
						}
						confirm("Saved", "The file is saved.")
						editorBtn.setDisable(false)

					}
				});

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
