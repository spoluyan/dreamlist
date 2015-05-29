package jobs.textanalyzer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import models.Dream;

import org.apache.commons.io.IOUtils;

import play.Play;
import play.jobs.Every;

@Every("5s")
public class GuessLanguageJob extends TextAnalyzerJob {
    @Override
    public void doJobInternal() {
        List<Dream> dreams = Dream.findByLanguageIsNull();
        dreams.forEach(dream -> {
            Lang language = guessLanguage(dream.dream);
            dream.guessedLanguage = language.name();
            dream.save();
        });
    }

    private Lang guessLanguage(String dream) {
        Lang currentGuess = Lang.UNKNOWN;
        long currentCounter = 0;

        Set<String> words = splitTextToWords(dream);

        for (Lang lang : Lang.values()) {
            List<String> dictionary = loadDictionary(lang);
            long count = words.stream().filter(word -> dictionary.contains(word)).count();
            if (count > currentCounter) {
                currentCounter = count;
                currentGuess = lang;
            }
        }
        return currentGuess;
    }

    private Set<String> splitTextToWords(String dream) {
        Set<String> words = new HashSet<>();
        Matcher matcher = WORD_PATTERN.matcher(dream);
        while (matcher.find()) {
            words.add(matcher.group().toLowerCase());
        }
        return words;
    }

    private List<String> loadDictionary(Lang lang) {
        InputStream is = Play.classloader.getResourceAsStream("jobs/textanalyzer/dictionary/voc_" + lang.name()
                + ".txt");
        try {
            List<String> dictionary = IOUtils.readLines(is);
            return dictionary;
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }
}
