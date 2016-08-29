package datenbank.view
import datenbank.model.Model
import datenbank.model.Summary
import datenbank.model.Group
import datenbank.model.Variables
import groovy.sql.Sql
import javafx.scene.input.ClipboardContent
import javafx.scene.input.DragEvent
import javafx.scene.input.Dragboard
import javafx.scene.input.TransferMode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
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
import javafx.scene.control.CheckBox
import javafx.scene.control.ContextMenu
import javafx.scene.control.Control
import javafx.scene.control.ComboBox
import javafx.scene.control.MenuItem
import javafx.scene.control.MenuBar
import javafx.scene.control.Slider
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
import javafx.stage.DirectoryChooser
import javafx.stage.Stage;
import javafx.stage.WindowEvent
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.geometry.Pos
import javafx.stage.FileChooser

class Settings {

	def init
	def changed = false
	def edit = true;

	def groupList = []
	def popupStage
	def tv
	
	TextField pathField
	ComboBox delimiter
	TextField csvReader
	CheckBox saveHist
	Slider sliderFetch
	Slider sliderDOB
	
	def isChanged() {
		groupList.each {
			if(Variables.config.groups."$it.group".target != it.target)
				changed = true		
				
			if(Variables.config.groups."$it.group".source != it.source)
				changed = true
				
			if(Variables.config.groups."$it.group".targetDriver != it.targetDriver)
				changed = true
			if(Variables.config.groups."$it.group".sourceDriver != it.sourceDriver)
				changed = true
			
			if(Variables.config.groups."$it.group".sqlProgramTarget != it.sqlProgramTarget)
				changed = true
			if(Variables.config.groups."$it.group".sqlProgramSource != it.sqlProgramSource)
				changed = true
		}
		
		if(Variables.csvSeperator != delimiter.getValue().toString())
			changed = true
		if(Variables.csvReader != csvReader.getText())
			changed = true
		if(Variables.saveCompareHistory != saveHist.isSelected())
			changed = true
		if(Variables.sqlFetchSize != (int)sliderFetch.getValue())
			changed = true
		if(Variables.degreeOfParallelism != (int)sliderDOB.getValue())
			changed = true
			
		if(Variables.path != pathField.getText())
			changed = true

		
	}
	
	def saveValues() {
		Variables.config.remove('groups')
		groupList.each {
			Variables.config.groups."$it.group".target = it.target
			Variables.config.groups."$it.group".source = it.source
			Variables.config.groups."$it.group".targetDriver = it.targetDriver
			Variables.config.groups."$it.group".sourceDriver = it.sourceDriver
			
			Variables.config.groups."$it.group".sqlProgramTarget = it.sqlProgramTarget
			Variables.config.groups."$it.group".sqlProgramSource = it.sqlProgramSource
		}
		
		def tmpPath = pathField.getText()

		if(tmpPath.endsWith("/") || tmpPath.endsWith("\\")) {
			Variables.path = tmpPath
		} else {

			Variables.path = tmpPath + "/"
		}		
		Variables.csvSeperator = delimiter.getValue().toString()
		Variables.csvReader = csvReader.getText()
		Variables.saveCompareHistory = saveHist.isSelected()
		Variables.sqlFetchSize = (int)sliderFetch.getValue()
		Variables.degreeOfParallelism = (int)sliderDOB.getValue()
		Variables.save()
	}
	
	def setValues() {
		pathField.setText(Variables.path)
		delimiter.setValue(Variables.csvSeperator)
		csvReader.setText(Variables.csvReader)
		saveHist.setSelected(Variables.saveCompareHistory)
		sliderFetch.setValue(Variables.sqlFetchSize)
		sliderDOB.setValue(Variables.degreeOfParallelism)
		
		
		Variables.config.groups.each {
		
			groupList << new Group(group: it.key, target: Variables.config.groups."$it.key".target
				, source: Variables.config.groups."$it.key".source
				, targetDriver: Variables.config.groups."$it.key".targetDriver
				, sourceDriver: Variables.config.groups."$it.key".sourceDriver
				, sqlProgramSource: Variables.config.groups."$it.key".sqlProgramSource
				, sqlProgramTarget: Variables.config.groups."$it.key".sqlProgramTarget)
			
		}
		
	}
	
	
	def groupsPopup(group) {

		if(group.group)
			edit = false;
		else
			edit = true


		popupStage = new Stage();
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));


		TextField groupTxt = new TextField()
		groupTxt.setEditable(edit)
		groupTxt.setDisable(!edit)
		grid.add(new Label("Group: "), 0, 0)
		grid.add(groupTxt, 1, 0)

		ComboBox source = new ComboBox()
		source.setEditable(true)
		source.setItems(FXCollections.observableArrayList(["jdbc:jtds:sqlserver://<server>:1433/<database>;instance=<instance>;user=<usr>;password=<pwd>", "jdbc:oracle:thin:<user>/<password>@//localhost:1521/xe"]))
		grid.add(new Label("Source: "), 0, 1)
		grid.add(source, 1, 1)
		


		ComboBox sourceDriver = new ComboBox()
		sourceDriver.setEditable(true)
		sourceDriver.setItems(FXCollections.observableArrayList(["net.sourceforge.jtds.jdbc.Driver", "oracle.jdbc.driver.OracleDriver"]))
		grid.add(new Label("Source Driver: "), 0, 2)
		grid.add(sourceDriver, 1, 2)
		
		Button sourceTest = new Button("Test")
		sourceTest.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
				def sql = Sql.newInstance( source.getValue().toString(), sourceDriver.getValue().toString() )
				init.ui.confirm("Connected", "Connection established.")
				}
				catch(all) {
					init.ui.alert("Couldn't connect","$all")
				}
				
			}
		});
		grid.add(sourceTest, 2, 1)

		ComboBox target = new ComboBox()
		target.setEditable(true)
		target.setItems(FXCollections.observableArrayList(["jdbc:jtds:sqlserver://<server>:1433/<database>;instance=<instance>;user=<usr>;password=<pwd>", "jdbc:oracle:thin:<user>/<password>@//localhost:1521/xe"]))
		grid.add(new Label("Target: "), 0, 3)
		grid.add(target, 1, 3)


		ComboBox targetDriver = new ComboBox()
		targetDriver.setEditable(true)
		targetDriver.setItems(FXCollections.observableArrayList(["net.sourceforge.jtds.jdbc.Driver", "oracle.jdbc.driver.OracleDriver"]))
		grid.add(new Label("Target Driver: "), 0, 4)
		grid.add(targetDriver, 1, 4)
		
		
		Button targetTest = new Button("Test")
		targetTest.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
				def sql = Sql.newInstance( target.getValue().toString(), targetDriver.getValue().toString() )
				init.ui.confirm("Connected", "Connection established.")
				}
				catch(all) {
					init.ui.alert("Couldn't connect","$all")
				}
				
			}
		});
		grid.add(targetTest, 2, 3)

		Button fileSrc = new Button("Open..")
		TextField appSrc = new TextField()
		grid.add(new Label("SQL Program (Source): "), 0, 5)
		grid.add(appSrc, 1, 5)
		grid.add(fileSrc, 2, 5)
		
		fileSrc.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				File f = fileChooser.showOpenDialog(popupStage);
				if(f)
					appSrc.setText("$f")
			}
		});

		Button fileTgt = new Button("Open..")
		TextField appTgt = new TextField()
		grid.add(new Label("SQL Program (Target): "), 0, 6)
		grid.add(appTgt, 1, 6)
		grid.add(fileTgt, 2, 6)
		
		
		fileTgt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				File f = fileChooser.showOpenDialog(popupStage);
				if(f)
					appTgt.setText("$f")
			}
		});

		HBox bntBox = new HBox()
		Button btnAdd = new Button("Save")
		Button btnCancel = new Button("Cancel")

		if(group.group)
			groupTxt.setText(group.group)
		if(group.source)
			source.setValue(group.source)
		if(group.target)
			target.setValue(group.target)
		if(group.sourceDriver)
			sourceDriver.setValue(group.sourceDriver)
		if(group.targetDriver)
			targetDriver.setValue(group.targetDriver)
		if(group.sqlProgramSource)
			appSrc.setText(group.sqlProgramSource)
		if(group.sqlProgramTarget)
			appTgt.setText(group.sqlProgramTarget)

		btnAdd.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						group.group = groupTxt.getText()
						group.source = source.getValue().toString()
						group.target = target.getValue().toString()
						group.sourceDriver = sourceDriver.getValue().toString()
						group.targetDriver = targetDriver.getValue().toString()
						group.sqlProgramSource = appSrc.getText()
						group.sqlProgramTarget = appTgt.getText()
						if(edit) {
							if(groupList.findAll { it.group == group.group}.size() == 0 && group.group != "") {
								groupList << group
								updateGroupTable()
								popupStage.close()
							} else {
								init.ui.alert("Cannot add group", "Group already exists.")
							}
						} else {
							updateGroupTable()
							popupStage.close()
						}
					}
				});


		btnCancel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						popupStage.close()
					}
				});

		bntBox.getChildren().addAll(btnAdd, btnCancel)

		grid.add(bntBox, 0, 7)


		VBox editorBox = new VBox()
		editorBox.getChildren().addAll(grid)


		def scene = new Scene(editorBox, 850, 400)

		popupStage.setTitle("Add group");
		def icon = new Image(getClass().getResourceAsStream("icon.png"))
		popupStage.getIcons().add(icon);
		popupStage.setScene(scene);
		popupStage.setResizable(false)
		popupStage.show();
	}

	def settingsEditor() {
		Stage stage = new Stage();
		MenuBar menu = new MenuBar()
		def fileMenu = new Menu("File")

		menu.getMenus().addAll(fileMenu)

		def itemSave = new MenuItem("Save");

		itemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		fileMenu.getItems().add(itemSave);
		
		itemSave.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						try {
							saveValues()
							init.ui.confirm("Settings saved", "The settings were saved successfully.")
						} catch(all) {
							init.ui.alert("Settings not saved correctly", "$all")
						}
						
					}
				});

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		def pathLabel = new Label("Directory")
		pathLabel.setFont(new Font("Arial", 18));
		grid.add(pathLabel, 0, 0)

		Button pathBtn = new Button("Open..")
		pathField = new TextField()
		grid.add(new Label("Path: "), 0, 1)
		grid.add(pathField, 1, 1)
		grid.add(pathBtn, 2, 1)

		pathBtn.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						DirectoryChooser dir = new DirectoryChooser();
						File f = dir.showDialog(stage);
						if(f)
							pathField.setText("$f")
					}
				});


		def csvLabel = new Label("CSV")
		csvLabel.setFont(new Font("Arial", 18));
		grid.add(csvLabel, 0, 2)

		delimiter = new ComboBox()
		delimiter.setItems(FXCollections.observableArrayList([";", ","]))
		delimiter.setValue(";")
		grid.add(new Label("CSV Delimiter: "), 0, 3)
		grid.add(delimiter, 1, 3)

		Button file = new Button("Open..")
		csvReader = new TextField()
		grid.add(new Label("CSV Reader: "), 0, 4)
		grid.add(csvReader, 1, 4)
		grid.add(file, 2, 4)

		file.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						FileChooser fileChooser = new FileChooser();
						File f = fileChooser.showOpenDialog(stage);
						if(f)
							csvReader.setText("$f")
					}
				});

		saveHist = new CheckBox()
		grid.add(new Label("Save compare history: "), 0, 5)
		grid.add(saveHist, 1, 5)


		def perfLabel = new Label("Performance")
		perfLabel.setFont(new Font("Arial", 18));
		grid.add(perfLabel, 0, 6)
		sliderFetch = new Slider();
		sliderFetch.setMin(1);
		sliderFetch.setMax(1000);
		sliderFetch.setValue(10)
		sliderFetch.setShowTickLabels(true)
		sliderFetch.setMajorTickUnit(499);
		sliderFetch.setMinorTickCount(5);
		sliderFetch.setBlockIncrement(10);

		grid.add(new Label("JDBC Fetch Size: "), 0, 7)
		grid.add(sliderFetch, 1, 7)

		sliderDOB = new Slider();
		sliderDOB.setMin(1);
		sliderDOB.setMax(10);
		sliderDOB.setValue(1);
		sliderDOB.setShowTickLabels(true)
		sliderDOB.setMajorTickUnit(1);
		sliderDOB.setBlockIncrement(1);

		grid.add(new Label("Degree of parallelism: "), 0, 8)

		grid.add(sliderDOB, 1, 8)

		def conLabel = new Label("Connections")
		conLabel.setFont(new Font("Arial", 18));
		grid.add(conLabel, 0, 9)

		def addLabel = new Label("Add")
		addLabel.setFont(new Font("Arial", 9));
		addLabel.setTextFill(Color.DARKBLUE );
		grid.add(addLabel, 1, 9)



		addLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
							if(mouseEvent.getClickCount() == 2){
								groupsPopup(new Group())
							}
						}
					}
				})

		tv = new TableView()
		def colGrp = new TableColumn("Group")
		def colSrc = new TableColumn("Source")
		def colTgt = new TableColumn("Target")

		colGrp.setCellValueFactory(new PropertyValueFactory("group"))
		colSrc.setCellValueFactory(new PropertyValueFactory("source"))
		colTgt.setCellValueFactory(new PropertyValueFactory("target"))

		tv.getColumns().addAll(colGrp, colSrc, colTgt)
		grid.add(tv, 0, 10, 4,2)


		updateGroupTable()

		ContextMenu rightMenu = new ContextMenu();
		MenuItem itemEdit= new MenuItem("Edit");
		MenuItem itemRemove = new MenuItem("Remove");

		rightMenu.getItems().add(itemEdit);
		rightMenu.getItems().add(itemRemove);

		tv.setContextMenu(rightMenu);
		itemEdit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def grp = (Group) tv.getSelectionModel().getSelectedItem();
						if(grp) {
							groupsPopup(grp)
						} else {
							init.ui.alert("Couldn't edit","No group selected")
						}
					}
				});
		itemRemove.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						def grp = (Group) tv.getSelectionModel().getSelectedItem();
						if(grp) {
							groupList.remove(grp)
							updateGroupTable()
						} else {
							init.ui.alert("Couldn't remove","No group selected")
						}
					}
				});

		VBox editorBox = new VBox()
		editorBox.getChildren().addAll(menu, grid)

		setValues()
		def scene = new Scene(editorBox, 400, 600)

		stage.setTitle("Settings");
		def icon = new Image(getClass().getResourceAsStream("icon.png"))
		stage.getIcons().add(icon);
		stage.setScene(scene);
		stage.setResizable(false)
		
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				isChanged()
				if(changed) {
					if(init.ui.accept("Settings changed", "Do you want to save the file before closing?")) {
						try {							
							saveValues()
						} catch(all) {
							init.ui.alert("Settings not saved correctly", "$all")
						}
					}
				}

			}
		});
		stage.show();
		
		
	}

	def updateGroupTable() {
		Platform.runLater(new Runnable() {
					@Override public void run() {
						tv.getItems().removeAll(tv.getItems());
						def l = FXCollections.observableArrayList(groupList)
						tv.setItems(l)
					}
				})
	}
}
