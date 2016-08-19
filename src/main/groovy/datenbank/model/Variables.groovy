package datenbank.model

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class Variables {
	static path = "./"
	static model = "model.csv"
	static sourceModel = "source.csv"
	static targetModel = "target.csv"
	
	static sourceConnection = "jdbc:jtds:sqlserver://localhost:1433/master"
	static targetConnection = "jdbc:jtds:sqlserver://localhost:1433/master"
	static sourceDriver = "net.sourceforge.jtds.jdbc.Driver"
	static targetDriver = "net.sourceforge.jtds.jdbc.Driver"
	static csvReader = ""
	static csvSeperator = ";"
	
	static sqlProgramTarget = ""
	static sqlProgramSource = ""
	static sqlFetchSize = 10
	
	static saveCompareHistory = false
	
	static config
	
	def static void load()
	{
		path = "./"
		model = "model.csv"
		sourceModel = "source.csv"
		targetModel = "target.csv"
		
		sourceConnection = "jdbc:jtds:sqlserver://localhost:1433/master"
		targetConnection = "jdbc:jtds:sqlserver://localhost:1433/master"
		sourceDriver = "net.sourceforge.jtds.jdbc.Driver"
		targetDriver = "net.sourceforge.jtds.jdbc.Driver"
		csvReader = ""
		csvSeperator = ";"
		
		sqlProgramTarget = ""
		sqlProgramSource = ""
		
		sqlFetchSize = 10
		
		saveCompareHistory = false
		
		config = new ConfigSlurper().parse(new File("./conf.txt").text)
		
		if(config.model) {
			model = config.model			
		}
		
		if(config.sourceModel) {
			sourceModel = config.sourceModel
		}
		if(config.targetModel) {
			targetModel = config.targetModel
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
		if(config.csvReader) {
			csvReader = config.csvReader
		}
		if(config.csvSeperator) {
			csvSeperator = config.csvSeperator
		}
		if(config.sqlProgramTarget) {
			sqlProgramTarget = config.sqlProgramTarget
		}
		if(config.sqlProgramSource) {
			sqlProgramSource = config.sqlProgramSource
		}
		if(config.sqlFetchSize) {
			sqlFetchSize = config.sqlFetchSize
		}
		if(config.saveCompareHistory) {
			saveCompareHistory = config.saveCompareHistory
		}
	}
	

	
	
}

