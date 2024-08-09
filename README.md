
    static – makes use of the premain to load the agent using -javaagent option
    dynamic – makes use of the agentmain to load the agent into the JVM using the Java Attach API

    java -javaagent:agent.jar -jar app.jar



![Instrumentation](instrumation.drawio.png?raw=true "Title")