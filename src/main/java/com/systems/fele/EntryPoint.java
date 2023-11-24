package com.systems.fele;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.systems.fele.support.CmdArgs;
import com.systems.fele.syntax.Parser;
import com.systems.fele.syntax.Program;

public class EntryPoint {
	
	public static void main(String[] args) {
		
		CmdArgs cmdArgs = new CmdArgs()
				.define(arg -> arg
						.names("--")
						.asPositional()
						.setList())
				.define(arg -> arg
						.names("-h", "--help")
						.asSwitch())
				.define(arg -> arg
						.names("-v", "--version")
						.asSwitch())
				.define(arg -> arg
						.names("-e", "--expression=")
						.asString())
				.define(arg -> arg
						.names("-i", "--input=")
						.asString());
		
		//cmdArgs.parse(new String[]{ "--expression=19+10*5;", "-v" });
		cmdArgs.parse(new String[]{ "--input=src/main/resources/main.fl" });
		
		Program program = null;
		if (cmdArgs.arg("-e").count() > 0) {
			var expression = cmdArgs.arg("-e").getString(0);
			
			var parser = new Parser(expression);
			program = parser.parseExpression();
		} else if (cmdArgs.arg("-i").count() > 0) {
			program = new Program();
			
			
			
			for (int i = 0; i < cmdArgs.arg("-i").count(); i++) {			
				var filename = cmdArgs.arg("-i").getString(0);
				
				var file = new File(System.getProperty("user.dir"), filename);
				var buffer = new char[256];
				var sb = new StringBuilder();
				try (var reader = new FileReader(file)){
					int len;
					while( (len = reader.read(buffer)) > 0) {
						sb.append(new String(buffer, 0, len));
					}
				} catch (FileNotFoundException e) {
					System.err.println("Could not find file " + file);
					System.exit(1);
				} catch (IOException e) {
					System.err.println("Something happed: " + e.getLocalizedMessage());
					System.exit(1);
				}
				
				var parser = new Parser(sb.toString());
				
				parser.parseSource(program);
			}
		} else {
			System.err.println("Please pass at least one of -f or -e");
			System.exit(1);
			return;
		}
		
		try {
			program.getMainFunction()
				.execute();
				//.executeDebug(System.out);
			
			System.out.println("program exited: " + program.getMachine().registerStack.pop().asInt());
			
			program.getMachine().printStackFrame(System.out);
		} finally { 
			System.out.println(program.getMachine().registerStack.dump());
			
		}
		
		System.exit(0);
	}
	
}
