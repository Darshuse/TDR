package com.eastnets.extraction.config;

public class Logging {
	com.eastnets.extraction.config.File file;
	Pattern pattern;
	Level level;

	public com.eastnets.extraction.config.File getFile() {
		return file;
	}

	public void setFile(com.eastnets.extraction.config.File file) {
		this.file = file;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

}
