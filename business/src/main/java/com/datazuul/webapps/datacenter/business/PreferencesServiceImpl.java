package com.datazuul.webapps.datacenter.business;

import com.datazuul.webapps.datacenter.backend.api.PreferencesRepository;
import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ralf
 */
public class PreferencesServiceImpl implements PreferencesService {

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Override
    public List<File> getSearchDirectories() {
        return preferencesRepository.getSearchDirectories();
    }
}
