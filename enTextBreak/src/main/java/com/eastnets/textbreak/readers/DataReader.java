
package com.eastnets.textbreak.readers;

import java.util.List;

import com.eastnets.textbreak.bean.SourceData;

public abstract class DataReader {

	public abstract List<SourceData> readMessages();

	public abstract List<SourceData> restoreMessages();
}
