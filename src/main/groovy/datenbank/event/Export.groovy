package datenbank.event

import datenbank.model.TestCase
import datenbank.model.Variables

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.stage.FileChooser

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class Export implements EventHandler<ActionEvent> {

	def init

	def group = "-"
	FileOutputStream fileOutputStream
	ZipOutputStream zipOutputStream


	def startZip(zipFilePath) {
		fileOutputStream = new FileOutputStream(zipFilePath);
		zipOutputStream = new ZipOutputStream(fileOutputStream);
		return zipOutputStream
	}
	def endZip() {
		zipOutputStream.close();
		fileOutputStream.close();
	}
	def zipFile(File inputFile, String dir) {
		try {

			ZipEntry zipEntry = new ZipEntry(dir+"/"+inputFile.getName());
			zipOutputStream.putNextEntry(zipEntry);

			FileInputStream fileInputStream = new FileInputStream(inputFile);
			byte[] buf = new byte[1024];
			int bytesRead;


			while ((bytesRead = fileInputStream.read(buf)) > 0) {
				zipOutputStream.write(buf, 0, bytesRead);
			}


			zipOutputStream.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(ActionEvent arg0) {
		
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(init.ui.stage);
		
		Thread.start {
			try {

				


				if(file) {
					init.ui.btnUpdate(true)
					startZip("$file")
					def i = 0
					init.summary.testCases.each { testCase ->

						if(testCase.getGroup() == group || group == "-") {

							def tgtSQL = new File("${Variables.path}Target/${testCase.name}.sql")
							def tgtCSV = new File("${Variables.path}Target/Result/${testCase.name}.csv")
							def srcSQL = new File("${Variables.path}Source/${testCase.name}.sql")
							def srcCSV = new File("${Variables.path}Source/Result/${testCase.name}.csv")
							def compCSV = new File("${Variables.path}Report/${testCase.name}.csv")
							def after = new File("${Variables.path}Target/${testCase.name}_After.bat")
							def before = new File("${Variables.path}Target/${testCase.name}_Before.bat")

							if(tgtSQL.exists()) {
								zipFile(tgtSQL, "Target")
								i++
							}

							if(tgtCSV.exists())
								zipFile(tgtCSV, "Target/Result")
							if(srcSQL.exists())
								zipFile(srcSQL, "Source")
							if(srcCSV.exists())
								zipFile(srcCSV, "Source/Result")
							if(compCSV.exists())
								zipFile(compCSV, "Report")

							if(before.exists())
								zipFile(before, "Target")
							if(after.exists())
								zipFile(after, "Target")
						}
					}
					endZip()

					init.ui.confirm("Export Completed", "Exported $i test case(s)")
				}
			} catch(all) {
				init.ui.alert("Export Error", "$all")
			}
			init.ui.btnUpdate(false)
		}
	}
}
