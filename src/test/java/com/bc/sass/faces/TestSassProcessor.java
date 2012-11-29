package com.bc.sass.faces;

import org.testng.annotations.Test;

import com.bc.sass.SassProcessor;
import com.bc.sass.Syntax;

import static org.testng.Assert.*;

public class TestSassProcessor {

	@Test
	public void testProcess() {
		SassProcessor processor = new SassProcessor();
		processor.setSyntax(Syntax.SCSS);
		String result = processor.process("div {\n  color: black;\n}\n");
		assertEquals(result, "div{color:black}\n");
	}

}
