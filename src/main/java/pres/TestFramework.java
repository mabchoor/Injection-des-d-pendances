package pres;

import framework.context.ApplicationContext;
import metier.IMetier;

public class TestFramework {
    public static void main(String[] args) {
        // Test XML configuration
        ApplicationContext context = new ApplicationContext("beans.xml");
        IMetier metier = context.getBean("metier", IMetier.class);
        System.out.println("Result from XML configuration: " + metier.calul());

        // Test annotation-based configuration
        ApplicationContext context2 = new ApplicationContext("beans.xml");
        IMetier metier2 = context2.getBean(IMetier.class);
        System.out.println("Result from annotation configuration: " + metier2.calul());
    }
} 