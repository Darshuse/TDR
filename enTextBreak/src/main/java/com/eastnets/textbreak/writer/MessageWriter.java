
package com.eastnets.textbreak.writer;

import java.util.List;

import com.eastnets.textbreak.entities.SystemTextField;
import com.eastnets.textbreak.entities.TextField;
import com.eastnets.textbreak.entities.TextFieldLoop;

public interface MessageWriter {
	public void persistFinMessages(List<TextField> textFieldList, List<TextFieldLoop> textFieldLoopList);

	public void persistSystemMessages(List<SystemTextField> sysTextFieldList);

}
