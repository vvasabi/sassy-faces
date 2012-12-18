package com.bc.sass.faces;

import com.bc.sass.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class TestSassProcessor {

	@BeforeTest
	public void setUp() {
		SassImporterFactory.setInstance(new ClassPathSassImporterFactory());
	}

	@Test
	public void testProcessSass() {
		SassProcessor processor = new SassProcessor();
		String result = processor.process("div\n  :color #000", Syntax.SASS)
				.getContent();
		assertEquals(result, "div{color:#000}\n");
	}

	@Test
	public void testImportSass() {
		SassProcessor processor = new SassProcessor();
		String result = processor.processFile("styles.sass").getContent();
		assertEquals(result, ".imported{color:#ff0}\n");
	}

	@Test
	public void testProcessScss() {
		SassProcessor processor = new SassProcessor();
		String result = processor.process("div {\n  color: black;\n}\n",
				Syntax.SCSS).getContent();
		assertEquals(result, "div{color:black}\n");
	}

	@Test
	public void testImportScss() {
		SassProcessor processor = new SassProcessor();
		String result = processor.processFile("styles.scss").getContent();
		assertEquals(result, ".imported{color:black}\n");
	}

	@Test
	public void testMultiImportScss() {
		SassProcessor processor = new SassProcessor();
		String result = processor.process("@import \"imported1\", "
				+ "\"imported2\", \"imported3\";", Syntax.SCSS)
				.getContent();
		assertEquals(result, ".imported{color:black}.imported2{color:red}"
				+ ".imported3{color:green}\n");
	}

	@Test
	public void testImportSassFromScss() {
		SassProcessor processor = new SassProcessor();
		String result = processor.process("@import \"imported-sass\";",
				Syntax.SCSS).getContent();
		assertEquals(result, ".imported{color:#ff0}\n");
	}

	@Test
	public void testImportScssFromSass() {
		SassProcessor processor = new SassProcessor();
		String result = processor.process("@import \"imported1\"", Syntax.SASS)
				.getContent();
		assertEquals(result, ".imported{color:black}\n");
	}

	@Test
	public void testImportMixins() {
		SassProcessor processor = new SassProcessor();
		String result = processor.processFile("import-mixin.scss").getContent();
		assertEquals(result, "div{border-radius:4px;-webkit-border-radius:4px;"
				+ "-ms-border-radius:4px;-moz-border-radius:4px;"
				+ "-o-border-radius:4px}\n");
	}

	@Test
	public void testImportPartial() {
		SassProcessor processor = new SassProcessor();
		String result = processor.processFile("import-partial.scss")
				.getContent();
		assertEquals(result, "body .imported{color:#abcdef}\n");
	}

	@Test
	public void testRecursiveImport() {
		SassProcessor processor = new SassProcessor();
		String result = processor.processFile("recursive-import.scss")
				.getContent();
		assertEquals(result, "body .imported a{color:yellow}\n");
	}

	@Test
	public void testImportCompass() {
		SassProcessor processor = new SassProcessor();
		SassConfig config = new SassConfig();
		config.setCompassEnabled(true);
		processor.setConfig(config);
		String result = processor.processFile("use-compass.scss").getContent();
		assertEquals(result, "a{color:green}\n");
	}

	@Test
	public void testGetFiles() {
		SassProcessor processor = new SassProcessor();
		SassScript script = processor.processFile("recursive-import.scss");
		List<String> files = script.getFiles();
		assertEquals(files.get(0), "recursive-import.scss");
		assertEquals(files.get(1), "recursive-import1.sass");
		assertEquals(files.get(2), "recursive-import2.scss");
	}

}
