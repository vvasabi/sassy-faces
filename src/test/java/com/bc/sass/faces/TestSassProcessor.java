package com.bc.sass.faces;

import com.bc.sass.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TestSassProcessor {

	@BeforeTest
	public void setUp() {
		SassImporterFactory.setInstance(new ClassPathSassImporterFactory());
	}

	@Test
	public void testProcessSass() {
		SassProcessor processor = new SassProcessor();
		String result = processor.process("div\n  :color #000", Syntax.SASS);
		assertEquals(result, "div{color:#000}\n");
	}

	@Test
	public void testImportSass() {
		SassProcessor processor = new SassProcessor();
		String result = processor.processFile("styles.sass");
		assertEquals(result, ".imported{color:#ff0}\n");
	}

	@Test
	public void testProcessScss() {
		SassProcessor processor = new SassProcessor();
		String result = processor.process("div {\n  color: black;\n}\n",
				Syntax.SCSS);
		assertEquals(result, "div{color:black}\n");
	}

	@Test
	public void testImportScss() {
		SassProcessor processor = new SassProcessor();
		String result = processor.processFile("styles.scss");
		assertEquals(result, ".imported{color:black}\n");
	}

	@Test
	public void testMultiImportScss() {
		SassProcessor processor = new SassProcessor();
		String result = processor.process("@import \"imported1\", "
				+ "\"imported2\", \"imported3\";", Syntax.SCSS);
		assertEquals(result, ".imported{color:black}.imported2{color:red}"
				+ ".imported3{color:green}\n");
	}

	@Test
	public void testImportSassFromScss() {
		SassProcessor processor = new SassProcessor();
		String result = processor.process("@import \"imported-sass\";",
				Syntax.SCSS);
		assertEquals(result, ".imported{color:#ff0}\n");
	}

	@Test(expectedExceptions = SassException.class)
	public void testImportScssFromSass() {
		SassProcessor processor = new SassProcessor();
		processor.process("@import \"imported1\"", Syntax.SASS);
	}

}
