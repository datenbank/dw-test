package datenbank.view;
import datenbank.model.*
import java.util.Observer;

public class ConsolePrinter implements Observer {
	
	def message(String s) {
		println "$s"
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		if(arg0 instanceof TestResult) {		
			print "\rCompare ${arg0.compared} of  ${arg0.total} (skipped: $arg0.skipped), elapsed: $arg0.elapsed seconds"
		} else if(arg0 instanceof QueryResult) {		
			print "\reExecuting ${arg0.i} of  ${arg0.total}, elapsed: $arg0.elapsed seconds"
		}
		
	}

}
