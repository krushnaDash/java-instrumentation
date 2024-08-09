package org.krushna.java.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class AppTransformer implements ClassFileTransformer {
	String targetClassName;
	ClassLoader targetClassLoader;

	public AppTransformer(String targetClassName, ClassLoader targetClassLoader) {
		super();
		this.targetClassName = targetClassName;
		this.targetClassLoader = targetClassLoader;
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) {
		byte[] byteCode = classfileBuffer;
		String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/");

		if (!className.equals(finalTargetClassName)) {
			return byteCode;
		}

		if (className.equals(finalTargetClassName) && loader.equals(targetClassLoader)) {

			System.out.println("[Agent] Transforming class " +targetClassName);
			try {
				ClassPool cp = ClassPool.getDefault();
				CtClass cc = cp.get(targetClassName);
				CtMethod m = cc.getDeclaredMethod("calculatePi");
				m.addLocalVariable("startTime", CtClass.longType);
				m.insertBefore("startTime = System.currentTimeMillis();");

				StringBuilder endBlock = new StringBuilder();

				m.addLocalVariable("endTime", CtClass.longType);
				m.addLocalVariable("opTime", CtClass.longType);
				endBlock.append("endTime = System.currentTimeMillis();");
				endBlock.append("opTime = (endTime-startTime)/1000;");

				endBlock.append("System.out.println(\"[Application] Withdrawal operation completed in:"
						+ "\" + opTime + \" seconds!\");");

				m.insertAfter(endBlock.toString());

				byteCode = cc.toBytecode();
				cc.detach();
			} catch (NotFoundException | CannotCompileException | IOException e) {
				e.printStackTrace();
			}
		}
		return byteCode;
	}
}