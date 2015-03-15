package com.github.fauu.helix.editor.manager;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.github.fauu.helix.editor.datum.GeometryNameEntry;

public class EditorGeometryManager {

  private static final String DIRECTORY_NAME = "tile-geometry";
  
  private static final String EXTENSION = "obj";

  public List<GeometryNameEntry> getAllNameEntries() {
    List<GeometryNameEntry> names = new LinkedList<GeometryNameEntry>();
    
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
