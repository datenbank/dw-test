package datenbank.engine

import datenbank.model.Variables;
import datenbank.model.Summary
import datenbank.model.TestCase

import groovy.sql.Sql
import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class Executor {

	

	def int run(file) {
		def dir = new File("${Variables.path}Target")
		def error = 3
		def description = ""
		def fileName = file.getName()
		def sourceFileName = "${Variables.path}Source/"+ fileName
		
		def sourceFile = new File(sourceFileName)
		if(fileName.endsWith(".sql")) {			
			def bat = new File("${Variables.path}Target/"+fileName.replace(".sql", "_Before.bat"))
			if(bat.exists() && bat.text.length() > 0)	{
				println "$bat".execute().text
			}
			
			def result = new File("${Variables.path}Target/Result/"+fileName.replace(".sql", ".csv"))	
			result.write("")
			try {
				def sql = Sql.newInstance( Variables.targetConnection, Variables.targetDriver )
							
				sql.eachRow(file.text){ row ->
					(0..row.getMetaData().columnCount-1).each {
						def attr = row[it]
						
						if("${attr}".contains(";")) {
							attr = "\"${attr}\""
						}
						
						if(it == row.getMetaData().columnCount-1)
							result << "${attr}\r\n"
						else 
							result << "${attr};"
					}
					
				} 
			} catch (Exception e) {
				result << "Error...\r\n" 
				description = "$e"
				error = 1
			}
			
			def batAfter = new File("${Variables.path}Target/"+fileName.replace(".sql", "_After.bat"))
			if(batAfter.exists() && batAfter.text.length() > 0)	{
				println "$batAfter".execute().text

			}
			def resultSource = new File("${Variables.path}Source/Result/"+fileName.replace(".sql", ".csv"))	
			try {
				if(sourceFile.exists()){
					resultSource.write("")	
					def sqlSource = Sql.newInstance( Variables.sourceConnection, Variables.sourceDriver )
										
					sqlSource.eachRow(sourceFile.text){ row ->
						(0..row.getMetaData().columnCount-1).each {
							def attrSource = row[it]
						
						if("${attrSource}".contains(";")) {
							attrSource = "\"${attrSource}\""
						}
						
						if(it == row.getMetaData().columnCount-1)
							resultSource <<  "${attrSource}\r\n"
						else 
							resultSource << "${attrSource};"
						}
						
					}
					
				}
			} catch (Exception e) { 
				resultSource << "Error...\r\n"
				error = 1
				description += "$e"
			}
		}
		return error
			
			
	}
	
	def runOne(testCase) {
		
		def file = new File("${Variables.path}Target/${testCase.name}.sql")
		if(file.exists()) {
			log.info("$testCase.name")
			testCase.begin()

			testCase.errors = run(file)
			testCase.resultFlag = null
			testCase.type = 1
			testCase.stop()
			testCase.ready()
			
			
		} else {
			testCase.errors = 1
			testCase.ready()
		}

	}	
	
	def runAll(def summary) {

		summary.testCases.each { testCase ->
			runOne(testCase)
		}
		summary.ready()

	}

}
