package ext;

public class DaoImplV2 implements dao.IDao {

    @Override
    public double getData() {
        System.out.println("DaoImplV2 version web service");
        return 77;
    }
}
