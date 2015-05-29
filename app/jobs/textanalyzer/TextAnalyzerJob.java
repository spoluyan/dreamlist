package jobs.textanalyzer;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import play.Logger;
import play.jobs.Job;

public abstract class TextAnalyzerJob extends Job {
    protected enum Lang {
        DA, DU, EN, FI, FR, GE, HU, IT, NO, PO, RO, RU, SP, SW, TU, UNKNOWN
    }

    protected static final Pattern WORD_PATTERN = Pattern.compile("\\w+");

    @Override
    public void doJob() throws Exception {
        Logger.info("%s started", getClass().getName());
        long startTime = System.currentTimeMillis();

        doJobInternal();

        long endTime = System.currentTimeMillis();
        long executionTime = TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.MILLISECONDS);
        Logger.info("%s finished in %d seconds", getClass().getName(), executionTime);
    }

    protected abstract void doJobInternal();
}
