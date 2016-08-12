package datenbank.view

import datenbank.engine.Executor
import datenbank.engine.Init
import datenbank.engine.ResultTester
import datenbank.event.*
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
import javafx.scene.control.MenuBar
import javafx.scene.control.ToolBar
import javafx.scene.control.Callback
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.Menu
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox;
import javafx.util.Callback
import javafx.scene.paint.Color;
import javafx.scene.Cursor
import javafx.scene.layout.Priority;


class FxPrinter extends Application implements Observer {
	def vbox
	def menuBar
	def groupMenu
	def tv
	def menu
	def compButton, newButton, execButton, bothButton
	def itemExec, itemComp, itemOpenTgt, itemOpenSrc, itemOpenBefore, itemOpenAfter, itemResultTgt, itemResultSrc, itemResult, itemSettings, itemSettingsLoad, itemDel, itemRename
	
	def init
	def summary
	def stage
	def icon

	@Override
	public void start(Stage primaryStage) throws Exception {


		primaryStage.setTitle("DW Test Toolkit")
		icon = new Image(getClass().getResourceAsStream("icon.png"))
		primaryStage.getIcons().add(icon);
		stage = primaryStage //use show hour glass on long running tasks from btnUpdate
		init = new Init(ui: this)
		summary = init.init()
		vbox = new VBox()

		menu()


		newButton = new Button()
		newButton.setTooltip(new Tooltip("New test case"))
		newButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("plus-16.png"))))

		execButton = new Button()
		execButton.setTooltip(new Tooltip("Execute all"))
		execButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("video-play-16.png"))))

		compButton = new Button()
		compButton.setTooltip(new Tooltip("Compare all"))
		compButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("search-3-16.png"))))

		bothButton = new Button()
		bothButton.setTooltip(new Tooltip("Execute/Compare all"))
		bothButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("arrow-46-16.png"))))


		ToolBar toolBar = new ToolBar(newButton, execButton, compButton, bothButton);

		
		/**
		 * 
		 * Set up table
		 *
		 */
		tv = new TableView()
		vbox.setVgrow(tv, Priority.ALWAYS);
		vbox.getChildren().addAll(toolBar, tv)
		def scene =new Scene(vbox,800,400)
		primaryStage.setScene(scene);

		def colGrp = new TableColumn("Group")
		def colFile = new TableColumn("Name")
		def colError = new TableColumn("Status")
		def colResultFlag = new TableColumn("Description")
		def colElapsed = new TableColumn("Elapsed")
		def colElapsedTest = new TableColumn("Elapsed compare")

		colGrp.setCellFactory(new Callback<TableColumn<TestCase, String>, TableCell<TestCase, String>>() {
					@Override
					public TableCell<TestCase, String> call(TableColumn<TestCase, String> param) {
						return new TableCell<TestCase, String>() {

									@Override
									protected void updateItem(String item, boolean empty) {
										super.updateItem(item, empty);
										setText("")
										if(item) {

											def fileSplit = item.split("#")
											if(fileSplit.size()>1)
												setText(fileSplit[0])
											else
												setText("Default")
										}

									}
								};
					}
				});

		colFile.setCellFactory(new Callback<TableColumn<TestCase, String>, TableCell<TestCase, String>>() {
					@Override
					public TableCell<TestCase, String> call(TableColumn<TestCase, String> param) {
						return new TableCell<TestCase, String>() {

									@Override
									protected void updateItem(String item, boolean empty) {
										super.updateItem(item, empty);
										setText("")
										if(item) {

											def fileSplit = item.split("#")
											if(fileSplit.size()>1) {

												def display = []
												def i = 1
												fileSplit.each {
													if(i>1)
														display << it
													i++
												}

												setText(display.join("#"))
											}
											else
												setText(item)
										}

									}
								};
					}
				});

		colError.setCellFactory(new Callback<TableColumn<TestCase, Integer>, TableCell<TestCase, Integer>>() {
					@Override
					public TableCell<TestCase, Integer> call(TableColumn<TestCase, Integer> param) {
						return new TableCell<TestCase, Integer>() {

									@Override
									protected void updateItem(Integer item, boolean empty) {
										setText("")
										super.updateItem(item, empty);
										if(item == -1 || item == null) {
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
		colGrp.setCellValueFactory(new PropertyValueFactory("name"))
		colFile.setCellValueFactory(new PropertyValueFactory("name"))
		colError.setCellValueFactory(new PropertyValueFactory("errors"))
		colResultFlag.setCellValueFactory(new PropertyValueFactory("resultFlag"))
		colElapsed.setCellValueFactory(new PropertyValueFactory("elapsed"))
		colElapsedTest.setCellValueFactory(new PropertyValueFactory("elapsedTest"))

		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY)
		tv.getColumns().addAll(colGrp, colFile, colError, colResultFlag, colElapsed, colElapsedTest)

		/**
		 *
		 * Right click menu
		 *
		 */
		menu = new ContextMenu();
		itemExec = new MenuItem("Execute");
		itemComp = new MenuItem("Compare");

		menu.getItems().add(itemExec);
		menu.getItems().add(itemComp);

		Menu codeGrp = new Menu("Code");
		itemOpenTgt = new MenuItem("Open/Create target SQL");
		itemOpenSrc = new MenuItem("Open/Create source SQL");

		Menu callbackGrp = new Menu("Callback");
		itemOpenBefore = new MenuItem("Open/Create before (.bat)");
		itemOpenAfter = new MenuItem("Open/Create after (.bat)");
		callbackGrp.getItems().add(itemOpenBefore);
		callbackGrp.getItems().add(itemOpenAfter);

		Menu resultGrp = new Menu("Result");

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

		SeparatorMenuItem separator = new SeparatorMenuItem();
		menu.getItems().add(separator);

		itemDel = new MenuItem("Delete test case");
		itemDel.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("minus-16.png"))))
		menu.getItems().add(itemDel);

		itemRename = new MenuItem("Rename test case");
		itemRename.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("edit-6-16.png"))))
		menu.getItems().add(itemRename);

		tv.setContextMenu(menu);



		/**
		 *
		 * Set up actions/events
		 *
		 */
		def execute = new Execute(init: init)
		def compare = new Compare(init: init)
		def compareAll = new CompareAll(init: init)
		def executeAll = new ExecuteAll(init: init)
		def executeCompareAll = new ExecuteCompareAll(init: init)
		def newTestCase = new NewTestCase(init: init)
		def deleteTestCase = new DeleteTestCase(init: init)
		def renameTestCase = new RenameTestCase(init: init)

		itemExec.setOnAction(execute);
		itemComp.setOnAction(compare);
		itemDel.setOnAction(deleteTestCase);
		itemRename.setOnAction(renameTestCase);

		newButton.setOnAction(newTestCase);
		compButton.setOnAction(compareAll)
		execButton.setOnAction(executeAll);
		bothButton.setOnAction(executeCompareAll);



		


		itemOpenTgt.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();

						if(testCase) {

							if(Variables.sqlProgramTarget != "") {
								"${Variables.sqlProgramTarget} ${Variables.path}Target/${testCase.name}.sql".execute()
							} else {
								def file = new File("${Variables.path}Target/${testCase.name}.sql")
								codeEditor(file, "SQL")
							}
						} else {
							alert("Couldn't open", "No test case selected.")
						}
					}
				});


		itemOpenSrc.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def testCase = (TestCase) tv.getSelectionModel().getSelectedItem();
						if(testCase) {
							if(Variables.sqlProgramSource != "") {
								"${Variables.sqlProgramSource} ${Variables.path}Source/${testCase.name}.sql".execute()
							} else {
								def file = new File("${Variables.path}Source/${testCase.name}.sql")
								codeEditor(file, "SQL")
							}

						} else {
							alert("Couldn't open", "No test case selected.")
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
						} else {
							alert("Couldn't open", "No test case selected.")
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

						} else {
							alert("Couldn't open", "No test case selected.")
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
									"${Variables.csvReader} \"${Variables.path}Target/Result/${testCase.name}.csv\"".execute()
								} else {
									alert("Open file error", "Couldn't open file. Please check that it exists!\n${Variables.path}Target/Result/${testCase.name}.csv")
								}

								btnUpdate(false)
							}
						} else {
							alert("Couldn't open", "No test case selected.")
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
									"${Variables.csvReader} \"${Variables.path}Source/Result/${testCase.name}.csv\"".execute()
								} else {
									alert("Open file error", "Couldn't open file. Please check that it exists!\n${Variables.path}Source/Result/${testCase.name}.csv")
								}

								btnUpdate(false)
							}
						} else {
							alert("Couldn't open", "No test case selected.")
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
									"${Variables.csvReader} \"${Variables.path}Report/${testCase.name}.csv\"".execute()
								} else {
									alert("Open file error", "Couldn't open file. Please check that it exists!\n${Variables.path}Report/${testCase.name}.csv")
								}

								btnUpdate(false)
							}
						}
						else {
							alert("Couldn't open", "No test case selected.")
						}
					}
				});




		primaryStage.show();

	}
	
	def menu() {
		if(vbox.getChildren().size() > 0)
			vbox.getChildren().remove(0)
		menuBar = new MenuBar();
		vbox.getChildren().add(0, menuBar)
		Menu settingsGrp = new Menu("Settings");
		Menu scriptsGrp = new Menu("Scripts");


		itemSettings = new MenuItem("Open file");
		itemSettingsLoad = new MenuItem("Reload");
		settingsGrp.getItems().add(itemSettings);
		settingsGrp.getItems().add(itemSettingsLoad);

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
						menu();


					}
				});


		menuBar.getMenus().add(settingsGrp)
		menuBar.getMenus().add(scriptsGrp)
		
		
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


		dir.eachFile() { file ->
			def script = new MenuItem("Run $file.name");
			scriptsGrp.getItems().add(script);
			def runScript = new RunScript(init: init, file: file)
			script.setOnAction(runScript);
		}

		groupMenu = new Menu("Groups");

		menuBar.getMenus().add(groupMenu)

		groupMenu.setDisable(true)
		summary.getGroups().each {

			groupMenu.setDisable(false)
			Menu group = new Menu("$it");

			def menuItemForGroupExec = new MenuItem("Execute $it");
			group.getItems().add(menuItemForGroupExec);

			def groupName = it

			menuItemForGroupExec.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							Thread.start {

								btnUpdate(true)

								init.summary.testCases.each { testCase ->

									if(testCase.group == groupName) {
										init.ex.runOne(testCase)
									}
								}

								btnUpdate(false)

							}
						}
					});

			def menuItemForGroupComp = new MenuItem("Compare $it");
			group.getItems().add(menuItemForGroupComp);
			menuItemForGroupComp.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							Thread.start {

								btnUpdate(true)

								init.summary.testCases.each { testCase ->

									if(testCase.group == groupName) {
										init.rt.runOne(testCase)
									}
								}

								btnUpdate(false)

							}
						}
					});
			def menuItemForGroupExecComp = new MenuItem("Execute/Compare $it");
			group.getItems().add(menuItemForGroupExecComp);
			menuItemForGroupExecComp.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							Thread.start {

								btnUpdate(true)

								init.summary.testCases.each { testCase ->

									if(testCase.group == groupName) {
										init.ex.runOne(testCase)
										init.rt.runOne(testCase)
									}
								}

								btnUpdate(false)

							}
						}
					});
			groupMenu.getItems().add(group);
			
		}


	}

	/**
	 *
	 * Disable/enable different menu items and buttons
	 *
	 */

	def btnUpdate(bool) {
		Platform.runLater(new Runnable() {
					@Override public void run() {


						if(bool == true) {
							stage.getScene().setCursor(Cursor.WAIT);
						} else {
							stage.getScene().setCursor(Cursor.DEFAULT);
						}


						itemComp.setDisable(bool)
						itemExec.setDisable(bool)
						itemOpenSrc.setDisable(bool)
						itemOpenTgt.setDisable(bool)

						itemOpenBefore.setDisable(bool)
						itemOpenAfter.setDisable(bool)
						itemResultTgt.setDisable(bool)
						itemResultSrc.setDisable(bool)
						itemResult.setDisable(bool)

						execButton.setDisable(bool)
						compButton.setDisable(bool)
						bothButton.setDisable(bool)

						groupMenu.setDisable(bool)
					}
				});
	}


	/**
	 *
	 * Alert the user
	 *
	 */


	def alert(header, msg) {
		Platform.runLater(new Runnable() {
					@Override public void run() {


						def alert = new Alert(Alert.AlertType.ERROR);
						Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
						stageAlert.getIcons().add(icon);

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
						def alert = new Alert(Alert.AlertType.INFORMATION);
						Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
						stageAlert.getIcons().add(icon);
						alert.setTitle("Information Dialog");
						alert.setHeaderText(header);
						alert.setContentText(msg);
						alert.showAndWait();

					}
				})

	}

	def accept(header, msg) {
		def go = false

		def alert = new Alert(Alert.AlertType.CONFIRMATION);
		Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
		stageAlert.getIcons().add(icon);
		alert.setTitle("Confirm Dialog");
		alert.setHeaderText(header);
		alert.setContentText(msg);


		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			go = true
		}

		return go
	}

	def input(header, msg) {
		TextInputDialog dialog = new TextInputDialog("");
		Stage stageAlert = (Stage) dialog.getDialogPane().getScene().getWindow();
		stageAlert.getIcons().add(icon);
		dialog.setTitle("Text Input Dialog");
		dialog.setHeaderText(header);
		dialog.setContentText(msg);
		Optional<String> result = dialog.showAndWait();
		return result.get();
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
		stage.getIcons().add(icon);
		stage.setScene(new Scene(editorBox, 800, 770));
		stage.setResizable(false)
		stage.show();

		editorBtn.setOnAction(new CodeEditorSave(init: init, file: file, editor: editor))

	}

	/**
	 *
	 * Update the table view, when notified by model objects
	 *
	 */	

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
