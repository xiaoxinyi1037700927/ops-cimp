package com.sinosoft.ops.cimp.export.word;

import com.aspose.words.Document;

public abstract class ProcessWord {

	public abstract Document execute(String templatePath) throws Exception;

}
