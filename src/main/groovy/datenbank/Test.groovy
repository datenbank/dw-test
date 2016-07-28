package datenbank

import datenbank.engine.Executor
import datenbank.engine.Init
import datenbank.engine.ResultTester
import datenbank.model.Variables
import datenbank.model.Model
import datenbank.view.ConsolePrinter
import datenbank.view.FxPrinter;
import groovy.util.CliBuilder 
import groovy.util.logging.Log4j

import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.log4j.BasicConfigurator

@Log4j
class Test {
	
	public static void main(String[] args) {
		Variables.load()
		
		
		ConsolePrinter cp = new ConsolePrinter()
		def init = new Init(ui: cp)
		def summary = init.init()
		Executor ex = new Executor()
		ResultTester rt = new ResultTester()
		
		def cli = new CliBuilder(usage:'groovy Test')
		cli.h(longOpt: 'help', 'usage information', required: false)  
		cli.a(longOpt: 'runAll', 'Execute and compare all tests', required: false) 
		cli.ea(longOpt: 'execAll', 'Execute all tests', required: false)
		cli.ta(longOpt: 'testAll', 'Compare all tests', required: false)
		cli.e(longOpt: 'exec', 'Execute specific test', required: false, args: 1)
		cli.t(longOpt: 'test', 'Compare specific test', required: false, args: 1)
		cli.ca(longOpt: 'create', 'Run all scripts to generate test cases (.sql files)', required: false)
		cli.c(longOpt: 'create', 'Run all scripts to generate test cases (.sql files)', required: false, args: 1)
		cli.l(longOpt: 'list', 'List all testcases (.sql files)', required: false)
		cli.u(longOpt: 'gui', 'Start the GUI', required: false)
		
		cli.i(longOpt: 'info', 'Provide information level logging detail', required: false)
		cli.d(longOpt: 'debug', 'Provide debugging level logging detail', required: false)
		OptionAccessor opt = cli.parse(args)  
		def i = 0 
		
		BasicConfigurator.configure();
		Logger.getLogger("datenbank").setLevel(Level.ERROR);		
		
		if(opt.i) {
			Logger.getLogger("datenbank").setLevel(Level.INFO);
			i++
		}
		
		if(opt.d) {
			Logger.getLogger("datenbank").setLevel(Level.DEBUG);
			i++
		}
		
		if(opt.u) {
			new FxPrinter().launch(FxPrinter.class, args)
			i++
		}

		if( opt.l ) {
			summary.testCases.each { testCase ->
				println testCase.name
				
			}
			i++
		}
		
		if( opt.ca || opt.c) {
			def m = new Model()
			m.loadModelFromFile()
			Binding binding = new Binding();
			binding.setVariable("model", m);
			binding.setVariable("path", Variables.path);
			GroovyShell shell = new GroovyShell(binding);
			
			if( opt.ca ) {
				def dir = new File("${Variables.path}Scripts")
				dir.eachFile() { file ->
					Object value = shell.evaluate(file.text);
				}

				i++
			}	
			
			if( opt.c ) {
				def file = new File("${Variables.path}Scripts/"+opt.c)
				
				if(file.exists()) {
					Object value = shell.evaluate(file.text);
				} else {
					println "Couldn't find the script file: $file"
				}

				i++
			}
			
		}
		

		
        if( opt.a ) {  
             ex.runAll(summary)
			 rt.runAll(summary)
			 i++
        }  
        if( opt.ea ) {  
            ex.runAll(summary)
			i++			
		} 
		if( opt.ta ) {  
            rt.runAll(summary)
			i++
		} 
		
		if( opt.e ) {
			ex.runOne(summary.testCases.find {name == opt.e})
			i++
		}
		
		if( opt.t ) {
			rt.runOne(summary.testCases.find {name == opt.e})
			i++
		}
		
		if(opt.h || i==0) {  
            cli.usage()  
        } 

	}
	
	
}
