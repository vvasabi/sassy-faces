package com.bc.sass.filter;

import com.bc.sass.SassConfig;
import com.bc.sass.SassScript;

import java.util.Stack;

/**
 * @author vvasabi
 */
public final class SassFilterChain {

	private final Stack<SassFilter> filters = new Stack<SassFilter>();

	public void addFilter(SassFilter filter) {
		filters.push(filter);
	}

	public String process(SassScript script, SassConfig config) {
		if (filters.empty()) {
			return script.getContent();
		}
		return filters.pop().process(script, config, this);
	}

}
