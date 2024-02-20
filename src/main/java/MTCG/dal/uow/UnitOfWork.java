package MTCG.dal.uow;

public class UnitOfWork implements IUnitOfWork{

    @Override
    public boolean commit() {
        return false;
    }

    @Override
    public void rollback(){

    }

    public void beginTransaction(){

    }

}
