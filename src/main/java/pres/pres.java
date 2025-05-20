package pres;
import metier.MetierImpl;
import dao.DaoImpl;
public class pres {
    public static void main(String[] args) {
        DaoImpl dao = new DaoImpl();
        MetierImpl m = new MetierImpl(dao);
        //m.setDao(dao);

        System.out.println(m.calul());
    }
}
