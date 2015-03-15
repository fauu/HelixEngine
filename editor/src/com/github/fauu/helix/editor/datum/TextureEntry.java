package com.github.fauu.helix.editor.datum;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class TextureEntry {
  
  private String name;
  
  private Drawable preview;
  
  private boolean selected;
  
  public TextureEntry() { }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Drawable getPreview() {
    return preview;
  }

  public void setPreview(Drawable preview) {
    this.preview = preview;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

}
