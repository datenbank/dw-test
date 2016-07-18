package datenbank.engine

import datenbank.model.Variables
import datenbank.model.Summary
import datenbank.model.TestResult
import datenbank.view.ConsolePrinter

import groovy.util.logging.Log4j
import org.apache.log4j.Logger


@Log4j
class ResultTester {
	
	static run(def file) {
				
		def skipped = 0
		def compared = 0		
		def errors = 0	
		def linesNotInSource = 0
		def linesNotInTarget = 0
		def resultFlag = 0
				
		def fileName = file.getName()
		if(fileName.endsWith(".csv")) {	
					
			def errorFlag = 0
					
			if(file.text == "Error...\r\n") {
				errorFlag += 2
			}
							
			def result = file.text.split("\n")
			def sourceFileName = "${Variables.path}Source/Result/"+ fileName			
			def sourceFile = new File(sourceFileName)
			if(sourceFile.exists()) {
				
				if(sourceFile.text == "Error...\r\n") {
					errorFlag += 1
				}	
				def sourceResult = sourceFile.text.split("\n")
				
				def outputFile = new File("${Variables.path}Report/"+fileName) 
				
				def notInSource = (result - sourceResult)
				notInSource.each {
					outputFile << "Not in Source;${runDate.format('YYYY-MM-dd')};${runDate.format('HH')};${runDate.format('mm')};"+it+"\n"
					linesNotInSource += 1 
				}
				def notInTarget = (sourceResult - result)
				
				notInTarget.each {
					outputFile << "Not in Target;${runDate.format('YYYY-MM-dd')};${runDate.format('HH')};${runDate.format('mm')};"+it+"\n"
					linesNotInTarget += 1 
				}	
				if(linesNotInSource > 0) 
					resultFlag +=1
					
				if(linesNotInTarget > 0) 
					resultFlag +=2
				
				
				if(resultFlag > 0)
					errors++
				compared++
			} else { //skip
				skipped++					
			}
			
		}
			
		return [compared, skipped, errors, resultFlag, linesNotInSource, linesNotInTarget]

	}
	
	static runOne(def test) {
		
		ConsolePrinter cp = new ConsolePrinter()
		def summary = new Summary()
		summary.addObserver(cp)
		
		def file = new File("${Variables.path}Target/Result/${test}.csv")
		if(file.exists()) {
			TestResult tr = new TestResult(file: file.getName())
			tr.addObserver(cp)
			tr.begin()							
			def result = run(file)
			tr.total += result[0]
			tr.compared += result[0]
			tr.skipped += result[1]
			tr.errors += result[2]
			tr.resultFlag += result[3]
			tr.linesNotInSource += result[4]
			tr.linesNotInTarget += result[5]
			tr.stop()
			tr.ready()
			summary.testResults << tr
			
		} 
		summary.ready()
	
	}
	
	static runAll() {
		def dir = new File("${Variables.path}Target/Result")
		
		ConsolePrinter cp = new ConsolePrinter()
		def summary = new Summary()	
		summary.addObserver(cp)
		
		def total = 0	
		dir.eachFile() { file ->
			if(file.getName().endsWith(".csv"))
				total++
		}
		def compared = 0
		def skipped = 0	
		def errors = 0
		dir.eachFile() { file ->
			if(file.getName().endsWith(".csv")) {
				TestResult tr = new TestResult(file: file.getName())
				tr.begin()
				tr.addObserver(cp)
				tr.begin()							
				def result = run(file)
				tr.total += total
				compared += result[0]
				skipped += result[1]
				errors += result[2]
				
				tr.compared = compared
				tr.skipped = skipped
				tr.errors = errors
				
				tr.resultFlag = result[3]
				tr.linesNotInSource = result[4]
				tr.linesNotInTarget = result[5]
				tr.stop()
				tr.ready()
				summary.testResults << tr
			}
			
		}
		summary.ready()

	}
}
