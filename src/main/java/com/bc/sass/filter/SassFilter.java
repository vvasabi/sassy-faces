package com.bc.sass.filter;

import com.bc.sass.SassConfig;
import com.bc.sass.SassScript;

/**
 * @author vvasabi
 */
public interface SassFilter {

	void process(SassScript script, SassConfig config,
				 SassFilterChain filterChain);

}
