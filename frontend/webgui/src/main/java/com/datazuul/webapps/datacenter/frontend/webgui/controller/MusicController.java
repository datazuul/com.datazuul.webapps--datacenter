package com.datazuul.webapps.datacenter.frontend.webgui.controller;

import com.datazuul.webapps.datacenter.business.MusicService;
import com.datazuul.webapps.datacenter.domain.music.MusicTrack;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for Portal.
 *
 * @author ralf
 */
@Controller
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @RequestMapping(value = "import", method = RequestMethod.GET)
    public String getImport(Model model) {
        return "music/import";
    }
    
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String postImport(@RequestParam String directory, Model model) {
        return "music/import";
    }

    @RequestMapping(value = {"/", "index"})
    public String index(Model model) {
        Iterable<MusicTrack> mt = musicService.getAll();
        model.addAttribute("tracks", mt);
        return "music/list";
    }

    @RequestMapping(value = {"/exampleMP3"}, method = RequestMethod.GET)
    public ResponseEntity<byte[]> testMP3() throws IOException {
        File file = new File("/mnt/fileserver/Alle_Musik/CDs/Enthrone Darkness Triumphant/08 - Dimmu Borgir - Master Of Disharmony.mp3");
        InputStream in = new FileInputStream(file);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.valueOf("audio/mp3"));

        return new ResponseEntity<>(IOUtils.toByteArray(in), responseHeaders, HttpStatus.CREATED);
    }
}
