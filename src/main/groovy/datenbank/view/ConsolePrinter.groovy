package datenbank.view;

import datenbank.model.TestResult
import datenbank.model.QueryResult
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
		log.info('update gui')
		if(arg0 instanceof TestResult) {		
			print "\rCompare ${arg0.compared} of  ${arg0.total} (skipped: $arg0.skipped), elapsed: $arg0.elapsed seconds"
		} else if(arg0 instanceof QueryResult) {		
			print "\rExecuting ${arg0.i} of  ${arg0.total}, elapsed: $arg0.elapsed seconds"
		} else {
			log.debug("missing action to update ui")
			
		}
		
	}

}
