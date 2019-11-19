package guru.nidi.graphviz.engine;

import javax.script.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static guru.nidi.graphviz.engine.JdkJavascriptEngine.ENGINE;

class NashornJavascriptEngine extends AbstractJavascriptEngine {
    private static final Pattern JAVA_18_PATTERN = Pattern.compile("1.8.0_(\\d+).*");
    private final ScriptContext context = new SimpleScriptContext();
    private final ResultHandler resultHandler = new ResultHandler();

    NashornJavascriptEngine() {
        final String version = System.getProperty("java.version");
        final Matcher matcher = JAVA_18_PATTERN.matcher(version);
        if (matcher.matches() && Integer.parseInt(matcher.group(1)) < 40) {
            throw new GraphvizException("You are using an old version of java 1.8. Please update it.");
        }
        context.getBindings(ScriptContext.ENGINE_SCOPE).put("handler", resultHandler);
        eval("function result(r){ handler.setResult(r); }" + "function error(r){ handler.setError(r); }");
    }

    @Override
    protected String execute(String js) {
        eval(js);
        return resultHandler.waitFor();
    }

    private void eval(String js) {
        try {
            ENGINE.eval(js, context);
        } catch (ScriptException e) {
            throw new GraphvizException("Problem executing javascript", e);
        }
    }
}
