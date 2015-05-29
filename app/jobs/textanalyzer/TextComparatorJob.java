package jobs.textanalyzer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

import javax.persistence.EntityManager;

import models.Dream;

import org.apache.commons.io.IOUtils;
import org.tartarus.snowball.SnowballStemmer;

import play.Play;
import play.db.jpa.JPA;
import play.jobs.Every;

@Every("10s")
public class TextComparatorJob extends TextAnalyzerJob {
    private static final int MIN_PERCENT_FOR_SIMILARITY = Integer.parseInt(Play.configuration
            .getProperty("analyzing.min-percent-to-similar"));

    @Override
    public void doJobInternal() {
        List<Dream> dreams = Dream.findAllPublicWithLanguage();
        dreams.parallelStream().forEach(
                dream -> {
                    dream.similarDreams = null;
                    dreams.parallelStream()
                            .filter(otherDream -> !(otherDream.id.equals(dream.id))
                                    || otherDream.user.id.equals(dream.user.id)).collect(Collectors.toList())
                            .parallelStream().forEach(dreamToCompare -> {
                                if (dreamsAreSimilar(dream, dreamToCompare)) {
                                    updateSimilarDreams(dream, dreamToCompare);
                                }
                            });
                });
    }

    private boolean dreamsAreSimilar(Dream dream, Dream dreamToCompare) {
        if (dream.guessedLanguage != dreamToCompare.guessedLanguage) {
            return false;
        }
        Set<String> dreamWords = getStemmedWords(dream.guessedLanguage, dream.dream);
        Set<String> dreamToCompareWords = getStemmedWords(dreamToCompare.guessedLanguage, dreamToCompare.dream);

        Set<Long> dreamShingles = shingles(dreamWords);
        Set<Long> dreamToCompareShingles = shingles(dreamToCompareWords);

        Set<Long> intersect = new TreeSet<Long>(dreamShingles);
        intersect.retainAll(dreamToCompareShingles);
        int same = intersect.size();
        int percent = (int) ((same * 2 / (float) (dreamShingles.size() + dreamToCompareShingles.size())) * 100);
        return percent >= MIN_PERCENT_FOR_SIMILARITY;
    }

    private Set<Long> shingles(Set<String> dreamWords) {
        CRC32 crc32 = new CRC32();
        SortedSet<Long> shingles = new TreeSet<Long>();
        dreamWords.forEach(word -> {
            crc32.update(word.getBytes());
            long crc32Value = crc32.getValue();
            shingles.add(crc32Value);
            crc32.reset();
        });
        return shingles;
    }

    private Set<String> getStemmedWords(String lang, String text) {
        Set<String> words = new HashSet<>();
        SnowballStemmer stemmer = StemmerFactory.getStemmerByLang(Lang.valueOf(lang));
        List<String> stopWords = loadStopWordsDictionary(lang);
        Matcher matcher = WORD_PATTERN.matcher(text);
        while (matcher.find()) {
            String word = matcher.group().toLowerCase();
            if (!stopWords.contains(word)) {
                stemmer.setCurrent(word);
                stemmer.stem();
                words.add(stemmer.getCurrent());
            }
        }
        return words;
    }

    private List<String> loadStopWordsDictionary(String guessedLanguage) {
        InputStream is = Play.classloader.getResourceAsStream("jobs/textanalyzer/dictionary/stop/stop_"
                + guessedLanguage + ".txt");
        try {
            List<String> dictionary = IOUtils.readLines(is);
            return dictionary;
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }

    private void updateSimilarDreams(Dream dream, Dream dreamToCompare) {
        Map<Long, Set<Long>> usersWithSimilarDreams = new HashMap<>();
        String similarDreams = dream.similarDreams;
        if (similarDreams != null) {
            usersWithSimilarDreams = dream.convertSimilarDreams();
        }
        Set<Long> dreamsIDS = usersWithSimilarDreams.get(dreamToCompare.user.id);
        if (dreamsIDS == null) {
            dreamsIDS = new HashSet<>();
        }
        dreamsIDS.add(dreamToCompare.id);
        usersWithSimilarDreams.put(dreamToCompare.user.id, dreamsIDS);
        dream.convertSimilarDreams(usersWithSimilarDreams);

        saveDream(dream);
    }

    private void saveDream(Dream dream) {
        EntityManager em = JPA.newEntityManager();
        JPA.bindForCurrentThread(JPA.DEFAULT, em, false);
        JPA.startTx(JPA.DEFAULT, false);
        Dream dreamToUpdate = Dream.findById(dream.id);
        dreamToUpdate.similarDreams = dream.similarDreams;
        dreamToUpdate.save();
        JPA.closeTx(JPA.DEFAULT);
    }
}
