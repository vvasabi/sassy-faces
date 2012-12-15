package com.bc.sass.filter;

import com.bc.sass.SassConfig;
import com.bc.sass.Syntax;

import java.util.Stack;

/**
 * @author vvasabi
 */
public class SassFilterChain {

	private Stack<SassFilter> filters = new Stack<SassFilter>();

	public void addFilter(SassFilter filter) {
		filters.push(filter);
	}

	public String process(String input, Syntax syntax, SassConfig config) {
		if (filters.empty()) {
			return input;
		}
		return filters.pop().process(input, syntax,  config, this);
	}

}
