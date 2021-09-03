package net.pgfmc.teams.teamscore.debug;


public class Debug {

	public static void out(String s) {
		
		if (DebugCommand.debug) {
			System.out.println("[" + s + "]: at method " + Thread.currentThread().getStackTrace()[2].getMethodName() + " line " + Thread.currentThread().getStackTrace()[2].getLineNumber());
			System.out.println("Method run by " + Thread.currentThread().getStackTrace()[3].getMethodName() + " at line " + Thread.currentThread().getStackTrace()[3].getLineNumber());
		}
	};
	
	public static void out(String s, Object[] objs) {
		
		if (DebugCommand.debug) {
			System.out.println("[" + s + "]: at method " + Thread.currentThread().getStackTrace()[2].getMethodName() + " line " + Thread.currentThread().getStackTrace()[2].getLineNumber());
			System.out.println("	Method run by " + Thread.currentThread().getStackTrace()[3].getMethodName() + " at line " + Thread.currentThread().getStackTrace()[3].getLineNumber());
			
			System.out.println("	Conditions:");
			
			for (Object obj : objs) {
				if (obj == null) {
					System.out.println("		null");
				} else {
					System.out.println("		" + obj.getClass().getName() + ": " + obj.toString());
				}
			}
		}
	};
}