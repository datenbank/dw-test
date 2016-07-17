package datenbank.model

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class Variables {

	static load()
	{
		def file = new File("./conf.txt")
		file.text.split("\r\n").each {
			if(it.startsWith("path=")) {
				path = it.substring(5, it.length())
			}
				
			if(it.startsWith("model=")) {
				model = it.substring(6, it.length())
			}
				
			
			if(it.startsWith("source=")) {
				sourceConnection = it.substring(7, it.length())
			}
				
			if(it.startsWith("target=")) {
				targetConnection = it.substring(7, it.length())
			}
			
			
			if(it.startsWith("targetDriver=")) {
				targetDriver = it.substring(13, it.length())
			}
			
			
			if(it.startsWith("sourceDriver=")) {
				sourceDriver = it.substring(13, it.length())
			}
		}
		
		
		
	}
	
	static path = "C:/Users/khansen/Desktop/Test/"
	static model = "model.csv"
	static sourceConnection = "jdbc:jtds:sqlserver://localhost:1433/master"
	static targetConnection = "jdbc:jtds:sqlserver://localhost:1433/master"
	static sourceDriver = "net.sourceforge.jtds.jdbc.Driver"
	static targetDriver = "net.sourceforge.jtds.jdbc.Driver"
	
	
}

