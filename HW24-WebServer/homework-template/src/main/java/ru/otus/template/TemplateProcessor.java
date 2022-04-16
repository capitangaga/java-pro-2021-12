package ru.otus.template;

import java.io.IOException;
import java.util.Map;

/**
 * @author kirillgolovko
 */
public interface TemplateProcessor {
    String getPage(String filename, Map<String, Object> data) throws IOException;
}
