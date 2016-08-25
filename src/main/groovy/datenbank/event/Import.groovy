package datenbank.event

import datenbank.model.TestCase
import datenbank.model.Variables

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.stage.FileChooser

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream;

class Import implements EventHandler<ActionEvent> {

	def init

	def i = 0


	def unZipIt(String zipFile){

		byte[] buffer = new byte[1024];


		//create output directory is not exists
		File folder = new File("${Variables.path}");
		if(!folder.exists()){
			folder.mkdir();
		}

		//get the zip file content
		ZipInputStream zis =new ZipInputStream(new FileInputStream(zipFile));
		//get the zipped file list entry
		ZipEntry ze = zis.getNextEntry();

		while(ze!=null){

			String fileName = ze.getName();
			File newFile = new File("${Variables.path}$fileName");

			i++

			//create all non exists folders
			//else you will hit FileNotFoundException for compressed folder
			new File(newFile.getParent()).mkdirs();

			FileOutputStream fos = new FileOutputStream(newFile);

			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}

			fos.close();
			ze = zis.getNextEntry();
		}

		zis.closeEntry();
		zis.close();

	}

	@Override
	public void handle(ActionEvent arg0) {
		if(init.ui.accept("Import Started", "This will overwrite existing test cases by same name!")) {
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
			fileChooser.getExtensionFilters().add(extFilter);
			File file = fileChooser.showOpenDialog(init.ui.stage);

			Thread.start {
				try {
	
					if(file) {
						init.ui.btnUpdate(true)
						unZipIt("$file")
						init.ui.confirm("Import Completed", "Imported $i file(s)")
						init.ui.summary = init.init()
	
					}

				} catch(all) {
					init.ui.alert("Import Error", "$all")
				}
				init.ui.btnUpdate(false)
			}
		}
	}
}
