package datenbank.view
import datenbank.model.Model
import datenbank.model.Summary
import datenbank.model.Group
import datenbank.model.Variables

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
	def groupsPopup() {



		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));


		TextField group = new TextField()
		grid.add(new Label("Group: "), 0, 0)
		grid.add(group, 1, 0)

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

		Button fileSrc = new Button("Open..")
		TextField appSrc = new TextField()
		grid.add(new Label("SQL Program (Source): "), 0, 5)
		grid.add(appSrc, 1, 5)
		grid.add(fileSrc, 2, 5)

		Button fileTgt = new Button("Open..")
		TextField appTgt = new TextField()
		grid.add(new Label("SQL Program (Target): "), 0, 6)
		grid.add(appTgt, 1, 6)
		grid.add(fileTgt, 2, 6)

		HBox bntBox = new HBox()
		Button btnAdd = new Button("Add")
		Button btnCancel = new Button("Cancel")
		
		bntBox.getChildren().addAll(btnAdd, btnCancel)
		
		grid.add(bntBox, 0, 7)

		
		VBox editorBox = new VBox()
		editorBox.getChildren().addAll(grid)


		def scene = new Scene(editorBox, 850, 400)
		Stage stage = new Stage();
		stage.setTitle("Add group");
		def icon = new Image(getClass().getResourceAsStream("icon.png"))
		stage.getIcons().add(icon);
		stage.setScene(scene);
		//stage.setResizable(false)
		stage.show();
	}
	def settingsEditor() {

		MenuBar menu = new MenuBar()
		def fileMenu = new Menu("File")

		menu.getMenus().addAll(fileMenu)

		def itemSave = new MenuItem("Save");

		itemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		fileMenu.getItems().add(itemSave);


		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));


		def csvLabel = new Label("CSV")
		csvLabel.setFont(new Font("Arial", 18));
		grid.add(csvLabel, 0, 0)

		ComboBox delimiter = new ComboBox()
		delimiter.setItems(FXCollections.observableArrayList([";", ","]))
		delimiter.setValue(";")
		grid.add(new Label("CSV Delimiter: "), 0, 1)
		grid.add(delimiter, 1, 1)

		Button file = new Button("Open..")
		TextField csvReader = new TextField()
		grid.add(new Label("CSV Reader: "), 0, 2)
		grid.add(csvReader, 1, 2)
		grid.add(file, 2, 2)

		CheckBox saveHist = new CheckBox()
		grid.add(new Label("Save compare history: "), 0, 3)
		grid.add(saveHist, 1, 3)


		def perfLabel = new Label("Performance")
		perfLabel.setFont(new Font("Arial", 18));
		grid.add(perfLabel, 0, 4)
		Slider sliderFetch = new Slider();
		sliderFetch.setMin(1);
		sliderFetch.setMax(100);
		sliderFetch.setValue(10)
		sliderFetch.setShowTickLabels(true)
		sliderFetch.setMajorTickUnit(49);
		sliderFetch.setMinorTickCount(5);
		sliderFetch.setBlockIncrement(10);

		grid.add(new Label("JDBC Fetch Size: "), 0, 5)
		grid.add(sliderFetch, 1, 5)

		Slider sliderDOB = new Slider();
		sliderDOB.setMin(1);
		sliderDOB.setMax(10);
		sliderDOB.setValue(1);
		sliderDOB.setShowTickLabels(true)
		sliderDOB.setMajorTickUnit(1);
		sliderDOB.setBlockIncrement(1);

		grid.add(new Label("Degree of parallelism: "), 0, 6)

		grid.add(sliderDOB, 1, 6)

		def conLabel = new Label("Connections")
		conLabel.setFont(new Font("Arial", 18));
		grid.add(conLabel, 0, 7)

		def addLabel = new Label("Add")
		addLabel.setFont(new Font("Arial", 9));
		addLabel.setTextFill(Color.DARKBLUE );
		grid.add(addLabel, 1, 7)



		addLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
							if(mouseEvent.getClickCount() == 2){
								groupsPopup()
							}
						}
					}
				})

		def tv = new TableView()
		def colGrp = new TableColumn("Group")
		def colSrc = new TableColumn("Source")
		def colTgt = new TableColumn("Target")

		colGrp.setCellValueFactory(new PropertyValueFactory("group"))
		colSrc.setCellValueFactory(new PropertyValueFactory("source"))
		colTgt.setCellValueFactory(new PropertyValueFactory("target"))

		tv.getColumns().addAll(colGrp, colSrc, colTgt)
		grid.add(tv, 0, 8, 4,2)

		def ll = []

		ll << new Group(group: "test", source: "jdbc:jtds:sqlserver://localhost:1433/master", target: "jdbc:jtds:sqlserver://localhost:1433/master")
		ll << new Group(group: "test2", source: "source2", target: "target2")
		def l = FXCollections.observableArrayList(ll)
		FilteredList fl = new FilteredList(l);

		tv.setItems(fl)

		ContextMenu rightMenu = new ContextMenu();
		MenuItem itemEdit= new MenuItem("Edit");
		MenuItem itemRemove = new MenuItem("Remove");

		rightMenu.getItems().add(itemEdit);
		rightMenu.getItems().add(itemRemove);

		tv.setContextMenu(rightMenu);

		VBox editorBox = new VBox()
		editorBox.getChildren().addAll(menu, grid)


		def scene = new Scene(editorBox, 400, 600)
		Stage stage = new Stage();
		stage.setTitle("Settings");
		def icon = new Image(getClass().getResourceAsStream("icon.png"))
		stage.getIcons().add(icon);
		stage.setScene(scene);
		stage.setResizable(false)
		stage.show();
	}
}
