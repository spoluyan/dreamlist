package jobs.textanalyzer;

import jobs.textanalyzer.TextAnalyzerJob.Lang;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.danishStemmer;
import org.tartarus.snowball.ext.dutchStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.finnishStemmer;
import org.tartarus.snowball.ext.frenchStemmer;
import org.tartarus.snowball.ext.germanStemmer;
import org.tartarus.snowball.ext.hungarianStemmer;
import org.tartarus.snowball.ext.italianStemmer;
import org.tartarus.snowball.ext.norwegianStemmer;
import org.tartarus.snowball.ext.porterStemmer;
import org.tartarus.snowball.ext.portugueseStemmer;
import org.tartarus.snowball.ext.romanianStemmer;
import org.tartarus.snowball.ext.russianStemmer;
import org.tartarus.snowball.ext.spanishStemmer;
import org.tartarus.snowball.ext.swedishStemmer;
import org.tartarus.snowball.ext.turkishStemmer;

public final class StemmerFactory {
    private StemmerFactory() {
    }

    public static SnowballStemmer getStemmerByLang(Lang lang) {
        switch (lang) {
        case DA:
            return new danishStemmer();
        case DU:
            return new dutchStemmer();
        case EN:
            return new englishStemmer();
        case FI:
            return new finnishStemmer();
        case FR:
            return new frenchStemmer();
        case GE:
            return new germanStemmer();
        case HU:
            return new hungarianStemmer();
        case IT:
            return new italianStemmer();
        case NO:
            return new norwegianStemmer();
        case PO:
            return new portugueseStemmer();
        case RO:
            return new romanianStemmer();
        case RU:
            return new russianStemmer();
        case SP:
            return new spanishStemmer();
        case SW:
            return new swedishStemmer();
        case TU:
            return new turkishStemmer();
        case UNKNOWN:
            return new porterStemmer();
        }
        return new porterStemmer();
    }
}
