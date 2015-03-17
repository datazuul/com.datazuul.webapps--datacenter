package com.datazuul.webapps.datacenter.backend.api;

import java.io.File;
import java.util.List;

/**
 * @author ralf
 */
public interface PreferencesRepository {

  public List<File> getSearchDirectories();

  public void setSearchDirectories(List<File> searchDirectories);
}
