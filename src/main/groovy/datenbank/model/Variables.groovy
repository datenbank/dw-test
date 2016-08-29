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
	
	static testType = ["DISTINCT", "COLUMN_CNT", "HASH", "COUNT", "GROUPBY", "SUMBY", "NONSENSE", "JOIN"]
	
	static degreeOfParallelism = 1
	
	static config
	
	
	def static void save() {
		config.path = path
		config.source = sourceConnection
		config.target = targetConnection
		config.sourceDriver = sourceDriver
		config.targetDriver = targetDriver
		
		config.sqlProgramTarget = sqlProgramTarget
		config.sqlProgramSource = sqlProgramSource
		
		config.csvReader = csvReader
		config.csvSeperator = csvSeperator
		
		config.sqlFetchSize = sqlFetchSize
		config.degreeOfParallelism = degreeOfParallelism
		
		config.testType = testType
		config.model = model
		config.sourceModel = sourceModel
		config.targetModel = targetModel
		
		def file = new File("./conf.txt")
		file.withWriter('UTF-8') { writer ->
			config.writeTo(writer)
		}
		
	}
	
	def static void load()
	{
		path = "./"
		model = "model.csv"
		testType = ["DISTINCT", "COLUMN_CNT", "HASH", "COUNT", "GROUPBY", "SUMBY", "NONSENSE", "JOIN"].sort()
		
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
		
		degreeOfParallelism = 1
		
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
		if(config.testType) {
			testType = config.testType
		}
		
		if(config.degreeOfParallelism) {
			degreeOfParallelism = config.degreeOfParallelism
		}
	}
	

	
	
}

