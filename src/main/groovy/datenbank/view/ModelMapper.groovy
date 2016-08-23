package datenbank.view
import datenbank.model.Model
import datenbank.model.Summary
import datenbank.model.TestCase
import datenbank.model.Variables

import javafx.scene.input.ClipboardContent
import javafx.scene.input.DragEvent
import javafx.scene.input.Dragboard
import javafx.scene.input.TransferMode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox;
import javafx.util.Callback
import javafx.scene.paint.Color;
import javafx.scene.text.Font
import javafx.scene.Cursor
import javafx.scene.layout.Priority
import javafx.scene.control.ScrollPane
import javafx.scene.control.ScrollPane.ScrollBarPolicy
import javafx.scene.control.ComboBox;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.input.KeyEvent
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu
import javafx.scene.control.Control
import javafx.scene.control.ComboBox
import javafx.scene.control.MenuItem
import javafx.scene.control.MenuBar
import javafx.scene.control.ToolBar
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.Menu
import javafx.scene.control.ProgressBar
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog
import javafx.scene.input.MouseButton
import javafx.stage.Stage;
import javafx.stage.WindowEvent
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class ModelMapper {
	
	def init
	def m
	def srcColumns
	def tgtColumns
	ComboBox srcComboBox
	ComboBox tgtComboBox
	def whereSource
	def whereTarget

	
	def modelEditor() {
		
		
		m = new Model()
		MenuBar menu = new MenuBar()
		def fileMenu = new Menu("File")
		def loadMenu = new Menu("Groups")
		loadMenu.setDisable(true)
		menu.getMenus().addAll(fileMenu, loadMenu)




		def itemSave = new MenuItem("Save");

		itemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		fileMenu.getItems().add(itemSave);


		VBox editorBox = new VBox()
		editorBox.setSpacing(20)

		m.loadModelFromFile()

		VBox srcBox = new VBox()
		VBox tgtBox = new VBox()
		def srcTablesOption = FXCollections.observableArrayList(m.srcTables);
		srcComboBox = new ComboBox(srcTablesOption);
		srcComboBox.setValue(m.srcTables[0])

		def tgtTablesOption = FXCollections.observableArrayList(m.tables);
		tgtComboBox = new ComboBox(tgtTablesOption);
		tgtComboBox.setValue(m.tables[0])


		srcComboBox.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {


						def selected =  srcComboBox.getValue().toString()

						def t

						m.srcTables.each {

							if(it.toString() == selected) {
								t = it
							}
						}


						srcBox.getChildren().clear()
						srcColumns = sourceColumns(t)
						srcBox.getChildren().addAll(new Label("Source:"),srcComboBox, srcColumns);

					}
				})

		tgtComboBox.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def selected =  tgtComboBox.getValue().toString()
						def t

						m.tables.each {
							if(it.toString() == selected) {
								t = it
							}
						}
						tgtBox.getChildren().clear()
						tgtColumns = targetColumns(t)
						tgtBox.getChildren().addAll(new Label("Target:"), tgtComboBox, tgtColumns);

					}
				})
		srcColumns = sourceColumns(m.srcTables[0])
		tgtColumns = targetColumns(m.tables[0])
		srcBox.getChildren().addAll(new Label("Source:"),srcComboBox, srcColumns);
		tgtBox.getChildren().addAll(new Label("Target:"),tgtComboBox, tgtColumns);





		Stage stage = new Stage();
		stage.setTitle("Model Mapper Helper");
		def icon = new Image(getClass().getResourceAsStream("icon.png"))
		stage.getIcons().add(icon);
		HBox hbox = new HBox()
		hbox.getChildren().addAll(srcBox,tgtBox);


		HBox testTypesBox = new HBox()
		testTypesBox.getChildren().add(new Label("Test types: |"))
		Variables.testType.each {
			def testTypeLabel = new Label(it)
			def space = new Label(" | ")
			testTypesBox.getChildren().addAll(testTypeLabel,space)

			testTypeLabel.setOnDragDetected(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent dragEvent) {
							Dragboard db = testTypeLabel.startDragAndDrop(TransferMode.ANY);

							/* put a string on dragboard */
							ClipboardContent content = new ClipboardContent();
							content.putString(testTypeLabel.getText());
							db.setContent(content);

							dragEvent.consume();
						}
					})

		}

		editorBox.getChildren().addAll(menu, testTypesBox, hbox);
		
		def sp = new ScrollPane()
		sp.setFitToWidth(true);
		sp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		sp.setContent(editorBox);
		def scene = new Scene(sp, 800, 500,  Color.WHITE)

		stage.setScene(scene);
		//stage.setResizable(false)
		stage.show();

		itemSave.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						loadMenu.setDisable(true)
						fileMenu.setDisable(true)
						itemSave.setDisable(true)
						stage.getScene().setCursor(Cursor.WAIT);
						m.writeModelToFile()
						init.ui.confirm("Saved", "The file is saved.")
						loadMenu.setDisable(false)
						fileMenu.setDisable(false)
						itemSave.setDisable(false)
						stage.getScene().setCursor(Cursor.DEFAULT);

					}
				})

		def lstGrp = ["Default"]
		Variables.config.groups.each {
			
			lstGrp << it.key
			
		}
		lstGrp.each {

			loadMenu.setDisable(false)

			def grp ="$it"
			def itemGroup = new MenuItem("Load from $grp");


			loadMenu.getItems().add(itemGroup);

			itemGroup.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							Thread.start {
								loadMenu.setDisable(true)
								fileMenu.setDisable(true)
								itemSave.setDisable(true)
								stage.getScene().setCursor(Cursor.WAIT);
								
								try {
									m.loadModelFromDb(grp)
									
	
									def tgt = FXCollections.observableArrayList(m.tables);
									def src = FXCollections.observableArrayList(m.srcTables);
									Platform.runLater(new Runnable() {
												@Override public void run() {
													tgtComboBox.setItems(tgt)
													tgtComboBox.setValue(m.tables[0])
													srcComboBox.setItems(src);
													srcComboBox.setValue(m.srcTables[0])
												}
									})
	
	
									
									init.ui.confirm("Load from Database", "Model is updated")
								} catch (all) {
									init.ui.alert("Load error", "$all")
								}
								stage.getScene().setCursor(Cursor.DEFAULT);
								loadMenu.setDisable(false)
								fileMenu.setDisable(false)
								itemSave.setDisable(false)
							}

						}
					})

		}

	}
	
	def sourceColumns(table) {
		VBox vbox = new VBox()
		if(table) {
			table.columns.each {

				def l = new Label(it.column)


				l.setOnDragDetected(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent dragEvent) {
								Dragboard db = l.startDragAndDrop(TransferMode.ANY);

								/* put a string on dragboard */
								ClipboardContent content = new ClipboardContent();
								content.putString(l.getText());
								db.setContent(content);

								dragEvent.consume();
							}
						})


				l.setOnDragDone(new EventHandler <DragEvent>() {
							public void handle(DragEvent event) {
								/* the drag-and-drop gesture ended */

								Dragboard db = event.getDragboard();


								/* if the data was successfully moved, clear it */
								if (event.getTransferMode() == TransferMode.MOVE) {
									//l.setText("");
								}

								event.consume();
							}
						});

				vbox.getChildren().addAll(l);
			}
			whereSource = new TextField(table.where)
			vbox.setPadding(new Insets(10, 10, 10, 10));
			vbox.setSpacing(10)
			vbox.getChildren().addAll(new Label("WHERE:"), whereSource);

			whereSource.setOnKeyPressed(new EventHandler<KeyEvent>() {
						public void handle(KeyEvent ke) {
							table.where = whereSource.getText() == "" ? "-" : whereSource.getText()

						}
					});
		}
		return vbox
	}


	def targetColumns(table) {
		VBox vbox = new VBox()
		if(table) {

			table.columns.each { col ->

				def l = new Label(col.toString())



				l.setOnMouseClicked(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent mouseEvent) {
								if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
									if(mouseEvent.getClickCount() == 2){
										col.columnRef = null
										col.testType = "-"
										l.setText(col.toString());
									}
								}
							}
						})

				l.setOnDragEntered(new EventHandler <DragEvent>() {
							public void handle(DragEvent event) {
								/* the drag-and-drop gesture entered the target */
								//System.out.println("onDragEntered");
								/* show to the user that it is an actual gesture target */
								if (event.getGestureSource() != l &&
								event.getDragboard().hasString()) {
									l.setTextFill(Color.GREEN);
								}

								event.consume();
							}
						});

				l.setOnDragExited(new EventHandler <DragEvent>() {
							public void handle(DragEvent event) {
								/* mouse moved away, remove the graphical cues */
								l.setTextFill(Color.BLACK);

								event.consume();
							}
						});

				l.setOnDragDropped(new EventHandler <DragEvent>() {
							public void handle(DragEvent event) {
								/* data dropped */

								/* if there is a string data on dragboard, read it and use it */
								Dragboard db = event.getDragboard();
								boolean success = false;
								if (db.hasString()) {
									def map = m.srcTables.find {it.toString() == srcComboBox.getValue().toString()}.columns.find { it.column == db.getString() }
									def tab = m.tables.find {it.toString() == tgtComboBox.getValue().toString()}
									tab.where = whereTarget.getText() == "" ? "-" : whereTarget.getText()
									if(map) {
										col.columnRef = map
										col.tableRef.where = whereSource.getText() == "" ? "-" : whereSource.getText()
									}
									else {

										def tt = m.tables.find {it.toString() == tgtComboBox.getValue().toString()}.columns.find { it.column == l.getText().split(" -> ").reverse()[0].split(" <- ")[0] }

										if(!tt.columnRef)
											init.ui.confirm("No column mapping", "Map a source column to the target column before adding a test type.")
										if(tt.testType != "-" && !tt.testType.contains(db.getString()))
											tt.testType += " | ${db.getString()}"
										else if(!tt.testType.contains(db.getString()))
											tt.testType = "${db.getString()}"
									}


									l.setText(col.toString());
									success = true;
								}
								/* let the source know whether the string was successfully
						 * transferred and used */
								event.setDropCompleted(success);

								event.consume();
							}
						});
				l.setOnDragOver(new EventHandler <DragEvent>() {
							public void handle(DragEvent event) {
								if (event.getGestureSource() != l &&
								event.getDragboard().hasString()) {
									/* allow for both copying and moving, whatever user chooses */
									event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
								}

								event.consume();
							}
						});


				vbox.getChildren().addAll(l);
			}
			whereTarget = new TextField(table.where)
			vbox.setPadding(new Insets(10, 10, 10, 10));
			vbox.setSpacing(10)
			vbox.getChildren().addAll(new Label("WHERE:"),whereTarget);


			whereTarget.setOnKeyPressed(new EventHandler<KeyEvent>() {
						public void handle(KeyEvent ke) {
							table.where = whereTarget.getText() == "" ? "-" : whereTarget.getText()

						}
					});
		}
		return vbox
	}
}
