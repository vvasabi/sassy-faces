package com.bc.sass.filter;

import com.bc.sass.SassConfig;
import com.bc.sass.Syntax;

/**
 * @author vvasabi
 */
public interface SassFilter {

	String process(String input, Syntax syntax, SassConfig config,
				   SassFilterChain filterChain);

}
