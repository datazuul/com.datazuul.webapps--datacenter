/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datazuul.webapps.portal;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;

/**
 *
 * @author ralf
 */
public class WalkFileTree {

  static int i = 0;

  public static void main(String[] args) throws IOException {
    Path startingDir = Paths.get("/mnt/fileserver/Alle_Musik/CDs");
    Files.walkFileTree(startingDir, new FindMusicVisitor());
  }

  private static class FindMusicVisitor extends SimpleFileVisitor<Path> {

    public FindMusicVisitor() {
    }

    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
      if ((file != null) && (attrs != null)) {
        if (file.getFileName().toString().endsWith(".ogg")
                || file.getFileName().toString().endsWith(".mp3")) {
          i++;
          System.out.println("[" + i + "] " + file.getFileName().toString());
          if (file.getFileName().toString().endsWith(".mp3")) {
            File mp3File = file.toFile();
            try {
              String artist="?", title="?", album="?", year="?";
              MP3File f = (MP3File) AudioFileIO.read(mp3File);
              ID3v24Tag v24tag = f.getID3v2TagAsv24();
              if (v24tag != null) {
                artist = (v24tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST));
                title = (v24tag.getFirst(ID3v24Frames.FRAME_ID_TITLE));
                album = (v24tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM));
                year = (v24tag.getFirst(ID3v24Frames.FRAME_ID_YEAR));
              }
              AbstractID3v2Tag iD3v2Tag = f.getID3v2Tag();
              if (iD3v2Tag != null) {
                artist = (iD3v2Tag.getFirst(FieldKey.ARTIST));
                title = (iD3v2Tag.getFirst(FieldKey.TITLE));
                album = (iD3v2Tag.getFirst(FieldKey.ALBUM));
                year = (iD3v2Tag.getFirst(FieldKey.YEAR));
              }
              System.out.println("##### " + artist + " - " + title + " - " + album + " - " + year);
            } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex) {
              Logger.getLogger(WalkFileTree.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
      }
      return FileVisitResult.CONTINUE;
    }
  }
}
