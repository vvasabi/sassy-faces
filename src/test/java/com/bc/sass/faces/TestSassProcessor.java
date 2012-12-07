package com.bc.sass.faces;

import java.io.IOException;
import java.net.URL;

import com.bc.sass.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class TestSassProcessor {

	@Test
	public void testProcess() {
		SassProcessor processor = new SassProcessor();
		processor.setSyntax(Syntax.SCSS);
		String result = processor.process("div {\n  color: black;\n}\n");
		assertEquals(result, "div{color:black}\n");
	}

	@Test
	public void testImport() throws IOException {
		SassImporterFactory.setInstance(new ClassPathSassImporterFactory());
		SassProcessor processor = new SassProcessor();
		processor.setSyntax(Syntax.SCSS);
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		URL url = classLoader.getResource("styles.scss");
		processor.addLoadPath("");
		String result = processor.process(IOUtils.toString(url.openStream()));
		assertEquals(result, ".imported{color:black}\n");
	}

}
