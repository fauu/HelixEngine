/*
 * Copyright (C) 2014-2016 Helix Engine Developers 
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

public class FileExtensionFilter implements FileFilter {
  
  private String extension;
  
  public FileExtensionFilter(String extension) {
    this.extension = extension;
  }
  
  @Override
  public boolean accept(File file) {
    return file.isDirectory() ||
           file.getName().toLowerCase().endsWith(extension);
  }

}
