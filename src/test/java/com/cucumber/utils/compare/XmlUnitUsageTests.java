package com.cucumber.utils.compare;

import com.cucumber.utils.engineering.utils.XmlUtils;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.*;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;


public class XmlUnitUsageTests {

    @Test
    public void checkXmlIsValid() {
        String controlXml = "<struct><int a=\"2\">3da</int><boolean>false</boolean></struct>";
        assertTrue(XmlUtils.isValid(controlXml));
    }

    @Test
    public void checkXmlIsValid_negative() {
        String controlXml = "<struct<int a=\"2\">3da</int><boolean>false</boolean></struct>";
        assertFalse(XmlUtils.isValid(controlXml));
    }

    @Test
    public void checkXmlHasInvalidAttributes_negative() {
        String controlXml = "<struct><int>3da</int a=\"2\"><boolean>false</boolean></struct>";
        assertFalse(XmlUtils.isValid(controlXml));
    }

    @Test
    public void checkXmlHasInvalidAttributeValues_negative() {
        String controlXml = "<struct><int a=2>3da</int><boolean>false</boolean></struct>";
        assertFalse(XmlUtils.isValid(controlXml));
    }

    @Test
    public void given2XMLS_whenIdentical_thenCorrect() {
        String controlXml = "<struct><int>3d~a</int><boolean>false</boolean></struct>";
        String testXml = "<struct><int>3d~a</int><boolean>false</boolean></struct>";
        assertThat(testXml, isSimilarTo(controlXml).withNodeMatcher(new DefaultNodeMatcher()));
    }

    @Test
    public void given2XMLS_whenSimilar_thenCorrect() {
        String controlXml = "<struct><int>3</int><boolean>false</boolean></struct>";
        String testXml = "<struct><boolean>false</boolean><int>3</int></struct>";

        assertThat(testXml, isSimilarTo(controlXml)
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName)));
    }

    @Test
    public void whenControlXmlIsIncludedInTextXml_thenCorrect() {
        String myControlXML = "<struct><boolean>false</boolean></struct>";
        String myTestXML = "<struct><boolean>false</boolean><int>3</int></struct>";

        Diff myDiff = DiffBuilder.compare(myControlXML).withTest(myTestXML).withDifferenceEvaluator(
                DifferenceEvaluators.chain(DifferenceEvaluators.Default, new DifferenceEvaluator() {
                    @Override
                    public ComparisonResult evaluate(Comparison comparison,
                                                     ComparisonResult comparisonResult) {
                        if (comparisonResult == ComparisonResult.EQUAL) {
                            return comparisonResult;
                        }
                        if (comparison.getType() == ComparisonType.CHILD_NODELIST_LENGTH
                                || comparison.getControlDetails().getTarget() == null) {
                            return ComparisonResult.SIMILAR;
                        }
                        return ComparisonResult.DIFFERENT;
                    }
                })).checkForSimilar()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText)).build();

        Iterator<Difference> iter = myDiff.getDifferences().iterator();
        int size = 0;
        while (iter.hasNext()) {
            Difference diff = iter.next();
            size++;
        }
        assertThat(size, equalTo(0));
    }

    @Test
    public void given2XMLsWithDifferences_use_own_logic() {
        final String control = "<root>\n" + "  <rather>-413687577.3070774</rather>\n"
                + "  <product>\n" + "    <interior attr=\"mostly_not_matched\">\n"
                + "      <select attr=\"mostly_not_matched\" distant=\"balloon\">\n"
                + "        <tube dried=\"create\">1821923088.010055</tube>\n" + "        <be>\n"
                + "          <tank>728900385</tank>\n" + "          <spring>fifth</spring>\n"
                + "          <globe>bite</globe>\n" + "          <cave>announced</cave>\n"
                + "        </be>\n" + "        <than>-2104276802.797926</than>\n"
                + "        <acres nothing=\"exist\">base</acres>\n" + "      </select>\n"
                + "      <mostly attr=\"mostly\" prove=\"draw\">report</mostly>\n"
                + "      <!--acres-->\n" + "      <did>control</did>\n"
                + "      <stop>swept</stop>\n" + "    </interior>\n"
                + "    <exercise sand=\"practical\">monkey</exercise>\n"
                + "    <!--source least fed sheet further onlinetools blank-->\n"
                + "    <!--farmer length daily-->\n" + "    <provide>-265519731</provide>\n"
                + "    <many southern=\"mass\">1427327046</many>\n" + "  </product>\n"
                + "  <nine>-1358340832.4822288</nine>\n"
                + "  <unit surrounded=\"him\">-701002634</unit>\n" + "</root>";
        final String test = "<root>\n" + "  <rather>-413687577.3070774</rather>\n" + "  <product>\n"
                + "    <interior attr=\"mostly_not_matched\">\n"
                + "      <select attr=\"mostly_not_matched\" distant=\"balloon\">\n"
                + "        <tube dried=\"create\">1821923088.010055</tube>\n" + "        <be>\n"
                + "          <tank>728900385</tank>\n" + "          <spring>fifth</spring>\n"
                + "          <globe>bite</globe>\n" + "          <cave>announced</cave>\n"
                + "        </be>\n" + "        <than>-2104276802.797926</than>\n"
                + "        <acres nothing=\"exist\">base</acres>\n" + "      </select>\n"
                + "      <mostly attr=\"mostly_matched2\" prove=\"draw\">report</mostly>\n"
                + "      <!--acres-->\n" + "      <did>control</did>\n"
                + "      <stop>swept</stop>\n" + "    </interior>\n"
                + "    <exercise sand=\"practical\">monkey</exercise>\n"
                + "    <!--source least fed sheet further onlinetools blank-->\n"
                + "    <!--farmer length daily-->\n" + "    <provide>-265519731</provide>\n"
                + "    <many southern=\"mass\">1427327046</many>\n" + "  </product>\n"
                + "  <nine>-1358340832.4822288</nine>\n"
                + "  <unit surrounded=\"him\">-701002634</unit>\n" + "</root>";
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
                .withDifferenceEvaluator(DifferenceEvaluators.chain(DifferenceEvaluators.Default,
                        new IgnoreAttributeDifferenceEvaluator()))
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName)));
    }
}
