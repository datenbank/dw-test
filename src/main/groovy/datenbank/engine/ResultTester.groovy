package datenbank.engine


import datenbank.model.Variables
import datenbank.model.Summary
import datenbank.model.TestCase


import groovy.util.logging.Log4j
import org.apache.log4j.Logger


@Log4j
class ResultTester {

	def run(def file) {
				
		def skipped = 0
		def compared = 0		
		def errors = 4	
		def linesNotInSource = 0
		def linesNotInTarget = 0
		def resultFlag = -1
		def runDate = Calendar.instance
		def fileName = file.getName()
		if(fileName.endsWith(".csv")) {	
			def result = file.text.split("\n")
			def sourceFileName = "${Variables.path}Source/Result/"+ fileName			
			def sourceFile = new File(sourceFileName)
			if(sourceFile.exists()) {
				resultFlag = 0

				def sourceResult = sourceFile.text.split("\n")
				
				def outputFile = new File("${Variables.path}Report/"+fileName) 
				if(!Variables.saveCompareHistory && outputFile.exists())
					outputFile.write("")
					
				def notInSource = (result - sourceResult)
				notInSource.each {
					outputFile << "Not in Source${Variables.csvSeperator}${runDate.format('YYYY-MM-dd')}${Variables.csvSeperator}${runDate.format('HH')}:${runDate.format('mm')}${Variables.csvSeperator}"+it+"\n"
					linesNotInSource += 1 
				}
				def notInTarget = (sourceResult - result)
				
				notInTarget.each {
					outputFile << "Not in Target${Variables.csvSeperator}${runDate.format('YYYY-MM-dd')}${Variables.csvSeperator}${runDate.format('HH')}:${runDate.format('mm')}${Variables.csvSeperator}"+it+"\n"
					linesNotInTarget += 1 
				}	
				if(linesNotInSource > 0) 
					resultFlag +=1
					
				if(linesNotInTarget > 0) 
					resultFlag +=2
				
				
				if(resultFlag > 0)
					errors = 2
				compared++
			} else { //skip
				skipped++
				errors = 2
			}
			
		}
			
		return [compared, skipped, errors, resultFlag, linesNotInSource, linesNotInTarget]

	}
	
	def runOne(def testCase) {

		
		def file = new File("${Variables.path}Target/Result/${testCase.name}.csv")
		if(file.exists()) {
			log.info("$testCase.name")
			testCase.beginTest()							
			def result = run(file)
			testCase.compared = result[0]
			testCase.skipped = result[1]
			testCase.errors = result[2]
			testCase.resultFlag = result[3]
			testCase.linesNotInSource = result[4]
			testCase.linesNotInTarget = result[5]
			testCase.type = 2
			testCase.stopTest()
			testCase.ready()
			
			
		} else {
			testCase.resultFlag = -1
			testCase.errors = 2
			testCase.ready()
		}

	}
	
	def runAll(summary) {
		
		summary.testCases.each { testCase ->
			
			runOne(testCase)
		}
		
		summary.ready()

	}
}
