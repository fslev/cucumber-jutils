package tests.ro.cucumber.core;

import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.*;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;


public class XmlUnitTests {
    @Test
    public void given2XMLS_whenIdentical_thenCorrect() {
        String controlXml = "<struct><int>3d~a</int><boolean>false</boolean></struct>";
        String testXml = "<struct><int>3d~a</int><boolean>false</boolean></struct>";
        assertThat(testXml, isIdenticalTo(controlXml));
    }

    @Test
    public void given2XMLS_whenSimilar_thenCorrect() {
        String controlXml = "<struct><int>3</int><boolean>false</boolean></struct>";
        String testXml = "<struct><boolean>false</boolean><int>3</int></struct>";

        assertThat(testXml, isSimilarTo(controlXml).withNodeMatcher(
                new DefaultNodeMatcher(ElementSelectors.byName)));
    }

    @Test
    public void given2XMLS_whenSimilar_withDiffs() {
        String myControlXML = "<struct><int>3</int><boolean>false</boolean></struct>";
        String myTestXML = "<struct><boolean>false</boolean><int>3</int></struct>";

        Diff myDiff = DiffBuilder
                .compare(myControlXML)
                .withTest(myTestXML).checkForSimilar()
                .withComparisonController(ComparisonControllers.StopWhenDifferent)
                .build();

        Iterator<Difference> iter = myDiff.getDifferences().iterator();
        int size = 0;
        while (iter.hasNext()) {
            iter.next().toString();
            size++;
        }
        assertThat(size, equalTo(1));
    }

    @Test
    public void given2XMLsWithDifferences_whenTestsSimilarWithDifferenceEvaluator_thenCorrect() {
        final String control = "<a><z>test</z><b attr=\"abc\">waa</b></a>";
        final String test = "<a><b attr=\"xyz\">waa</b><z>test</z></a>";
        class IgnoreAttributeDifferenceEvaluator implements DifferenceEvaluator {
            private String attributeName;

            public IgnoreAttributeDifferenceEvaluator(String attributeName) {
                this.attributeName = attributeName;
            }

            @Override
            public ComparisonResult evaluate(Comparison comparison, ComparisonResult outcome) {
                if (outcome == ComparisonResult.EQUAL)
                    return outcome;
                final Node controlNode = comparison.getControlDetails().getTarget();
                if (controlNode instanceof Attr) {
                    Attr attr = (Attr) controlNode;
                    if (attr.getName().equals(attributeName)) {
                        return ComparisonResult.SIMILAR;
                    }
                }
                return outcome;
            }
        }

        assertThat(test, isSimilarTo(control)
                .withDifferenceEvaluator(DifferenceEvaluators
                        .chain(DifferenceEvaluators.Default, new IgnoreAttributeDifferenceEvaluator("attr")))
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName)));
    }

    @Test
    public void given2XMLsWithDifferences_use_own_logic() {
        final String control = "<root>\n" +
                "  <rather>-413687577.3070774</rather>\n" +
                "  <product>\n" +
                "    <interior attr=\"mostly_not_matched\">\n" +
                "      <select attr=\"mostly_not_matched\" distant=\"balloon\">\n" +
                "        <tube dried=\"create\">1821923088.010055</tube>\n" +
                "        <be>\n" +
                "          <tank>728900385</tank>\n" +
                "          <spring>fifth</spring>\n" +
                "          <globe>bite</globe>\n" +
                "          <cave>announced</cave>\n" +
                "        </be>\n" +
                "        <than>-2104276802.797926</than>\n" +
                "        <acres nothing=\"exist\">base</acres>\n" +
                "      </select>\n" +
                "      <mostly attr=\"mostly\" prove=\"draw\">report</mostly>\n" +
                "      <!--acres-->\n" +
                "      <did>control</did>\n" +
                "      <stop>swept</stop>\n" +
                "    </interior>\n" +
                "    <exercise sand=\"practical\">monkey</exercise>\n" +
                "    <!--source least fed sheet further onlinetools blank-->\n" +
                "    <!--farmer length daily-->\n" +
                "    <provide>-265519731</provide>\n" +
                "    <many southern=\"mass\">1427327046</many>\n" +
                "  </product>\n" +
                "  <nine>-1358340832.4822288</nine>\n" +
                "  <unit surrounded=\"him\">-701002634</unit>\n" +
                "</root>";
        final String test = "<root>\n" +
                "  <rather>-413687577.3070774</rather>\n" +
                "  <product>\n" +
                "    <interior attr=\"mostly_not_matched\">\n" +
                "      <select attr=\"mostly_not_matched\" distant=\"balloon\">\n" +
                "        <tube dried=\"create\">1821923088.010055</tube>\n" +
                "        <be>\n" +
                "          <tank>728900385</tank>\n" +
                "          <spring>fifth</spring>\n" +
                "          <globe>bite</globe>\n" +
                "          <cave>announced</cave>\n" +
                "        </be>\n" +
                "        <than>-2104276802.797926</than>\n" +
                "        <acres nothing=\"exist\">base</acres>\n" +
                "      </select>\n" +
                "      <mostly attr=\"mostly_matched2\" prove=\"draw\">report</mostly>\n" +
                "      <!--acres-->\n" +
                "      <did>control</did>\n" +
                "      <stop>swept</stop>\n" +
                "    </interior>\n" +
                "    <exercise sand=\"practical\">monkey</exercise>\n" +
                "    <!--source least fed sheet further onlinetools blank-->\n" +
                "    <!--farmer length daily-->\n" +
                "    <provide>-265519731</provide>\n" +
                "    <many southern=\"mass\">1427327046</many>\n" +
                "  </product>\n" +
                "  <nine>-1358340832.4822288</nine>\n" +
                "  <unit surrounded=\"him\">-701002634</unit>\n" +
                "</root>";
        class IgnoreAttributeDifferenceEvaluator implements DifferenceEvaluator {

            @Override
            public ComparisonResult evaluate(Comparison comparison, ComparisonResult outcome) {
                if (outcome == ComparisonResult.EQUAL)
                    return outcome;
                final Node controlNode = comparison.getControlDetails().getTarget();
                final Node testNode = comparison.getTestDetails().getTarget();
                if (controlNode instanceof Attr && testNode instanceof Attr) {
                    Attr attr = (Attr) controlNode;
                    if (attr.getValue().contains("mostly")) {
                        return ComparisonResult.SIMILAR;
                    }
                }
                return outcome;
            }
        }

        assertThat(test, isSimilarTo(control)
                .withDifferenceEvaluator(DifferenceEvaluators
                        .chain(DifferenceEvaluators.Default, new IgnoreAttributeDifferenceEvaluator()))
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName)));
    }
}
