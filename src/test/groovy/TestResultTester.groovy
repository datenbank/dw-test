import java.util.Observer;

import static org.junit.Assert.*

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer

import datenbank.engine.ResultTester
import datenbank.engine.Init

class TestResultTester implements Observer {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		def src = new File('./Source/Result/junit.csv')
		def tgt = new File('./Target/Result/junit.csv')
		
		src << "1;2\r\n"
		src << "3;4\r\n"
		
		tgt << "1;2\r\n"
		tgt << "3;4\r\n"
		
		
		
		src = new File('./Source/Result/junit2.csv')
		tgt = new File('./Target/Result/junit2.csv')
		
		src << "2;2\r\n"
		src << "3;4\r\n"
		src << "4;5\r\n"
		
		tgt << "1;2\r\n"
		tgt << "3;4\r\n"
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		new File('./Source/Result/junit.csv').delete()
		new File('./Target/Result/junit.csv').delete()
		
		new File('./Source/Result/junit2.csv').delete()
		new File('./Target/Result/junit2.csv').delete()
		
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	
	@Test
	public void test() {
		
		def init = new Init(ui: this)
		def summary = init.init()
		new ResultTester().runAll(summary)
		
		summary.testCases.each {
			if(it.name == "junit2") {
				assert it.errors == 1
				assert it.linesNotInSource == 1	
				assert it.linesNotInTarget == 2
			}
			else
				assert it.errors == 0 
			
		}
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		
	}

}
