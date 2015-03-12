/*
 * Copyright (C) 2014, 2015 Helix Engine Developers 
 * (http://github.com/fauu/HelixEngine)
 *
 * This software is licensed under the GNU General Public License
 * (version 3 or later). See the COPYING file in this distribution.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this software. If not, see <http://www.gnu.org/licenses/>.
 *
 * Authored by: Piotr Grabowski <fau999@gmail.com>
 */

package com.github.fauu.helix.editor.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class FileExtensionFilter implements FilenameFilter, FileFilter {
  
  private String extension;
  
  private boolean exclude;
  
  public FileExtensionFilter(String extension, boolean exclude) {
    this(extension);
    this.exclude = exclude;
  }
  
  public FileExtensionFilter(String extension) {
    this.extension = extension;
  }

  @Override
  public boolean accept(File file) {
    // TODO: First argument
    return accept(null, file.getName());
  }

  @Override
  public boolean accept(File directory, String filename) {
    boolean match = filename.endsWith("." + extension);

    return exclude ? !match : match;
  }

}
