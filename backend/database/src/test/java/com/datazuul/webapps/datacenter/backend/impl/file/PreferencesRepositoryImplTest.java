package com.datazuul.webapps.datacenter.backend.impl.file;

import com.datazuul.webapps.datacenter.backend.impl.file.PreferencesRepositoryImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author ralf
 */
@Ignore
public class PreferencesRepositoryImplTest {

  /**
   * Test of getSearchDirectories method, of class PreferencesRepositoryImpl.
   */
  @Test
  public void testGetSearchDirectories() {
    PreferencesRepositoryImpl instance = new PreferencesRepositoryImpl();
    instance.PREFERENCES_FILE_PATH = "TEST/.datazuul/datacenter/music";
    List<File> result = instance.getSearchDirectories();
    assertEquals(0, result.size());
  }

  @Test
  public void testSetSearchDirectories() {
    PreferencesRepositoryImpl instance = new PreferencesRepositoryImpl();
    instance.PREFERENCES_FILE_PATH = "TEST/.datazuul/datacenter/music";
    List<File> files = new ArrayList<>();
    File file1 = new File("/test/dir1");
    files.add(file1);
    File file2 = new File("/.dir2");
    files.add(file2);
    instance.setSearchDirectories(files);
  }
}
