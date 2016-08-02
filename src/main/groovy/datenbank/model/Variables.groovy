package datenbank.model

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class Variables {
	static path = "./"
	static model = "model.csv"
	static sourceConnection = "jdbc:jtds:sqlserver://localhost:1433/master"
	static targetConnection = "jdbc:jtds:sqlserver://localhost:1433/master"
	static sourceDriver = "net.sourceforge.jtds.jdbc.Driver"
	static targetDriver = "net.sourceforge.jtds.jdbc.Driver"
	
	def static void load()
	{

		def config = new ConfigSlurper().parse(new File("./conf.txt").text)
		
		if(config.model) {
			model = config.model			
		}
		
		if(config.path) {
			path = config.path
		}

		if(config.source) {
			sourceConnection = config.source
		}
		
		if(config.target) {
			targetConnection = config.target
		}
		
		if(config.sourceDriver) {
			sourceDriver = config.sourceDriver
		}
		
		if(config.targetDriver) {
			targetDriver = config.targetDriver
		}
		
		
		
	}
	

	
	
}

