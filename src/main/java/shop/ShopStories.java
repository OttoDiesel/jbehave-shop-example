package shop;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;
import static org.jbehave.core.reporters.Format.TXT;
import static org.jbehave.core.reporters.Format.XML;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang.StringUtils;
import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.context.Context;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.executors.SameThreadExecutors;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.ConsoleOutput;
import org.jbehave.core.reporters.ContextOutput;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.MarkUnmatchedStepsAsPending;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.NumberConverter;
import org.jbehave.core.steps.ParameterConverters.ParameterConverter;

import shop.steps.LifecycleSteps;
import shop.steps.OrderStep;

public final class ShopStories extends JUnitStories {

    private Configuration configuration;

    private final Context context = new Context();
    private final Format contextFormat = new ContextOutput(this.context);
    private final CrossReference xref = new CrossReference();

    public ShopStories() {
        ExecutorService service = new SameThreadExecutors().create(configuredEmbedder().embedderControls());
        configuredEmbedder().useExecutorService(service);
    }

    @Override
    public void run() throws Throwable {
        try {
            Embedder embedder = configuredEmbedder();
            // Dieses Attribut kann nicht direkt in Mavens pom.xml gesetzt werden, daher so:
            String dryRun = embedder.systemProperties().getProperty("dryRun");
            if (!StringUtils.isEmpty(dryRun)) {
                this.configuration.doDryRun(Boolean.valueOf(dryRun));
            }
            super.run();
        } finally {
            browseToReports();
        }
    }
    
    private void browseToReports() throws IOException {
        String reports = MessageFormat.format("{0}/view/reports.html", this.configuration.storyReporterBuilder().outputDirectory());
        File reportsFile = new File(reports);
        if (reportsFile.exists() && Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(reportsFile.toURI());
        }
    }

    @Override
    public Configuration configuration() {
        if (this.configuration == null) {
            Class<? extends Embeddable> embeddableClass = this.getClass();
            // Es ist aufwändiger als gedacht, die Locale auf Deutsch zu ändern:
            Keywords keywords = new LocalizedKeywords(getLocale());
            Properties resources = new Properties();
            resources.setProperty("encoding", "UTF-8");
            this.configuration = new MostUsefulConfiguration();
            this.configuration
                    .useKeywords(keywords)
                    .useStepCollector(new MarkUnmatchedStepsAsPending(keywords))
                    .useStoryParser(new RegexStoryParser(keywords))
                    .useDefaultStoryReporter(new ConsoleOutput(keywords))
                    .useParameterConverters(new ParameterConverters().addConverters(customConverters()))
                    .useStoryLoader(new LoadFromClasspath(embeddableClass))
                    // Sonst verhält sich das Parameter-Parsing in Beispieltabellen in manchen Fällen eigenartig:
                    .useParameterControls(new ParameterControls().useDelimiterNamedParameters(true))
                    .useStoryReporterBuilder(new StoryReporterBuilder()
                            .withKeywords(keywords)
                            .withViewResources(resources)
                            .withCodeLocation(codeLocationFromClass(embeddableClass))
                            .withFailureTrace(false)
                            .withCrossReference(this.xref)
                            .withDefaultFormats()
                            .withFormats(this.contextFormat, CONSOLE, HTML, XML, TXT));
            // Sonst verhält sich die Filterung anhand von Meta-Infos bei vorgegebenen Stories ungünstig:
            this.configuration.storyControls().doIgnoreMetaFiltersIfGivenStory(true);
        }
        return this.configuration;
    }

    private Locale getLocale() {
        return Locale.GERMAN;
    }

    private ParameterConverter[] customConverters() {
        List<ParameterConverter> converters = new ArrayList<>();
        converters.add(new NumberConverter(NumberFormat.getInstance(getLocale())));
        return converters.toArray(new ParameterConverter[converters.size()]);
    }

    /* 
     * Die Steps können auch generisch ermittelt werden.
     * Siehe: http://jbehave.org/reference/stable/finding-steps.html
     */
    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new LifecycleSteps(), new OrderStep());
    }

    /* 
     * Die Story-Pfade können auch generisch ermittelt werden.
     * Siehe: http://jbehave.org/reference/stable/locating-stories.html
     */
    @Override
    protected List<String> storyPaths() {
        List<String> storyPaths = new ArrayList<>();
        storyPaths.add("shop/stories/Bestellung.story");
        //storyPaths.add("shop/stories/Gegenprobe.story");
        //storyPaths.add("shop/stories/Leere.story");
        return storyPaths;
    }
    
}
