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
		println arg0
		
	}

}
