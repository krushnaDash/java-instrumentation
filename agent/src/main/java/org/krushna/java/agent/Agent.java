package org.krushna.java.agent;

import java.lang.instrument.Instrumentation;


public class Agent {
	public static void main(String[] args) {
		System.out.println("Hello World!");
	}

	public static void premain(String agentArgs, Instrumentation inst) {

		System.out.println("[Agent] In premain method");
		String className = "org.krushna.java.application.App";
		transformClass(className, inst);
	}

	public static void agentmain(String agentArgs, Instrumentation inst) {
		System.out.println("[Agent] In agentmain method");
		String className = "org.krushna.java.application.App";
		transformClass(className, inst);
	}

	private static void transformClass(String className, Instrumentation instrumentation) {
		Class<?> targetCls = null;
		ClassLoader targetClassLoader = null;
		// see if we can get the class using forName
		try {
			targetCls = Class.forName(className);
			targetClassLoader = targetCls.getClassLoader();
			transform(targetCls, targetClassLoader, instrumentation);
			return;
		} catch (Exception ex) {
			System.err.println("Class [{}] not found with Class.forName");
		}
		
		// otherwise iterate all loaded classes and find what we want
		for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
			if (clazz.getName().equals(className)) {
				targetCls = clazz;
				targetClassLoader = targetCls.getClassLoader();
				transform(targetCls, targetClassLoader, instrumentation);
				return;
			}
		}
		throw new RuntimeException("Failed to find class [" + className + "]");
	}

	private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation) {
		AppTransformer appTransformer = new AppTransformer(clazz.getName(), classLoader);
		instrumentation.addTransformer(appTransformer, true);
		try {
			instrumentation.retransformClasses(clazz);
		} catch (Exception ex) {
			throw new RuntimeException("Transform failed for: [" + clazz.getName() + "]", ex);
		}
	}
}
