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

package com.github.fauu.helix.editor.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.github.fauu.helix.editor.datum.GeometryNameEntry;

import java.util.LinkedList;
import java.util.List;

public class EditorGeometryManager {

  private static final String DIRECTORY_NAME = "tile-geometry";
  
  private static final String EXTENSION = "obj";

  public List<GeometryNameEntry> getAllNameEntries() {
    List<GeometryNameEntry> names = new LinkedList<>();
    
    FileHandle directory = Gdx.files.internal("./bin/" + DIRECTORY_NAME);
    
    for (FileHandle entry : directory.list()) {
      if (entry.extension().equalsIgnoreCase(EXTENSION)) {
        GeometryNameEntry newEntry = new GeometryNameEntry();
        newEntry.setName(entry.nameWithoutExtension());

        names.add(newEntry);
      }
    }
    
    return names;
  }

}
