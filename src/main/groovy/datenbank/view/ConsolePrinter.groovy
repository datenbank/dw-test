package datenbank.view;

import datenbank.model.TestCase
import datenbank.model.Summary

import java.util.Observer

import groovy.util.logging.Log4j
import org.apache.log4j.Logger

@Log4j
public class ConsolePrinter implements Observer {
	
	def comCount = 1
	def execCount = 1
	
	@Override
	public void update(Observable arg0, Object arg1) {
		log.info("update gui $arg0")
		if(arg0 instanceof TestCase && arg0.tester > 0) {		
			print "\rCompare ${comCount++} elapsed: $arg0.elapsed seconds"
		} else if(arg0 instanceof TestCase && arg0.executor > 0 && arg0.tester == 0) {		
			print "\rExecuting ${execCount++} elapsed: $arg0.elapsed seconds"
		} else if(arg0 instanceof Summary) {		
			println ""
			arg0.testCases.each {
				
				if(it.errors == 0)
					println "$it.name\t->\tSUCCESSS"	
			}
			arg0.testCases.each {

				if(it.errors == 1)
					println "$it.name\t->\tFAILURE"
			}
			
		} else {
			log.debug("missing action to update ui")
			
		}
		
	}

}
