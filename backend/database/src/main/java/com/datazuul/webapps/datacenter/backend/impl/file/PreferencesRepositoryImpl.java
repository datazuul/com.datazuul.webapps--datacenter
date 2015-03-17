package com.datazuul.webapps.datacenter.backend.impl.file;

import com.datazuul.webapps.datacenter.backend.api.PreferencesRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author ralf
 */
@Repository
public class PreferencesRepositoryImpl implements PreferencesRepository {

    protected String PREFERENCES_FILE_PATH = "datazuul/datacenter/music";

    @Override
    public List<File> getSearchDirectories() {
        List<File> result = new ArrayList<>();
        Preferences userRootNode = Preferences.userRoot();
        Preferences prefs = userRootNode.node(PREFERENCES_FILE_PATH);
        String searchDirectoriesConcatenated = prefs.get("searchDirectories", null);
        if (searchDirectoriesConcatenated != null) {
            String[] searchDirectoriesPaths = searchDirectoriesConcatenated.split(",");
            for (String searchDirectoryPath : searchDirectoriesPaths) {
                File searchDirectory = new File(searchDirectoryPath);
                result.add(searchDirectory);
            }
        }
        return result;
    }

    @Override
    public void setSearchDirectories(List<File> searchDirectories) {
        List<String> searchDirectoryPaths = new ArrayList<>();
        for (File searchDirectory : searchDirectories) {
            searchDirectoryPaths.add(searchDirectory.getAbsolutePath());
        }
        String searchDirectoriesConcatenated = StringUtils.collectionToCommaDelimitedString(searchDirectoryPaths);
        Preferences userRootNode = Preferences.userRoot();
        Preferences prefs = userRootNode.node(PREFERENCES_FILE_PATH);
        prefs.put("searchDirectories", searchDirectoriesConcatenated);
        try {
            prefs.flush();
        } catch (BackingStoreException ex) {
            // nothing to do...?
        }
    }
}
