package com.bc.sass.filter;

import com.bc.sass.SassConfig;
import com.bc.sass.SassScript;
import com.bc.sass.Syntax;

/**
 * @author vvasabi
 */
public interface SassFilter {

	String process(SassScript script, SassConfig config,
				   SassFilterChain filterChain);

}
