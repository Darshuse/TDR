package com.eastnets.reporting.license.generator.frames;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


/**
 * xml filter is added as file type in save dialog
 * @author EastNets
 * @since January 24, 2013
 */
class XmlFilter extends FileFilter {
  
  String description;
  String extensions[];

  public XmlFilter(String description, String extension) {
    this(description, new String[] { extension });
  }

  public XmlFilter(String description, String extensions[]) {
	  
    if (description == null) {
      this.description = extensions[0];
    } else {
      this.description = description;
    }
    this.extensions = (String[]) extensions.clone();
    toLower(this.extensions);
  }

  private void toLower(String array[]) {
    for (int i = 0, n = array.length; i < n; i++) {
      array[i] = array[i].toLowerCase();
    }
  }

  public String getDescription() {
    return description;
  }

  public boolean accept(File file) {
	  
    if (file.isDirectory()) {
      return true;
    } else {
    	
	  String path = file.getAbsolutePath().toLowerCase();
	  for (int i = 0, n = extensions.length; i < n; i++) {
	    String extension = extensions[i];
	    if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
	      return true;
	    }
	  }//for
	}//else
    
    return false;
  }
  
  //check if the extension of the file = .xml, if not add .xml extension to the file
  public String fixFileExtension(JFileChooser fileChooser) throws Exception {
	  
	  File currentFile = fileChooser.getSelectedFile();
	  String currentFilePath = currentFile.getCanonicalPath();
	  
      if(fileChooser.getFileFilter().getDescription().equalsIgnoreCase(this.description)){
          if(!accept(currentFile)){
        	  currentFilePath = currentFilePath.concat("." + extensions[0]);
          }
      }
      
      return currentFilePath;
  }
  
}