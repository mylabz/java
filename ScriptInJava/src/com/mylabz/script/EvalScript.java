package com.mylabz.script;

import javax.script.*;

public class EvalScript {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");

		// create a Java object
		String name = "Tom";

		// create the binding
		engine.put("greetingname", name);

		// evaluate JavaScript code from String
		engine.eval("println('Hello, ' + greetingname)");
		engine.eval("println('The name length is ' +  greetingname.length)");
		engine.eval("var date = '03/27/2012';");
		Object date = engine.get("date");
		System.out.println("Date = "+ date);
	}
}
