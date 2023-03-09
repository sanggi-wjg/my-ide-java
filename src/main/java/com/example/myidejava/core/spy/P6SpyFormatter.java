package com.example.myidejava.core.spy;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Stack;

@Configuration
public class P6SpyFormatter implements MessageFormattingStrategy {

    @Value("${logging.level.p6spy_callstack}")
    private boolean p6spyCallstack;

    @PostConstruct
    public void setMessageFormatter() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        if (sql == null || sql.trim().isEmpty()) {
            return "";
        }
        return p6spyCallstack ? sql + createStack(connectionId, elapsed) : sql;
    }

    private String formatSql(String category, String sql) {
        if (sql == null || sql.trim().equals("")) return sql;

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.getName().equals(category)) {
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);

            if (tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            } else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
        }
        return sql;
    }

    private String createStack(int connectionId, long elapsed) {
        Stack<String> callStack = new Stack<>();
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();

        for (StackTraceElement stackTraceElement : stackTrace) {
            String trace = stackTraceElement.toString();

            if (trace.startsWith("com.example.myidejava") && !trace.startsWith("com.example.myidejava.core.spy.P6SpyFormatter")) {
                callStack.push(trace);
            }
        }

        StringBuilder sb = new StringBuilder();
        int order = 1;
        while (!callStack.isEmpty()) {
            sb.append("\n\t\t").append(order++).append(". ").append(callStack.pop());
        }

        return new StringBuilder().append("\n\tConnection ID:").append(connectionId)
                .append(" | Execution Time: ").append(elapsed).append(" ms")
                .append("\tCall Stack: ").append(sb).append("\n").toString();
    }

}
