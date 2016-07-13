package datenbank
import groovy.util.CliBuilder 
import groovy.util.logging.* 

import org.apache.log4j.*

import datenbank.engine.Executor;
import datenbank.engine.ResultTester;
import datenbank.model.Model;
import datenbank.model.Variables;

import datenbank.view.*
import datenbank.model.*

@Log4j
class Test {
	
	public static void main(String[] args) {
		Variables.load()

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
		
		cli.i(longOpt: 'info', 'Provide information level logging detail', required: false)
		cli.d(longOpt: 'debug', 'Provide debugging level logging detail', required: false)
		OptionAccessor opt = cli.parse(args)  
		def i = 0 
		
		BasicConfigurator.configure();
		Logger.getLogger("datenbank").setLevel(Level.INFO);		
		
		if(opt.i) {
			Logger.getLogger("datenbank").setLevel(Level.INFO);
			i++
		}
		
		if(opt.d) {
			Logger.getLogger("datenbank").setLevel(Level.DEBUG);
			i++
		}

		if( opt.l ) {
			def dir = new File("${Variables.path}Target")
			dir.eachFile() { file ->
				if(file.getName().endsWith(".sql"))
					println file.getName().substring(0,file.getName().length()-4)
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
             Executor.runAll()
			 ResultTester.runAll()
			 i++
        }  
        if( opt.ea ) {  
            Executor.runAll()
			i++			
		} 
		if( opt.ta ) {  
            ResultTester.runAll()
			i++
		} 
		if( opt.e ) {  
            Executor.runOne(opt.e)
			i++
		} 
		if( opt.t ) {  
            ResultTester.runOne(opt.t)
			i++
		}
		if(opt.h || i==0) {  
            cli.usage()  
        } 

	}
	
	
}
