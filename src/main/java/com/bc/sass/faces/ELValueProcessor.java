package com.bc.sass.faces;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vvasabi
 */
class ELValueProcessor {

	private static final Pattern EL_VAR_PATTERN
			= Pattern.compile("#\\{[^$][^}]*\\}");

	String process(String input) {
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
