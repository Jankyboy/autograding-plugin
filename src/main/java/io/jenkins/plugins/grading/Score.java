package io.jenkins.plugins.grading;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores the results of a scoring run.
 * Provides support for persisting the results of the build and loading.
 *
 * @author Eva-Maria Zeintl
 * @author Ullrich Hafner
 */
public class Score {
    private int grade;
    private Configuration configs;
    private final List<AnalysisScore> analysisScores = new ArrayList<>();
    private final List<CoverageScore> cocoBases = new ArrayList<>();
    private final List<PitScore> pitBases = new ArrayList<>();
    private final List<TestScore> junitBases = new ArrayList<>();
    private AnalysisConfiguration analysisConfiguration;
    private TestScore testsScore;
    private TestConfiguration testsConfiguration;
    private CoverageScore coverageScore;
    private CoverageConfiguration coverageConfiguration;
    private PitConfiguration pitConfiguration;
    private PitScore pitScore;

    /**
     * Creates a new instance of {@link Score}.
     * @param maxScore
     *          sets initial score
     */
    public Score(final int maxScore) {
        this.grade = maxScore;
    }

    /**
     * Creates a new instance of {@link Score}.
     */
    public Score() {
    }

    public int getScore() {
        return grade;
    }

    /**
     * increase score by change.
     * @param change calculated delta
     */
    public void addToScore(final int change) {
        this.grade = this.grade + change;
    }

    public Configuration getConfigs() {
        return configs;
    }

    public List<AnalysisScore> getAnalysisScores() {
        return analysisScores;
    }
    public List<PitScore> getPitBases() {
        return pitBases;
    }
    public List<TestScore> getJunitBases() {
        return junitBases;
    }
    public List<CoverageScore> getCocoBases() {
        return cocoBases;
    }

    public AnalysisConfiguration getAnalysisConfiguration() {
        return analysisConfiguration;
    }

    public TestConfiguration getTestsConfiguration() {
        return testsConfiguration;
    }

    public List<TestScore> getTestsScores() {
        return Collections.singletonList(testsScore);
    }

    public CoverageConfiguration getCoverageConfiguration() {
        return coverageConfiguration;
    }

    public List<CoverageScore> getCoverageScores() {
        return Collections.singletonList(coverageScore);
    }

    public PitConfiguration getPitConfiguration() {
        return pitConfiguration;
    }

    public List<PitScore> getPitScores() {
        return Collections.singletonList(pitScore);
    }

    /**
     * Save configurations.
     * @param inputConfig configurations read from xml
     */
    public void addConfigs(final Configuration inputConfig) {
        this.configs = inputConfig;
    }

    /**
     * Save Default results.
     * @param inputBase results from static checks
     */
    public void addAnalysisScore(final AnalysisScore inputBase) {
        this.analysisScores.add(inputBase);
    }

    /**
     * Save PIT results.
     * @param inputBases results from pit mutation check
     */
    public void addPitBase(final PitScore inputBases) {
        this.pitBases.add(inputBases);
    }

    /**
     * Save Coco results.
     * @param inputBases results from code coverage check
     */
    public void addCocoBase(final CoverageScore inputBases) {
        this.cocoBases.add(inputBases);
    }

    /**
     * Save junit results.
     * @param inputBases results from junit tests
     */
    public void addJunitBase(final TestScore inputBases) {
        this.junitBases.add(inputBases);
    }

    public int addAnalysisTotal(final AnalysisConfiguration configuration, final List<AnalysisScore> scores) {
        analysisScores.addAll(scores);
        analysisConfiguration = configuration;

        int delta = 0;
        for (AnalysisScore score : scores) {
            delta = delta + score.getTotalChange();
        }

        int actual;
        if (delta <= 0) {
            actual = Math.max(0, configuration.getMaxScore() + delta);
        }
        else {
            actual = Math.min(configuration.getMaxScore(), delta);
        }

        grade += actual;
        return actual;
    }

    public int addTestsTotal(final TestConfiguration configuration, final TestScore scores) {
        testsScore = scores;
        testsConfiguration = configuration;

        int actual;
        if (testsScore.getTotalChange() <= 0) {
            actual = Math.max(0, configuration.getMaxScore() + testsScore.getTotalChange());
        }
        else {
            actual = Math.min(configuration.getMaxScore(), testsScore.getTotalChange());
        }

        grade += actual;
        return actual;
    }

    public int addCoverageTotal(final CoverageConfiguration coverageConfiguration, final CoverageScore coverageScore) {
        this.coverageScore = coverageScore;
        this.coverageConfiguration = coverageConfiguration;

        int actual;
        if (testsScore.getTotalChange() <= 0) {
            actual = Math.max(0, coverageConfiguration.getMaxScore() + testsScore.getTotalChange());
        }
        else {
            actual = Math.min(coverageConfiguration.getMaxScore(), testsScore.getTotalChange());
        }

        grade += actual;
        return actual;
    }

    public int addPitTotal(final PitConfiguration pitConfiguration, final PitScore pitScore) {
        this.pitConfiguration = pitConfiguration;
        this.pitScore = pitScore;

        int actual;
        if (testsScore.getTotalChange() <= 0) {
            actual = Math.max(0, coverageConfiguration.getMaxScore() + testsScore.getTotalChange());
        }
        else {
            actual = Math.min(coverageConfiguration.getMaxScore(), testsScore.getTotalChange());
        }

        grade += actual;
        return actual;
    }
}
