package MTCG.dal.uow;

public interface IUnitOfWork {

    void beginTransaction();
    boolean commit();
    void rollback();
}
