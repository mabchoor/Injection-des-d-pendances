package metier;

public class MetierImpl implements metier.IMetier {

    private dao.IDao dao;

    public MetierImpl(dao.IDao dao) {
        this.dao = dao;
    }

    public MetierImpl() {
    }

    @Override
    public double calul() {
        double t = dao.getData();
        double res = t * 23;
        return res;
    }

    public void setDao(dao.IDao dao) {
        this.dao = dao;
    }
}
