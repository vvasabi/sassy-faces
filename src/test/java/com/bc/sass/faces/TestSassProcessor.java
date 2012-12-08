package com.bc.sass.faces;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.bc.sass.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TestSassProcessor {

	@BeforeTest
	public void setUp() {
		SassImporterFactory.setInstance(new ClassPathSassImporterFactory());
	}

	@Test
	public void testProcess() {
		SassProcessor processor = new SassProcessor();
		processor.setSyntax(Syntax.SCSS);
		String result = processor.process("div {\n  color: black;\n}\n");
		assertEquals(result, "div{color:black}\n");
	}

	@Test
	public void testImport() throws IOException {
		SassProcessor processor = new SassProcessor();
		processor.setSyntax(Syntax.SCSS);
		String result = processor.processFile("styles.scss");
		assertEquals(result, ".imported{color:black}\n");
	}

	@Test
	public void testMultiImport() throws IOException {
		SassProcessor processor = new SassProcessor();
		processor.setSyntax(Syntax.SCSS);
		String result = processor.process("@import \"imported1\", "
				+ "\"imported2\", \"imported3\";");
		assertEquals(result, ".imported{color:black}.imported2{color:red}"
				+ ".imported3{color:green}\n");
	}

}
