package dao;
import org.springframework.stereotype.Component;

@Component
public class DaoImpl implements IDao {
    @Override
    public double getData() {
        System.out.printf("Version base de données");
        double temp = 25;

        return temp;
    }
}
