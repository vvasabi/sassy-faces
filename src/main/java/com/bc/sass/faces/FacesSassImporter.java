package com.bc.sass.faces;

import com.bc.sass.AbstractSassImporter;
import com.bc.sass.SassConfig;
import org.apache.commons.io.IOUtils;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Import Ruby
 *
 * @author vvasabi
 */
public class FacesSassImporter extends AbstractSassImporter {

	static final String RESOURCE_PATH_PREFIX = "/resources/";

	private static final Pattern EL_VAR_PATTERN
			= Pattern.compile("#\\{[^$][^}]*\\}");

	public FacesSassImporter(SassConfig config) {
		super(config);
	}

	@Override
	protected String loadSassScriptContent(String relativePath) {
		try {
			String resourcePath = RESOURCE_PATH_PREFIX + relativePath;
			ExternalContext externalContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			InputStream is = externalContext.getResourceAsStream(resourcePath);
			if (is == null) {
				return null;
			}
			try {
				return process(IOUtils.toString(is));
			} finally {
				IOUtils.closeQuietly(is);
			}
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public String process(String input) {
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext elContext = context.getELContext();
		ExpressionFactory expressionFactory = context.getApplication().
				getExpressionFactory();
		StringBuffer sb = new StringBuffer();
		Matcher matcher = EL_VAR_PATTERN.matcher(input);
		while (matcher.find()) {
			String replacement = matcher.group();
			ValueExpression expression = expressionFactory
					.createValueExpression(elContext, replacement,
							String.class);
			String value = (String) expression.getValue(elContext);
			matcher.appendReplacement(sb, value);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

}
