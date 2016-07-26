package datenbank.view;

import datenbank.model.TestCase
import datenbank.model.Summary

import java.util.Observer

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
public class ConsolePrinter implements Observer {
	
	def message(String s) {
		println "$s"
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		log.info("update gui $arg0")
		if(arg0 instanceof TestCase && arg0.compared > 0) {		
			print "\rCompare ${arg0.compared} of  ${arg0.total} (skipped: $arg0.skipped), elapsed: $arg0.elapsed seconds"
		} else if(arg0 instanceof TestCase && arg0.i > 0) {		
			print "\rExecuting ${arg0.i} of  ${arg0.total}, elapsed: $arg0.elapsed seconds"
		} else if(arg0 instanceof Summary) {		
			println ""
			arg0.testCases.each {
				
				if(it.errors == 0)
					println "$it.file\t->\tSUCCESSS"	
			}
			arg0.testCases.each {

				if(it.errors == 1)
					println "$it.file\t->\tFAILURE\t[$it.resultFlag,$it.linesNotInTarget,$it.linesNotInSource]"
			}
			
		} else {
			log.debug("missing action to update ui")
			
		}
		
	}

}
