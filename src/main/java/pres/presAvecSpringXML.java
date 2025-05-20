package pres;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class presAvecSpringXML {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
        metier.IMetier metier = (metier.IMetier) context.getBean(metier.IMetier.class );
        System.out.println("Res = "+metier.calul());
    }
}
