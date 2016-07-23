package datenbank.engine

import datenbank.model.Variables;
import datenbank.model.Summary
import datenbank.model.QueryResult
import datenbank.view.ConsolePrinter

import java.util.Observer

import groovy.sql.Sql
import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
class Executor {
	
	Observer ui;
	

	def int run(def file) {
		def dir = new File("${Variables.path}Target")
		def error = 0
		def fileName = file.getName()
		def sourceFileName = "${Variables.path}Source/"+ fileName
		
		def sourceFile = new File(sourceFileName)
		if(fileName.endsWith(".sql")) {			
			def bat = new File("${Variables.path}Target/"+fileName.replace(".sql", "_Before.bat"))
			if(bat.exists())	{
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
				error = 1
			}
			
			def batAfter = new File("${Variables.path}Target/"+fileName.replace(".sql", "_After.bat"))
			if(batAfter.exists())	{
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
			}
		}
		return error
			
			
	}
	
	def runOne(def test) {
		
		def summary = new Summary()
		summary.addObserver(ui)
		def file = new File("${Variables.path}Target/${test}.sql")
		if(file.exists()) {
			
			def qr = new QueryResult(i: 1, total: 1, file: file.getName())
			qr.addObserver(ui)
			qr.begin()
			
			def errors = 0 
			errors = run(file)
			qr.errors = errors	
			qr.ready()
			qr.stop()
			summary.queryResults << qr
			
		} else {
			cp.message "No test to execute named: ${test}"
		}
		summary.ready()
	}	
	
	def runAll() {

		
		def summary = new Summary()
		summary.addObserver(ui)
		def dir = new File("${Variables.path}Target")
		
		def total = 0
		dir.eachFile() { file ->
			if(file.getName().endsWith(".sql"))
				total++
		}
		def i = 0
		def sysTime = System.currentTimeMillis()
		def errors = 0 
		dir.eachFile() { file ->
			if(file.getName().endsWith(".sql")) {
				i++
				
				def qr = new QueryResult(i: i, total: total, file: file.getName())
				qr.addObserver(ui)
				
				qr.begin()				
				errors += run(file)
				qr.errors = errors
				
				qr.stop()
				qr.ready()
				summary.queryResults << qr
			}
			
		}
		
		summary.ready()
		
			
	}

}
