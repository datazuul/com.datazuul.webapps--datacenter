package com.datazuul.webapps.datacenter.business;

import com.datazuul.webapps.datacenter.backend.api.MusicRepository;
import com.datazuul.webapps.datacenter.domain.music.MusicTrack;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author ralf
 */
@Service
@Transactional(readOnly = true) // default transactional behavior
public class MusicServiceImpl implements MusicService {

    @Autowired
    private MusicRepository musicRepository;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MusicServiceImpl.class);
    private static final List<String> tagFieldKeyArrayList = new ArrayList<>();
    private static final Pattern PATTERN_NON_DIGIT = Pattern.compile(".*[^0-9].*");
    private static final Pattern PATTERN_FOUR_DIGITS = Pattern.compile("\\D*(\\d{4})\\D*");
    public static final String[] GENRES = {"Blues", "Classic Rock", "Country", "Dance", "Disco",
        "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B",
        "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal",
        "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion",
        "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel",
        "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop",
        "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic",
        "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40",
        "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave",
        "Psychedelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz",
        "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock",
        "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass",
        "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock",
        "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech",
        "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Brass", "Primus",
        "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad",
        "Power Ballad", "Rhytmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "Acapella",
        "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror",
        "Indie", "BritPop", "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta", "Heavy Metal",
        "Black Metal", "Crossover", "Contemporary C", "Christian Rock", "Merengue", "Salsa",
        "Thrash Metal", "Anime", "JPop", "SynthPop", "Variety", "News", "Talk", "Student Station",
        "Sport", "Generalist", "Urban", "Education", "Humor", "8-bits", "Eclectic"};

    static {
        try {
            // Disable Jaudiotagger logs
            LogManager.getLogManager().readConfiguration(new ByteArrayInputStream("org.jaudiotagger.level = OFF".getBytes()));

            // get supported tags
            FieldKey[] tagFieldKeys = FieldKey.values();
            for (FieldKey tfk : tagFieldKeys) {
                if (!tfk.equals(FieldKey.DISC_NO) && !tfk.equals(FieldKey.ALBUM)
                        && !tfk.equals(FieldKey.ALBUM_ARTIST) && !tfk.equals(FieldKey.ARTIST)
                        && !tfk.equals(FieldKey.GENRE) && !tfk.equals(FieldKey.TITLE)
                        && !tfk.equals(FieldKey.TRACK) && !tfk.equals(FieldKey.YEAR)
                        && !tfk.equals(FieldKey.COMMENT)) {
                    tagFieldKeyArrayList.add(tfk.name());
                }
            }
        } catch (Exception e) {
            //Log.error(e);
        }
    }

    @Override
    public MusicTrack createMusicTrack() {
        return musicRepository.createMusicTrack();
    }

    @Override
    public Iterable<MusicTrack> getAll() {
        // TODO get all tracks from backend
        return null;
    }

    @Override
    public Iterable<MusicTrack> importFromFilesystem(Path sourceDirectory) {
        List<MusicTrack> list = new ArrayList<>();
        Path startingDir = Paths.get("/mnt/fileserver/Alle_Musik/CDs");
        try {
            Files.walkFileTree(startingDir, new FindMusicVisitor(list));
        } catch (IOException ex) {
            LOGGER.error(null, ex);
        }
        return list;
    }

    private class FindMusicVisitor extends SimpleFileVisitor<Path> {

        int i = 0;

        private final List<MusicTrack> list;

        private FindMusicVisitor(List<MusicTrack> list) {
            this.list = list;
        }

        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if ((file != null) && (attrs != null)) {
                if (file.getFileName().toString().endsWith(".ogg")
                        || file.getFileName().toString().endsWith(".mp3")) {
                    i++;
                    LOGGER.info("Parsing file {}", file.getFileName().toString());
                    File systemFile = file.toFile();
                    try {
                        String artist = "?", genre = "?", title = "?", album = "?", year = "?";
                        long length, trackNumber;
                        AudioFile f = AudioFileIO.read(systemFile);

                        Tag tag = f.getTag();
                        if (tag != null) {
                            artist = tag.getFirst(FieldKey.ARTIST);
                            title = tag.getFirst(FieldKey.TITLE);
                            album = tag.getFirst(FieldKey.ALBUM);
                            year = getYear(tag);
                            length = f.getAudioHeader().getTrackLength();
                            genre = getGenreName(tag);
                            trackNumber = getTrackNumber(tag);

                            MusicTrack musicTrack = createMusicTrack();
                            musicTrack.setAlbum(album);
                            musicTrack.setArtist(artist);
                            musicTrack.setFilepath(systemFile.getAbsolutePath());
                            musicTrack.setGenre(genre);
                            musicTrack.setLength(length);
                            musicTrack.setTitle(title);
                            musicTrack.setTrackNumber(trackNumber);
                            musicTrack.setPubYear(year);

                            list.add(musicTrack);
                        } else {
                            LOGGER.warn("File ''{}'' contains no tags.", file.toString());
                        }
                    } catch (Exception ex) {
                        LOGGER.warn("File ''{}'' not parsable.", file.toString());
                    }
                }
            }
            return FileVisitResult.CONTINUE;
        }
    }

    public long getTrackNumber(Tag tag) throws Exception {
        String trackNumber = tag.getFirst(FieldKey.TRACK);
        if (StringUtils.isEmpty(trackNumber)) {
            return 0;
        }
        if (trackNumber.matches(".*/.*")) {
            trackNumber = trackNumber.substring(0, trackNumber.indexOf('/'));
        }
        return Long.parseLong(trackNumber);
    }

    public String getYear(Tag tag) throws Exception {
        String result = tag.getFirst(FieldKey.YEAR);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        // The string contains at least a single character other than a digit,
        // then try to parse the first four digits if any
        if (PATTERN_NON_DIGIT.matcher(result).matches()) {
            Matcher matcher = PATTERN_FOUR_DIGITS.matcher(result);
            if (matcher.find()) {
                return matcher.group(1);
            } else {
                throw new NumberFormatException("Wrong year or date format");
            }
        } else {
            // Only digits
            return result;
        }
    }

    public String getGenreName(Tag tag) throws Exception {
        String result = tag.getFirst(FieldKey.GENRE);
        if (StringUtils.isEmpty(result) || "genre".equals(result)) {
            return null;
        }
        // format (nb)
        if (result.matches("\\(.*\\).*")) {
            result = result.substring(1, result.indexOf(')'));
            try {
                result = GENRES[Integer.parseInt(result)];
            } catch (Exception e) {
                return null;
            }
        }
        // If genre is a number mapping a known genre, use this genre
        try {
            int number = Integer.parseInt(result);
            if (number >= 0 && number < GENRES.length) {
                result = GENRES[number];
            }
        } catch (NumberFormatException e) {
            // nothing wrong here
        }
        return result;
    }
}
