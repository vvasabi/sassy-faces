package com.bc.sass;

import java.util.Map;

/**
 * @author vvasabi
 */
public interface SassImporter {

	SassFile findRelative(String uri, String base, Map<String, Object> options);

	SassFile find(String uri, Map<String, Object> options);

	String importSassFile(String uri);

}
